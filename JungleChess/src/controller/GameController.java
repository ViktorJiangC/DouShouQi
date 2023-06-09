package controller;
import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessComponent;
import view.ChessboardComponent;

import java.io.Serial;
import java.io.Serializable;

import static controller.MinMaxAlgorithm.*;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener, Serializable {


    @Serial
    private static final long serialVersionUID = 577898782661302314L;

    public GameController(ChessboardComponent view, GameController gameController2) {
        this.view = view;
        this.model = gameController2.model;
        this.currentPlayer = gameController2.currentPlayer;

        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }

    public void setModel(Chessboard model) {
        this.model = model;
    }

    private Chessboard model;

    public void setView(ChessboardComponent view) {
        this.view = view;
    }

    private ChessboardComponent view;
    private PlayerColor currentPlayer;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    public Chessboard getModel() {
        return model;
    }
    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;

        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }

    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
            }
        }
    }
    // after a valid move swap the player
    private void swapColor() {
        trappedTurnMultiplier();
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
    }
    //Trapped chess piece turn++
    private void trappedTurnMultiplier() {
        int[][] trapLocationRed = {{0, 2}, {0, 4}, {1, 3}};
        for (int[] ints : trapLocationRed) {
            ChessboardPoint point = new ChessboardPoint(ints[0], ints[1]);
            ChessPiece p = model.getChessPieceAt(point);

            if(p != null && p.getTrappedturn()==0 && p.getOwner() == PlayerColor.BLUE) {
                p.setTrapped(true);
                p.setTrappedturn(p.getTrappedturn() + 1);
            }
            if(p != null && p.getTrappedturn()>=0 && p.getOwner() == PlayerColor.BLUE) {
                p.setTrappedturn(p.getTrappedturn() + 1);
            }
            //PvP这里改成>4
            if (p != null && p.getTrappedturn() > 4) {
                p.setTrapped(false);
            }
        }
        int[][] trapLocationBlue = {{8, 2}, {8, 4}, {7, 3}};
        for (int[] ints : trapLocationBlue) {
            ChessboardPoint point = new ChessboardPoint(ints[0], ints[1]);
            ChessPiece p = model.getChessPieceAt(point);
            if(p != null && p.getTrappedturn()==0 && p.getOwner() == PlayerColor.RED) {
                p.setTrapped(true);
                p.setTrappedturn(p.getTrappedturn() + 1);
            }
            if(p != null && p.getTrappedturn()>=0 && p.getOwner() == PlayerColor.RED) {
                p.setTrappedturn(p.getTrappedturn() + 1);
            }
            //PvP这里改成>4
            if (p != null && p.getTrappedturn() > 4) {
                p.setTrapped(false);
            }
        }
    }
    private void trappedTurnEraser() {
        int[][] untrapLocation = {{0, 1}, {0, 3}, {0, 5},{1, 2},{1, 4},{2, 3},{6, 3},{7, 2},{7, 4},{8, 1},{8, 3},{8, 5}};
        for (int[] ints : untrapLocation) {
            ChessboardPoint point = new ChessboardPoint(ints[0], ints[1]);
            ChessPiece p = model.getChessPieceAt(point);
            if (p != null && p.getTrappedturn() > 0) {
                p.setTrappedturn(0);
            }
        }
    }
    @Override
    //点击空白格子
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            //下面三行移动棋子到新的位置
            model.moveChessPiece(selectedPoint, point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            view.repaint();
            //清空选中的棋子
            selectedPoint = null;
            trappedTurnMultiplier();
            trappedTurnEraser();
            //交换玩家   PvP 就把makeAIMove换成 swapColor()，再保留之后的repaint
            makeAIMove();
            win();
            //view.repaint();
        }
    }
    public static Chessboard copyFrom(Chessboard B){
        Chessboard c = new Chessboard();
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint point = new ChessboardPoint(i, j);
                ChessPiece p = B.getChessPieceAt(point);
                c.setChessPiece(point, p);
            }
        }
        return c;
    }
    private void makeAIMove() {

        Chessboard c = copyFrom(model);
    //将目前的棋盘复制到c
    minimax(model, true,0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        model = copyFrom(c);
    ChessboardPoint esrc = easyMove(model)[0];
    ChessboardPoint edest = easyMove(model)[1];
    //将c的棋子复制回到model
    model = copyFrom(c);
    ChessboardPoint src = bMove[0];
    ChessboardPoint dest = bMove[1];

    loop:
            for(int i=0; i<=1; i++) {
                if (model.getChessPieceAt(src) != null && model.getChessPieceAt(dest) == null) {
                    if (getModel().isValidMove(src, dest)) {
                        model.getChessPieceAt(src).setRepeatTurn(model.getChessPieceAt(src).getRepeatTurn() + 1);
                        view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                        getModel().moveChessPiece(src, dest);
                        view.repaint();
                        break loop;
                    }
                } else {
                    if (model.getChessPieceAt(esrc) != null && model.getChessPieceAt(edest) == null) {
                        if (getModel().isValidMove(esrc, edest)) {
                            model.getChessPieceAt(esrc).setRepeatTurn(model.getChessPieceAt(esrc).getRepeatTurn() + 1);
                            view.setChessComponentAtGrid(edest, view.removeChessComponentAtGrid(esrc));
                            getModel().moveChessPiece(esrc, edest);
                            view.repaint();
                            break loop;
                        }
                    }
                }
                if (model.getChessPieceAt(src) != null && model.getChessPieceAt(dest) != null) {
                    if (getModel().isValidCapture(src, dest)) {
                        model.getChessPieceAt(src).setRepeatTurn(model.getChessPieceAt(src).getRepeatTurn() + 1);
                        view.removeChessComponentAtGrid(dest);
                        view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                        getModel().captureChessPiece(src, dest);
                        view.repaint();
                        break loop;
                    }
                } else if ((model.getChessPieceAt(esrc) != null && model.getChessPieceAt(edest) != null)) {
                    if (getModel().isValidCapture(esrc, edest)) {
                        model.getChessPieceAt(esrc).setRepeatTurn(model.getChessPieceAt(esrc).getRepeatTurn() + 1);
                        view.removeChessComponentAtGrid(edest);
                        view.setChessComponentAtGrid(edest, view.removeChessComponentAtGrid(esrc));
                        getModel().captureChessPiece(esrc, edest);
                        view.repaint();
                        break loop;
                    }
                }
            }
    trappedTurnMultiplier();
    trappedTurnEraser();
    }
    public void win() {
        ChessboardPoint redDens = new ChessboardPoint(0, 3);
        ChessboardPoint blueDens = new ChessboardPoint(8, 3);
        if (model.getChessPieceAt(redDens) != null && model.getChessPieceAt(redDens).getOwner() == PlayerColor.BLUE) {
            //此处应该有弹窗
            System.out.println("Blue win!");
        }
        if (model.getChessPieceAt(blueDens) != null && model.getChessPieceAt(blueDens).getOwner() == PlayerColor.RED) {
            //此处应该有弹窗
            System.out.println("Red win!");
        }
    }
    @Override
    //点击有棋子的格子
    public void onPlayerClickChessPiece(ChessboardPoint point, ChessComponent component) {
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                //如果这俩棋子属于当前玩家的，就选中这个棋子
                selectedPoint = point;
                component.setSelected(true);
                component.repaint();
            }
        } else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            component.setSelected(false);
            component.repaint();
        }else if(model.isValidCapture(selectedPoint,point) ){
            //如果这棋子不属于当前玩家，就吃掉这个棋子
            view.removeChessComponentAtGrid(point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            model.captureChessPiece(selectedPoint, point);
            trappedTurnMultiplier();
            trappedTurnEraser();
            selectedPoint = null;
            view.repaint();
            //swapColor();PvP 就把makeAIMove换成 swapColor()
            makeAIMove();
            win();
        }
    }
}
