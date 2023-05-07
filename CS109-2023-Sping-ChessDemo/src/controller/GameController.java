package controller;
import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessComponent;
import view.ChessboardComponent;
/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener {
    private Chessboard model;
    private ChessboardComponent view;
    private PlayerColor currentPlayer;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
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
        int[][] trapLocation = {{0, 2}, {0, 4}, {1, 3}, {8, 2}, {8, 4}, {7, 3}};
        for (int[] ints : trapLocation) {
            ChessboardPoint point = new ChessboardPoint(ints[0], ints[1]);
            ChessPiece p = model.getChessPieceAt(point);
            if(p != null && p.getTrappedturn()==0) {
                p.setTrapped(true);
                p.setTrappedturn(p.getTrappedturn() + 1);
            }
            if(p != null && p.getTrappedturn()>=0) {
                p.setTrappedturn(p.getTrappedturn() + 1);
            }
            if (p != null && p.getTrappedturn() > 4) {
                p.setTrappedturn(0);
                p.setTrapped(false);
            }
        }
    }

    //1:blue win 2:red win 0:continue
    private int winner() {
         if(model.getChessPieceOwner(new ChessboardPoint(0, 3)).equals(PlayerColor.BLUE)){
             return 1;
         }
         if(model.getChessPieceOwner(new ChessboardPoint(8, 3)).equals(PlayerColor.RED)){
             return 2;
         }
         return 0;
    }
    private void gameOver() {
        int winner = winner();
        if (winner == 1) {
            view.showWinner(PlayerColor.BLUE);
        } else if (winner == 2) {
            view.showWinner(PlayerColor.RED);
        }
    }
    // click an empty cell
    @Override
    //通过点击来实现移动的方法
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            //下面三行移动棋子到新的位置
            model.moveChessPiece(selectedPoint, point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            selectedPoint = null;
            //交换玩家
            swapColor();
            //重新绘图
            view.repaint();
        }
    }
    // click a cell with a chess
    @Override
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
            selectedPoint = null;
            view.repaint();
            swapColor();
        }
    }

}
