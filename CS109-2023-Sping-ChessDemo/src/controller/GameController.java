package controller;
import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessComponent;
import view.ChessboardComponent;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import static controller.MinMaxAlgorithm.*;
import static controller.MinMaxAlgorithm.bMove;


/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/
public class GameController implements GameListener, Serializable {
    private static Chessboard model;
    public static int turn=0;
    private static ChessboardComponent view;
    private static PlayerColor currentPlayer;

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    private static ArrayList<ChessboardPoint> initPoint=new ArrayList<>();
    private static ArrayList<ChessboardPoint> nowPoint=new ArrayList<>();
    private static ArrayList<ChessPiece> initPiece=new ArrayList<>();
    private static ArrayList<ChessboardPoint> eatenPoint=new ArrayList<>();
    private ChessboardPoint[][] points=new ChessboardPoint[9][7];
    private static int eats=0;
    private static ArrayList<Integer> eatsTurn=new ArrayList<>();
    CellComponent[][] cellComponent=new CellComponent[9][7];
    private int mode = 0;
    public void setMode(int mode) {
        this.mode = mode;
    }
    public Chessboard getModel() {
        return model;
    }
    @Serial
    private static final long serialVersionUID = 577898782661302314L;

    public static PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }
    public GameController(ChessboardComponent view, Chessboard model) {
        GameController.view = view;
        GameController.model = model;
        currentPlayer = PlayerColor.BLUE;

        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }
    public GameController(ChessboardComponent view, GameController gameController2) {
        this.view = view;
        this.model = gameController2.model;
        this.currentPlayer = gameController2.currentPlayer;

        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
    }


    public static void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
            }
        }
    }

    public static void resetGame() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                ChessboardPoint p = new ChessboardPoint(i, j);
                if(model.getChessPieceAt(p)!=null){
                    view.removeChessComponentAtGrid(p);
                }
            }
        }
        model = new Chessboard();
        view.initiateChessComponent(model);
        view.repaint();
        turn=0;
        currentPlayer = PlayerColor.BLUE;
    }

    // after a valid move swap the player
    private void swapColor() {
        trappedTurnMultiplier();
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
        turn++;
    }
    public static void Huiqi(){
        model.moveChessPieceWithoutLaw(nowPoint.get(turn-1),initPoint.get(turn-1));
        view.setChessComponentAtGrid(initPoint.get(turn-1), view.removeChessComponentAtGrid(nowPoint.get(turn-1)));

            if(!initPiece.isEmpty()){
                if (initPiece.get(eats-1)!=null){
                    if(eatsTurn.get(eats-1)==turn){
                    model.addPiece(initPiece.get(eats-1),eatenPoint.get(eats-1));
                    view.addChessComponent(eatenPoint.get(eats-1),initPiece.get(eats-1));
                    ArrayList<ChessPiece>P=new ArrayList<>();
                    int i=0;
                    for (ChessPiece p:initPiece){
                        if (i!=eats-1) P.add(p);
                        i++;
                    }
                    initPiece=P;
                    eats--;
                    ArrayList<Integer> t=new ArrayList<>();
                    int x=0;
                    for (int y: eatsTurn){
                        if(x!=eatsTurn.size()-1) t.add(y);
                        x++;
                    }
                    eatsTurn=t;
                    }
            }
        }

        view.repaint();
        ArrayList<ChessboardPoint>cbp=new ArrayList<>();
        int j=0;
        for (ChessboardPoint p:initPoint){
            if (j!=turn-1) cbp.add(p);
            j++;
        }
        initPoint=cbp;
        ArrayList<ChessboardPoint>Cbp=new ArrayList<>();
        int k=0;
        for (ChessboardPoint p:nowPoint){
            if (j!=turn-1) Cbp.add(p);
            k++;
        }
        nowPoint=Cbp;

        int[][] trapLocationRed = {{0, 2}, {0, 4}, {1, 3}};
        for (int[] ints : trapLocationRed) {
            ChessboardPoint point = new ChessboardPoint(ints[0], ints[1]);
            ChessPiece p = model.getChessPieceAt(point);
            if(p != null && p.getTrappedturn()==0 && p.getOwner() == PlayerColor.BLUE) {
                p.setTrapped(true);
                p.setTrappedturn(p.getTrappedturn() - 1);
            }
            if(p != null && p.getTrappedturn()>=0 && p.getOwner() == PlayerColor.BLUE) {
                p.setTrappedturn(p.getTrappedturn() - 1);
            }
            if (p != null && p.getTrappedturn() > 4) {
                p.setTrappedturn(0);
                p.setTrapped(false);
            }
        }
        int[][] trapLocationBlue = {{8, 2}, {8, 4}, {7, 3}};
        for (int[] ints : trapLocationBlue) {
            ChessboardPoint point = new ChessboardPoint(ints[0], ints[1]);
            ChessPiece p = model.getChessPieceAt(point);
            if(p != null && p.getTrappedturn()==0 && p.getOwner() == PlayerColor.RED) {
                p.setTrapped(true);
                p.setTrappedturn(p.getTrappedturn() - 1);
            }
            if(p != null && p.getTrappedturn()>=0 && p.getOwner() == PlayerColor.RED) {
                p.setTrappedturn(p.getTrappedturn() - 1);
            }
            if (p != null && p.getTrappedturn() > 4) {
                p.setTrappedturn(0);
                p.setTrapped(false);
            }
        }
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
        turn--;
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
            if (p != null && p.getTrappedturn() > 4) {
                p.setTrappedturn(0);
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
            if (p != null && p.getTrappedturn() > 4) {
                p.setTrappedturn(0);
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
            nowPoint.add(point);
            //下面三行移动棋子到新的位置
            model.moveChessPiece(selectedPoint, point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            view.repaint();
            for(int x=0;x<9;x++){
                for (int y = 0; y < 7; y++) {
                    cellComponent[x][y].setIsValidMoveTo(false);
                    cellComponent[x][y].repaint();
                }
            }
            //清空选中的棋子
            selectedPoint = null;
            trappedTurnMultiplier();
            trappedTurnEraser();
            if(mode == 0){
                swapColor();
            }
            if(mode ==1){
                makeAIMove();
            }
            if(mode ==2){
                SmartAI();
            }
            view.repaint();
            win();
            //交换玩家
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
    public void win() {
        boolean redPiecesAlive = false;
        boolean bluePiecesAlive = false;

        // 判断红方是否有存活的棋子
        for (ChessboardPoint point : getRedPieces(model)) {
            ChessPiece piece = model.getChessPieceAt(point);
            if (piece.isAlive()) {
                redPiecesAlive = true;
                break;
            }
        }
        // 判断蓝方是否有存活的棋子
        for (ChessboardPoint point : getBluePieces(model)) {
            ChessPiece piece = model.getChessPieceAt(point);
            if (piece.isAlive()) {
                bluePiecesAlive = true;
                break;
            }
        }
        if (!redPiecesAlive) {
            // 红方所有子被消灭，蓝方胜利
            System.out.println("Blue wins!");
            // TODO: 显示弹窗或进行其他处理
        } else if (!bluePiecesAlive) {
            // 蓝方所有子被消灭，红方胜利
            System.out.println("Red wins!");
            // TODO: 显示弹窗或进行其他处理
        }
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
    private void SmartAI(){
        Chessboard c = copyFrom(model);
        ChessboardPoint esrc = easyMove(c)[0];
        ChessboardPoint edest = easyMove(c)[1];
        if (model.getChessPieceAt(esrc) != null && model.getChessPieceAt(edest) == null) {
            if (getModel().isValidMove(esrc, edest)) {
                model.getChessPieceAt(esrc).setRepeatTurn(model.getChessPieceAt(esrc).getRepeatTurn() + 1);
                view.setChessComponentAtGrid(edest, view.removeChessComponentAtGrid(esrc));
                getModel().moveChessPiece(esrc, edest);
                view.repaint();
            }
        }
        if ((model.getChessPieceAt(esrc) != null && model.getChessPieceAt(edest) != null)) {
            if (getModel().isValidCapture(esrc, edest)) {
                model.getChessPieceAt(esrc).setRepeatTurn(model.getChessPieceAt(esrc).getRepeatTurn() + 1);
                view.removeChessComponentAtGrid(edest);
                view.setChessComponentAtGrid(edest, view.removeChessComponentAtGrid(esrc));
                getModel().captureChessPiece(esrc, edest);
                view.repaint();
            }
        }
    }
    private void makeAIMove() {

        Chessboard c = copyFrom(model);
        //将目前的棋盘复制到c
        minimax(model, true,0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        model = copyFrom(c);
        ChessboardPoint esrc = easyMove(c)[0];
        ChessboardPoint edest = easyMove(c)[1];
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
    @Override
    //点击有棋子的格子
    public void onPlayerClickChessPiece(ChessboardPoint point, ChessComponent component) {

        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                //如果这俩棋子属于当前玩家的，就选中这个棋子
                selectedPoint = point;
                initPoint.add(point);
                component.setSelected(true);
                int r= point.getRow();
                int c= point.getCol();
                for(int i=0;i<9;i++){
                    for (int j = 0; j < 7; j++) {
                        ChessboardPoint point1=new ChessboardPoint(i,j);
                        cellComponent[i][j]=view.getGridComponentAt(point1);
                        if(model.isValidMove(selectedPoint,point1)){
                            cellComponent[i][j].setIsValidMoveTo(true);
                            cellComponent[i][j].repaint();
                        }
                        if(model.getChessPieceAt(point1)!=null){
                            if(!model.isValidCapture(point,point1)){
                                cellComponent[i][j].setIsValidMoveTo(false);
                                cellComponent[i][j].repaint();
                            }
                            else{
                                cellComponent[i][j].setIsValidMoveTo(true);
                                cellComponent[i][j].repaint();
                            }
                        }
                    }

                }
                component.repaint();
            }
            else{new notYourTurn();}
        } else if (selectedPoint.equals(point)) {
            for(int x=0;x<9;x++){
                for (int y = 0; y < 7; y++) {
                    cellComponent[x][y].setIsValidMoveTo(false);
                    cellComponent[x][y].repaint();
                }
            }
            selectedPoint = null;
            ArrayList<ChessboardPoint>cbp=new ArrayList<>();
            int j=0;
            for (ChessboardPoint p:initPoint){
                if (j!=initPoint.size()-1) cbp.add(p);
                j++;
            }
            initPoint=cbp;
            component.setSelected(false);
            component.repaint();
        }else if(model.isValidCapture(selectedPoint,point) ){
            //如果这棋子不属于当前玩家，就吃掉这个棋子
            initPiece.add(model.getChessPieceAt(point));
            eatenPoint.add(point);
            eatsTurn.add(turn+1);
            eats++;
            nowPoint.add(point);
            view.removeChessComponentAtGrid(point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            model.captureChessPiece(selectedPoint, point);
            selectedPoint = null;
            view.repaint();
            for(int x=0;x<9;x++){
                for (int y = 0; y < 7; y++) {
                    cellComponent[x][y].setIsValidMoveTo(false);
                    cellComponent[x][y].repaint();
                }
            }
            if(mode == 0){
                swapColor();
            }
            if(mode ==1){
                makeAIMove();
            }
            if(mode ==2){
                SmartAI();
            }
            view.repaint();
            win();
        }
    }

    public static int getTurn() {
        return turn;
    }

    public void setView(ChessboardComponent chessboardComponent) {
        view = chessboardComponent;
    }
}
