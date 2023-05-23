package model;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    private Cell[][] grid;

    public Chessboard() {
        this.grid = new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//19X19
        initGrid();
        initPieces();
    }
    //初始化棋盘
    private void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }
    //初始化棋子
    private void initPieces() {
        grid[6][0].setPiece(new ChessPiece(PlayerColor.BLUE, "象",8));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.BLUE, "狮",7));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.BLUE, "虎",6));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.BLUE, "豹",5));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.BLUE, "狼",4));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.BLUE, "狗",3));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.BLUE, "猫",2));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.BLUE, "鼠",1));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.RED, "鼠",1));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.RED, "猫",2));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.RED, "狗",3));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.RED, "狼",4));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.RED, "豹",5));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.RED, "虎",6));
        grid[0][0].setPiece(new ChessPiece(PlayerColor.RED, "狮",7));
        grid[2][6].setPiece(new ChessPiece(PlayerColor.RED, "象",8));
    }
    public ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }
    private Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }
    private int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }
    public ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }
    public void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }

    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        setChessPiece(dest, removeChessPiece(src));
    }

    public void captureChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }else {
            setChessPiece(dest, removeChessPiece(src));
        }
    }
    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        int srcRank = getChessPieceAt(src).getRank();
        int destRank = getChessPieceAt(dest).getRank();
        boolean notSameOwner = getChessPieceAt(src).getOwner() != getChessPieceAt(dest).getOwner();
        boolean movable = calculateDistance(src, dest) == 1 && !isRiverCell(dest);
        //if a piece is trapped in a trap, it can be captured by any piece
        if(!notSameOwner){
            return false;
        }
        if (getChessPieceAt(src).isTrapped()) {
            return false;
        }
        if (getChessPieceAt(dest).isTrapped() && movable) {
            return true;
        }
        if (srcRank == 1 && destRank == 8 && movable && !isRiverCell(src)) {
            return true;
        }
        if (srcRank == 8 && destRank == 1 ) {
            return false;
        }
        if(srcRank == 6||srcRank == 7){
            boolean needJumpOverRiver = needJumpOverRiver(src, dest);
            if(needJumpOverRiver){
                return canJumpOverRiverToCapture(src, dest);    //判断是否可以跳河吃子
            }else{
                return srcRank >= destRank && movable;
            }
        }
        // rank 高的吃rank低的
        return srcRank >= destRank && notSameOwner && movable;
    }
    public Cell[][] getGrid() {
        return grid;
    }
    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        return getChessPieceAt(point).getOwner();
    }
    public boolean needJumpOverRiver(ChessboardPoint src, ChessboardPoint dest){
        if (src.getCol() == dest.getCol() && src.getRow() > dest.getRow()) {
            for (int i = src.getRow()-1; i >= dest.getRow(); i--) {
                if (isRiverCell(new ChessboardPoint(i, src.getCol()))) {
                    return true;
                }
            }
        }
        if (src.getCol() == dest.getCol() && src.getRow() < dest.getRow()) {
            for (int i = src.getRow()+1; i <= dest.getRow(); i++) {
                if (isRiverCell(new ChessboardPoint(i, src.getCol()))) {
                    return true;
                }
            }
        }
        if (src.getRow() == dest.getRow() && src.getCol() > dest.getCol()) {
            for (int i = src.getCol()-1; i >= dest.getCol(); i--) {
                if (isRiverCell(new ChessboardPoint(src.getRow(), i))) {
                    return true;
                }
            }
        }
        if (src.getRow() == dest.getRow() && src.getCol() < dest.getCol()) {
            for (int i = src.getCol()+1; i <= dest.getCol(); i++) {
                if (isRiverCell(new ChessboardPoint(src.getRow(), i))) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {
        //if src is trapped, return false this turn
        if (getChessPieceAt(src).isTrapped()) {
            return false;
        }
        //RAT can move one step in any direction, AND CAN enter the river.
        if (getChessPieceAt(src).getRank() == 1) {
            return calculateDistance(src, dest) == 1;
        }
        //Lion and Tiger can jump over the river, but cannot jump over other pieces.
        if (getChessPieceAt(src).getRank() == 7 || getChessPieceAt(src).getRank() == 6) {
            boolean needJumpOverRiver = needJumpOverRiver(src, dest);
            if (needJumpOverRiver) {
                return canJumpOverRiver(src, dest);
            }else {
                if (getChessPieceAt(src) == null || getChessPieceAt(dest) != null) {
                    return false;
                }
                return calculateDistance(src, dest) == 1 && !isRiverCell(dest);
            }
        }
        //其他棋子移动的判断
        if (getChessPieceAt(src) == null || getChessPieceAt(dest) != null) {
            return false;
        }
        return calculateDistance(src, dest) == 1 &&  !isRiverCell(dest);
    }
    public boolean canJumpOverRiver(ChessboardPoint src, ChessboardPoint dest) {
        if(isRiverCell(dest)) {
            return false;
        }
        //front
        if (src.getCol() == dest.getCol() && src.getRow() > dest.getRow()) {
            if(calculateDistance(src, dest) != 4){
                return false;
            }
            for (int i = src.getRow()-1; i >= dest.getRow(); i--) {
                ChessboardPoint P = new ChessboardPoint(i, src.getCol());
                if (getGridAt(P).getPiece() != null) {
                    return false;
                }
            }
            return true;
        }
        //back
        if (src.getCol() == dest.getCol() && src.getRow() < dest.getRow()) {
            if(calculateDistance(src, dest) != 4){
                return false;
            }
            for (int i = src.getRow()+1; i <= dest.getRow(); i++) {
                ChessboardPoint P = new ChessboardPoint(i, src.getCol());
                if (getGridAt(P).getPiece() != null) {
                    return false;
                }
            }
            return true;
        }
        //left
        if (src.getRow() == dest.getRow() && src.getCol() > dest.getCol()) {
            if(calculateDistance(src, dest) != 3){
                return false;
            }
            for (int i = src.getCol()-1; i >= dest.getCol(); i--) {
                if (getGridAt(new ChessboardPoint(src.getRow(), i)).getPiece() != null) {
                    return false;
                }
            }
            return true;
        }
        //right
        if (src.getRow() == dest.getRow() && src.getCol() < dest.getCol()) {
            if(calculateDistance(src, dest) != 3){
                return false;
            }
            for (int i = src.getCol()+1; i <= dest.getCol(); i++) {
                if (getGridAt(new ChessboardPoint(src.getRow(), i)).getPiece() != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    public boolean canJumpOverRiverToCapture(ChessboardPoint src, ChessboardPoint dest) {
        //front
        if (src.getCol() == dest.getCol() && src.getRow() > dest.getRow()) {
            if(calculateDistance(src, dest) != 4){
                return false;
            }
            for (int i = src.getRow()-1; i > dest.getRow(); i--) {
                ChessboardPoint P = new ChessboardPoint(i, src.getCol());
                if (getChessPieceAt(P) != null) {
                    return false;
                }
            }
            return getChessPieceAt(dest).getRank() <= getChessPieceAt(src).getRank();
        }
        //back
        if (src.getCol() == dest.getCol() && src.getRow() < dest.getRow()) {
            if(calculateDistance(src, dest) != 4){
                return false;
            }
            for (int i = src.getRow()+1; i < dest.getRow(); i++) {
                ChessboardPoint P = new ChessboardPoint(i, src.getCol());
                if (getChessPieceAt(P) != null) {
                    return false;
                }
            }
            return getChessPieceAt(dest).getRank() <= getChessPieceAt(src).getRank();
        }
        //left
        if (src.getRow() == dest.getRow() && src.getCol() > dest.getCol()) {
            if(calculateDistance(src, dest) != 3){
                return false;
            }
            for (int i = src.getCol()-1; i > dest.getCol(); i--) {
                ChessboardPoint P = new ChessboardPoint(src.getRow(), i);
                if (getChessPieceAt(P) != null) {
                    return false;
                }
            }
            return getChessPieceAt(dest).getRank() <= getChessPieceAt(src).getRank();
        }
        //right
        if (src.getRow() == dest.getRow() && src.getCol() < dest.getCol()) {
            if(calculateDistance(src, dest) != 3){
                return false;
            }
            for (int i = src.getCol()+1; i < dest.getCol(); i++) {
                ChessboardPoint P = new ChessboardPoint(src.getRow(), i);
                if (getChessPieceAt(P) != null) {
                    return false;
                }
            }
            return getChessPieceAt(dest).getRank() <= getChessPieceAt(src).getRank();
        }
        return false;
    }
    public boolean isRiverCell(ChessboardPoint point) {
        return  getGridAt(point).equals(grid[3][1])||
                getGridAt(point).equals(grid[3][2])||
                getGridAt(point).equals(grid[3][4])||
                getGridAt(point).equals(grid[3][5])||
                getGridAt(point).equals(grid[4][1])||
                getGridAt(point).equals(grid[4][2])||
                getGridAt(point).equals(grid[4][4])||
                getGridAt(point).equals(grid[4][5])||
                getGridAt(point).equals(grid[5][1])||
                getGridAt(point).equals(grid[5][2])||
                getGridAt(point).equals(grid[5][4])||
                getGridAt(point).equals(grid[5][5]);
    }
}