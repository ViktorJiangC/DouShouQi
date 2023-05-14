package controller;
import model.ChessboardPoint;
public class Move {
    private ChessboardPoint from;
    private ChessboardPoint to;

    public Move(ChessboardPoint from, ChessboardPoint to) {
        this.from = from;
        this.to = to;
    }

    public ChessboardPoint getFrom() {
        return from;
    }

    public ChessboardPoint getTo() {
        return to;
    }
}
