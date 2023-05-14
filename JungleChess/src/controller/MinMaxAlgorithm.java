package controller;
import model.*;

import java.util.ArrayList;
import java.util.List;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

public class MinMaxAlgorithm {
    private static final int MAX_DEPTH = 2; // 最大深度
    private static final int WIN_SCORE = 1000; // 胜利分数
    private static final int LOSE_SCORE = -1000; // 失败分数
    public static Move bestMove; // 最佳走法

    //alpha表示当前最大值，同时也是上一层的最小值
    //beta表示当前最小值，同时也是上一层的最大值
    //isMax表示当前是最大层还是最小层
    //最终收益在alpha和beta之间
    //Minmax搜索树和alpha-beta剪枝的主要方法
    private static final int[][] LTdir = {{4,0},{-4,0},{0,3},{0,-3},{1,0},{-1,0},{0,1},{0,-1}};
    private static int minimax(Chessboard board, boolean isMax, int depth, int alpha, int beta) {
        if (depth == MAX_DEPTH || isGameOver(board)) {
            return evaluate(board);
        }
        int bestScore;
        if (isMax) {
            bestScore = Integer.MIN_VALUE;
            for (ChessboardPoint piece : getRedPieces(board)) {
                // 遍历当前玩家的每个棋子
                int x = piece.getRow();
                int y = piece.getCol();
                if(board.getChessPieceAt(piece).getRank()==6 || board.getChessPieceAt(piece).getRank()==7){
                    for(int[] dir : LTdir) {
                        int dx = dir[0];
                        int dy = dir[1];
                        int newX = x + dx;
                        int newY = y + dy;
                        //防止越界
                        if (newX < 0 || newX >= 9 || newY < 0 || newY >= 7) {
                            continue;
                        }
                        ChessboardPoint newPoint = new ChessboardPoint(newX, newY);
                        if (validMove(board, piece, newPoint)) {
                            ChessPiece capturedPiece = board.getChessPieceAt(newPoint);
                            board.setChessPiece(newPoint, capturedPiece);
                            board.setChessPiece(piece, null);

                            int score = minimax(board, !isMax, depth + 1, alpha, beta);

                            board.setChessPiece(newPoint, capturedPiece);
                            board.setChessPiece(piece, board.getChessPieceAt(newPoint));

                            bestScore = Math.max(bestScore, score);
                            alpha = Math.max(alpha, bestScore);

                            if (bestScore > beta) {
                                // 更新最佳移动
                                bestMove = new Move(piece, newPoint);
                            }

                            if (beta <= alpha) {
                                // 进行剪枝
                                break;
                            }
                        }
                    }
                }else {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) {
                                continue;
                            }
                            int newX = x + dx;
                            int newY = y + dy;
                            //防止越界
                            if (newX < 0 || newX >= CHESSBOARD_ROW_SIZE.getNum() || newY < 0 || newY >= CHESSBOARD_COL_SIZE.getNum()) {
                                continue;
                            }
                            ChessboardPoint newPoint = new ChessboardPoint(newX, newY);
                            if (validMove(board, piece, newPoint)) {
                                ChessPiece capturedPiece = board.getChessPieceAt(newPoint);
                                board.setChessPiece(newPoint, capturedPiece);
                                board.setChessPiece(piece, null);

                                int score = minimax(board, !isMax, depth + 1, alpha, beta);

                                board.setChessPiece(newPoint, capturedPiece);
                                board.setChessPiece(piece, board.getChessPieceAt(newPoint));


                                bestScore = Math.max(bestScore, score);
                                alpha = Math.max(alpha, bestScore);

                                if (bestScore > beta) {
                                    // 更新最佳移动
                                    bestMove = new Move(piece, newPoint);
                                }

                                if (beta <= alpha) {
                                    // 进行alpha剪枝
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (ChessboardPoint piece : getBluePieces(board)) {
                // 遍历当前玩家的每个棋子
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
                            board.setChessPiece(newPoint, capturedPiece);
                            board.setChessPiece(piece, null);

                            int score = minimax(board, !isMax, depth + 1, alpha, beta);

                            board.setChessPiece(newPoint, capturedPiece);
                            board.setChessPiece(piece, board.getChessPieceAt(newPoint));

                            bestScore = Math.min(bestScore, score);
                            beta = Math.min(beta, bestScore);

                            if (bestScore > alpha) {
                                // 更新最佳移动
                                bestMove = new Move(piece, newPoint);
                            }
                            if (bestScore < alpha) {
                                // 进行alpha剪枝
                                break;
                            }
                            if (beta <= alpha) {
                                // 进行beta剪枝
                                break;
                            }
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
                                board.setChessPiece(newPoint, capturedPiece);
                                board.setChessPiece(piece, null);

                                int score = minimax(board, !isMax, depth + 1, alpha, beta);

                                board.setChessPiece(newPoint, capturedPiece);
                                board.setChessPiece(piece, board.getChessPieceAt(newPoint));

                                bestScore = Math.min(bestScore, score);
                                beta = Math.min(beta, bestScore);

                                if (bestScore > alpha) {
                                    // 更新最佳移动
                                    bestMove = new Move(piece, newPoint);
                                }
                                if (bestScore < alpha) {
                                    // 进行alpha剪枝
                                    break;
                                }
                                if (beta <= alpha) {
                                    // 进行beta剪枝
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return bestScore;
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
    public static void AIMove(Chessboard board) {

        minimax(board, true, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        ChessboardPoint bestSource = bestMove.getFrom();
        ChessboardPoint bestDestination = bestMove.getTo();
        // 执行最佳移动
        board.moveChessPiece(bestSource, bestDestination);
    }

    // 判断游戏是否结束
    private static boolean isGameOver(Chessboard board) {
        // 根据斗兽棋的规则判断游戏是否结束
        // 实现根据具体规则的游戏结束判断逻辑
        ChessboardPoint redDens = new ChessboardPoint(0, 3);
        ChessboardPoint blueDens = new ChessboardPoint(8, 3);
        ChessPiece redDensPiece = board.getChessPieceAt(redDens);
        ChessPiece blueDensPiece = board.getChessPieceAt(blueDens);
        if (redDensPiece != null && redDensPiece.getOwner() == PlayerColor.BLUE) {
            return true;
        }
        return blueDensPiece != null && blueDensPiece.getOwner() == PlayerColor.RED;
    }

    // 评估当前棋盘状态的分数
    private static final int[][] redTable = {
            {10,10,12,0,12,10,10},
            {11,13,12,13,12,13,11},
            {11,12,13,14,13,12,11},
            {14,13,13,15,13,13,14},
            {15,14,14,15,14,14,15},
            {16,15,15,17,15,15,16},
            {17,17,18,22,18,17,17},
            {17,18,22,25,22,18,17},
            {18,21,25,99,25,21,18}
    };
    private static final int[][] blueTable ={
            {18,21,25,99,25,21,18},
            {17,18,22,25,22,18,17},
            {17,17,18,22,18,17,17},
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
            score -= 0.8*rank*blueTable[P.getRow()][P.getCol()];
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

    // 判断移动是否合法
    private static boolean validMove(Chessboard board, ChessboardPoint src, ChessboardPoint dest) {
        //能动和能吃一起判断
        ChessPiece srcPiece = board.getChessPieceAt(src);
        ChessPiece destPiece = board.getChessPieceAt(dest);
        if (srcPiece != null & destPiece != null) {
            return board.isValidCapture(src,dest); //能吃
        }
        if (srcPiece != null & destPiece == null) {
            return board.isValidMove(src,dest); //能动
        }
        return false;
    }
}