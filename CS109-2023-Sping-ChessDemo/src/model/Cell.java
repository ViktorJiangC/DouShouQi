package model;

import view.ChessComponent;

import java.io.Serializable;
/**
 * This class describe the slot for Chess in Chessboard
 * */
public class Cell implements Serializable {
    // the position for chess
    private ChessPiece piece;
    private ChessComponent component;
    public ChessPiece getPiece() {
        return piece;
    }
    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }
    public void setChessComponent(ChessComponent component){this.component=component;}
    public void removePiece() {
        this.piece = null;
    }
}