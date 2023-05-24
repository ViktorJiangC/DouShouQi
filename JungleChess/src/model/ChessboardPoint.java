package model;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class represents positions on the checkerboard, such as (0, 0), (0, 7), and so on
 * Where, the upper left corner is (0, 0), the lower left corner is (7, 0), the upper right corner is (0, 7), and the lower right corner is (7, 7).
 */
public record ChessboardPoint(int row, int col) implements Serializable {


    @Serial
    private static final long serialVersionUID = 6573769168152474927L;

    @Override
    @SuppressWarnings("ALL")
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        ChessboardPoint temp = (ChessboardPoint) obj;
        return (temp.row() == this.row) && (temp.col() == this.col);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ") " + "on the chessboard is clicked!";
    }
}
