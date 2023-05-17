package controller;

public class Extra {
     /*else  {
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

                                    if (score < bestScore) {
                                        bestScore = score;
                                    }

                                    board.setChessPiece(newPoint, capturedPiece);
                                    board.setChessPiece(piece, board.getChessPieceAt(newPoint));

                                    beta = Math.min(beta, bestScore);

                                    if (beta <= alpha) {
                                        // 进行剪枝
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }*/

     /*if(board.getChessPieceAt(piece).getRank() != 6 || board.getChessPieceAt(piece).getRank() != 7) {
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

                                    if (score > bestScore) {
                                        bMove[0] = piece;
                                        bMove[1] = newPoint;
                                        bestScore = score;
                                    }

                                    board.setChessPiece(newPoint, capturedPiece);
                                    board.setChessPiece(piece, board.getChessPieceAt(newPoint));

                                    alpha = Math.max(alpha, bestScore);

                                    if (beta <= alpha) {
                                        // 进行剪枝
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }*/
}
