package controller;
import model.*;

import java.util.ArrayList;
import java.util.List;

import static controller.GameController.copyFrom;
import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

public class MinMaxAlgorithm {
    private static final int MAX_DEPTH = 4; // 最大深度
    public static ChessboardPoint[] bMove = new ChessboardPoint[2];// 最佳走法
    public static ChessboardPoint[] prevMove = new ChessboardPoint[2];// 上一步走法

    //alpha表示当前最大值，同时也是上一层的最小值
    //beta表示当前最小值，同时也是上一层的最大值
    //isMax表示当前是最大层还是最小层
    //最终收益在alpha和beta之间
    private static final int[][] LTdir = {{4,0},{-4,0},{0,3},{0,-3},{1,0},{-1,0},{0,1},{0,-1}};
    private static final int[][] simpledir = {{1,0},{-1,0},{0,1},{0,-1}};
    static int minimax(Chessboard board, boolean isMax, int depth, int alpha, int beta) {
        // 如果达到最大深度或者游戏结束，返回当前局面的评估值
        if (depth == MAX_DEPTH|| isGameOver(board)) {
            return evaluate(board);
        }
        int bestScore;
        if (isMax) {
            bestScore = Integer.MIN_VALUE;
            for (ChessboardPoint piece : getRedPieces(board)) {

                int x = piece.getRow();
                int y = piece.getCol();
                if(board.getChessPieceAt(piece).getRank()==6||board.getChessPieceAt(piece).getRank()==7) {
                    for (int[] dir : LTdir) {
                        int dx = dir[0];
                        int dy = dir[1];
                        int newX = x + dx;
                        int newY = y + dy;
                        //防止越界
                        if (newX < 0 || newX >= 9 || newY < 0 || newY >= 7) {
                            continue;
                        }
                        ChessboardPoint newPoint = new ChessboardPoint(newX, newY);
                        if (notRepeatedMove(piece, newPoint) &&validMove(board, piece, newPoint)&& notRepeatedChess(board,piece)) {
                            ChessPiece capturedPiece = board.getChessPieceAt(newPoint);

                            Chessboard board1 = copyFrom(board);
                            board1.setChessPiece(newPoint, capturedPiece);
                            board1.setChessPiece(piece, null);

                            int score = minimax(board1, false, depth + 1, alpha, beta);

                            if (score >= bestScore) {
                                bMove[0] = piece;
                                bMove[1] = newPoint;
                                bestScore = score;
                            }

                            alpha = Math.max(alpha, bestScore);
                            if (alpha <= beta) {
                                return bestScore;  // Alpha剪枝
                            }

                        }
                    }
                }else{
                    for (int[] dir : simpledir) {
                        int dx = dir[0];
                        int dy = dir[1];
                        int newX = x + dx;
                        int newY = y + dy;
                        //防止越界
                        if (newX < 0 || newX >= 9 || newY < 0 || newY >= 7) {
                            continue;
                        }
                        ChessboardPoint newPoint = new ChessboardPoint(newX, newY);
                        if (notRepeatedMove(piece, newPoint) && validMove(board, piece, newPoint) && notRepeatedChess(board,piece)) {
                            ChessPiece capturedPiece = board.getChessPieceAt(newPoint);

                            Chessboard board1 = copyFrom(board);
                            board1.setChessPiece(newPoint, capturedPiece);
                            board1.setChessPiece(piece, null);

                            int score = minimax(board1, false, depth + 1, alpha, beta);

                            if (score >= bestScore) {
                                bMove[0] = piece;
                                bMove[1] = newPoint;
                                bestScore = score;
                            }

                            alpha = Math.max(alpha, bestScore);
                            if (alpha <= beta) {
                                return bestScore;  // Alpha剪枝
                            }

                        }
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for(ChessboardPoint piece : getBluePieces(board)){

                int x = piece.getRow();
                int y = piece.getCol();
                if(board.getChessPieceAt(piece).getRank()==6|| board.getChessPieceAt(piece).getRank()==7) {
                    for (int[] dir : LTdir) {
                        int dx = dir[0];
                        int dy = dir[1];
                        //防止越界
                        if (x + dx < 0 || x + dx >= 9 || y + dy < 0 || y + dy >= 7) {
                            continue;
                        }
                        int newX = x + dx;
                        int newY = y + dy;

                        ChessboardPoint newPoint = new ChessboardPoint(newX, newY);
                        if (validMove(board, piece, newPoint)) {
                            ChessPiece capturedPiece = board.getChessPieceAt(newPoint);

                            Chessboard board1 = copyFrom(board);
                            board1.setChessPiece(newPoint, capturedPiece);
                            board1.setChessPiece(piece, null);

                            int score = minimax(board1, true, depth + 1, alpha, beta);

                            if (score < bestScore) {
                                bestScore = score;
                            }

                            beta = Math.min(beta, bestScore);
                            if (beta >= alpha) {
                                return bestScore;  // Beta剪枝
                            }
                        }
                    }
                }else{
                    for (int[] dir : simpledir) {
                        int dx = dir[0];
                        int dy = dir[1];
                        //防止越界
                        if (x + dx < 0 || x + dx >= 9 || y + dy < 0 || y + dy >= 7) {
                            continue;
                        }
                        int newX = x + dx;
                        int newY = y + dy;
                        ChessboardPoint newPoint = new ChessboardPoint(newX, newY);

                        if (validMove(board, piece, newPoint)) {
                            ChessPiece capturedPiece = board.getChessPieceAt(newPoint);

                            Chessboard board1 = copyFrom(board);
                            board1.setChessPiece(newPoint, capturedPiece);
                            board1.setChessPiece(piece, null);

                            int score = minimax(board1, true, depth + 1, alpha, beta);

                            if (score < bestScore) {
                                bestScore = score;
                            }

                            beta = Math.min(beta, bestScore);

                            if (beta >= alpha) {
                                return bestScore;  // Beta剪枝
                            }
                        }
                    }
                }
            }
        }
        return bestScore;
    }
    private static boolean notRepeatedMove(ChessboardPoint piece, ChessboardPoint newPoint) {
        if(prevMove[0] != null&& prevMove[1] != null) {
            return !(prevMove[1].equals(piece) & prevMove[0].equals(newPoint));
        }
        return true;
    }
    private static boolean notRepeatedChess(Chessboard board ,ChessboardPoint P){
        ChessPiece piece = board.getChessPieceAt(P);
        return piece.getRepeatTurn() <= 1;
    }
    private static boolean isGameOver(Chessboard board) {
        ChessboardPoint redDens = new ChessboardPoint(0, 3);
        ChessboardPoint blueDens = new ChessboardPoint(8, 3);
        ChessPiece redDensPiece = board.getChessPieceAt(redDens);
        ChessPiece blueDensPiece = board.getChessPieceAt(blueDens);
        if (redDensPiece != null && redDensPiece.getOwner() == PlayerColor.BLUE) {
            return true;
        }
        return blueDensPiece != null && blueDensPiece.getOwner() == PlayerColor.RED;
    }

    private static final int[][] redTable = {
            {10,10,12,0,12,10,10},
            {11,13,12,13,12,13,11},
            {11,12,13,14,13,12,11},
            {14,13,13,15,13,13,14},
            {15,14,14,15,14,14,15},
            {16,15,15,17,15,15,16},
            {17,17,18,22,18,17,17},
            {17,18,22,21,22,18,17},
            {18,21,21,20000,21,21,18}
    };
    private static final int[][] blueTable ={
            {18,21,50,200,50,21,18},
            {17,18,22,50,22,18,17},
            {17,17,18,35,18,17,17},
            {16,15,15,17,15,15,16},
            {15,14,14,15,14,14,15},
            {14,13,13,14,13,13,14},
            {11,12,13,14,13,12,11},
            {11,13,12,13,12,13,11},
            {10,10,12,0,12,10,10}
    };
    private static int evaluate(Chessboard board) {
        int score = 0;
        for(ChessboardPoint P : getRedPieces(board)){
            ChessPiece piece = board.getChessPieceAt(P);
            int rank = piece.getRank();
            score += rank*redTable[P.getRow()][P.getCol()];
        }
        for(ChessboardPoint P : getBluePieces(board)){
            ChessPiece piece = board.getChessPieceAt(P);
            int rank = piece.getRank();
            score -= 50*rank*blueTable[P.getRow()][P.getCol()];
        }
        return score;
    }
    public static List<ChessboardPoint> getBluePieces(Chessboard board){
        List<ChessboardPoint> bluePieces = new ArrayList<>();
        for(int i=0;i<CHESSBOARD_ROW_SIZE.getNum();i++){
            for(int j=0;j<CHESSBOARD_COL_SIZE.getNum();j++){
                ChessboardPoint p = new ChessboardPoint(i,j);
                if(board.getChessPieceAt(p)!=null && board.getChessPieceAt(p).getOwner()==PlayerColor.BLUE){
                    bluePieces.add(p);
                }
            }
        }
        return bluePieces;
    }
    public static List<ChessboardPoint> getRedPieces(Chessboard board){
        List<ChessboardPoint> redPieces = new ArrayList<>();
        for(int i=0;i<CHESSBOARD_ROW_SIZE.getNum();i++){
            for(int j=0;j<CHESSBOARD_COL_SIZE.getNum();j++){
                ChessboardPoint p = new ChessboardPoint(i,j);
                if(board.getChessPieceAt(p)!=null && board.getChessPieceAt(p).getOwner()==PlayerColor.RED){
                    redPieces.add(p);
                }
            }
        }
        return redPieces;
    }
    public static ChessboardPoint[] easyMove(Chessboard board){
        ChessboardPoint[] points = new ChessboardPoint[2];
        for (ChessboardPoint piece : getRedPieces(board)) {
            int score;
            int bestScore = Integer.MIN_VALUE;
            int x = piece.getRow();
            int y = piece.getCol();
            if(board.getChessPieceAt(piece).getRank()==6 || board.getChessPieceAt(piece).getRank()==7){
                for (int[] dir : LTdir) {
                    int dx = dir[0];
                    int dy = dir[1];
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    //防止越界
                    if (x + dx < 0 || x + dx >= 9 || y + dy < 0 || y + dy >= 7) {
                        continue;
                    }
                    int newX = x + dx;
                    int newY = y + dy;
                    ChessboardPoint newPoint = new ChessboardPoint(newX, newY);
                    if (validMove(board, piece, newPoint)) {
                        ChessPiece capturedPiece = board.getChessPieceAt(newPoint);
                        board.setChessPiece(newPoint, board.getChessPieceAt(piece));
                        board.setChessPiece(piece, null);

                        score = evaluate(board);
                        if(score>bestScore){
                            bestScore=score;
                            points[0]=piece;
                            points[1]=newPoint;
                        }

                        board.setChessPiece(piece, board.getChessPieceAt(newPoint));
                        board.setChessPiece(newPoint, capturedPiece);
                    }
                }
            }else {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) {
                            continue;
                        }
                        //防止越界
                        if (x + dx < 0 || x + dx >= 9 || y + dy < 0 || y + dy >= 7) {
                            continue;
                        }
                        int newX = x + dx;
                        int newY = y + dy;
                        ChessboardPoint newPoint = new ChessboardPoint(newX, newY);
                        if (validMove(board, piece, newPoint)) {
                            ChessPiece capturedPiece = board.getChessPieceAt(newPoint);
                            board.setChessPiece(newPoint, board.getChessPieceAt(piece));
                            board.setChessPiece(piece, null);

                            score = evaluate(board);
                            if(score>bestScore){
                                bestScore=score;
                                points[0]=piece;
                                points[1]=newPoint;
                            }

                            board.setChessPiece(piece, board.getChessPieceAt(newPoint));
                            board.setChessPiece(newPoint, capturedPiece);
                        }
                    }
                }
            }
        }
        return points;
    }
    // 判断移动是否合法
    private static boolean validMove(Chessboard board, ChessboardPoint src, ChessboardPoint dest) {
        //能动和能吃一起判断
        ChessPiece destPiece = board.getChessPieceAt(dest);
        if (destPiece != null) {
            return board.isValidCapture(src, dest);
        }
        return board.isValidMove(src, dest);
    }
}