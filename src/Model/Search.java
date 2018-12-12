package Model;

public class Search {

    public Player maximizePlayer = Player.BLACK;
    public int globalDepth;
    public String bestMoveFound;
    private Opening opening = new Opening();
    public Search(String[] moves) {
        opening.moves=moves;
    }

    public int alphaBeta(int alpha, int beta, long WP, long WN, long WB, long WR, long WQ, long WK,
                                   long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                                   CastlingRights castlingRights, Player currentPlayer, int depth) {

        if(depth == 0) {
            System.out.println(currentPlayer);
            return Evaluation.getValue(currentPlayer, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        }

        int bestScore = -1000;

        String moves = MoveGenerator.generatePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant, currentPlayer);

        for (int i = 0; i < moves.length(); i += 4) {
            String currentMove = moves.substring(i, i+4);
            long newWP = Move.makeMove(WP, currentMove, 'P');
            long newWN = Move.makeMove(WN, currentMove, 'N');
            long newWB = Move.makeMove(WB, currentMove, 'B');
            long newWR = Move.makeMove(WR, currentMove, 'R');
            long newWQ = Move.makeMove(WQ, currentMove, 'Q');
            long newWK = Move.makeMove(WK, currentMove, 'K');
            long newBP = Move.makeMove(BP, currentMove, 'p');
            long newBN = Move.makeMove(BN, currentMove, 'n');
            long newBB = Move.makeMove(BB, currentMove, 'b');
            long newBR = Move.makeMove(BR, currentMove, 'r');
            long newBQ = Move.makeMove(BQ, currentMove, 'q');
            long newBK = Move.makeMove(BK, currentMove, 'k');
            //long newEnPassant = Move.makeMove(WP|BP, currentMove)
            newWR = Move.makeCastle(newWR, WK|BK, currentMove);
            newBR = Move.makeCastle(newBR, WK|BK, currentMove);
            CastlingRights currentCastlingRights = new CastlingRights(castlingRights);
            if(Character.isDigit(currentMove.charAt(3))) {
                int departure = (Character.getNumericValue(currentMove.charAt(0)) * 8) + (Character.getNumericValue(currentMove.charAt(1)));
                if(((1L << departure) & WK) != 0) {
                    currentCastlingRights.shortWhiteCastle = false;
                    currentCastlingRights.longWhiteCastle = false;
                }
                else if (((1L << departure) & BK) != 0) {
                    currentCastlingRights.shortBlackCastle = false;
                    currentCastlingRights.longBlackCastle = false;
                }
                else if(((1L << departure) & WR & (1L << 63)) != 0)
                    currentCastlingRights.shortWhiteCastle = false;
                else if(((1L << departure) & WR & (1L << 56)) != 0)
                    currentCastlingRights.longWhiteCastle = false;
                else if(((1L << departure) & BR & (1L << 7)) != 0)
                    currentCastlingRights.shortBlackCastle = false;
                else if(((1L << departure) & BR & 1L) != 0)
                    currentCastlingRights.longBlackCastle = false;

            }
            Player p = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
            int moveValue = -alphaBeta(-beta, -alpha, newWP, newWN, newWB, newWR, newWQ, newWK,
            newBP, newBN, newBB, newBR, newBQ, newBK, enPassant,
            currentCastlingRights, p, depth-1);

            if(moveValue > bestScore) {
                bestScore = moveValue;
                if(depth == 6)
                    bestMoveFound = currentMove;
            }
            alpha = Integer.max(alpha, bestScore);

            if(alpha >= beta)
                break;
        }
        return alpha;
    }

    public String alphaBeta2(int alpha, int beta, long WP, long WN, long WB, long WR, long WQ, long WK,
                          long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                          CastlingRights castlingRights, Player currentPlayer, int depth) {

        int bestScore = Integer.MIN_VALUE;
        String bestMove = "";
        String moves = MoveGenerator.generatePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant, currentPlayer);

        for (int i = 0; i < moves.length(); i += 4) {
            String currentMove = moves.substring(i, i + 4);
            long newWP = Move.makeMove(WP, currentMove, 'P');
            long newWN = Move.makeMove(WN, currentMove, 'N');
            long newWB = Move.makeMove(WB, currentMove, 'B');
            long newWR = Move.makeMove(WR, currentMove, 'R');
            long newWQ = Move.makeMove(WQ, currentMove, 'Q');
            long newWK = Move.makeMove(WK, currentMove, 'K');
            long newBP = Move.makeMove(BP, currentMove, 'p');
            long newBN = Move.makeMove(BN, currentMove, 'n');
            long newBB = Move.makeMove(BB, currentMove, 'b');
            long newBR = Move.makeMove(BR, currentMove, 'r');
            long newBQ = Move.makeMove(BQ, currentMove, 'q');
            long newBK = Move.makeMove(BK, currentMove, 'k');
            //long newEnPassant = Move.makeMove(WP|BP, currentMove)
            newWR = Move.makeCastle(newWR, WK | BK, currentMove);
            newBR = Move.makeCastle(newBR, WK | BK, currentMove);
            CastlingRights currentCastlingRights = new CastlingRights(castlingRights);
            if (Character.isDigit(currentMove.charAt(3))) {
                int departure = (Character.getNumericValue(currentMove.charAt(0)) * 8) + (Character.getNumericValue(currentMove.charAt(1)));
                if (((1L << departure) & WK) != 0) {
                    currentCastlingRights.shortWhiteCastle = false;
                    currentCastlingRights.longWhiteCastle = false;
                } else if (((1L << departure) & BK) != 0) {
                    currentCastlingRights.shortBlackCastle = false;
                    currentCastlingRights.longBlackCastle = false;
                } else if (((1L << departure) & WR & (1L << 63)) != 0)
                    currentCastlingRights.shortWhiteCastle = false;
                else if (((1L << departure) & WR & (1L << 56)) != 0)
                    currentCastlingRights.longWhiteCastle = false;
                else if (((1L << departure) & BR & (1L << 7)) != 0)
                    currentCastlingRights.shortBlackCastle = false;
                else if (((1L << departure) & BR & 1L) != 0)
                    currentCastlingRights.longBlackCastle = false;

            }

            Player p = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;


                int moveValue = minAB(alpha, beta, newWP, newWN, newWB, newWR, newWQ, newWK,
                        newBP, newBN, newBB, newBR, newBQ, newBK, enPassant,
                        currentCastlingRights, p, depth-1);
                if(moveValue > bestScore) {
                    System.out.println(moveValue + " > " + bestScore);
                    bestScore = moveValue;
                    bestMove = currentMove;
                    System.out.println("New bestmove : " + bestMove);
                }


        }
        return bestMove;
    }

    private int minAB(int alpha, int beta, long WP, long WN, long WB, long WR, long WQ, long WK,
                      long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                      CastlingRights castlingRights, Player currentPlayer, int depth) {
        Player opponent = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        if(depth == 0) {
            return Evaluation.getValue(opponent, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        }
        int bestScore = Integer.MAX_VALUE;
        String moves = MoveGenerator.generatePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant, currentPlayer);

        for (int i = 0; i < moves.length(); i += 4) {
            String currentMove = moves.substring(i, i + 4);
            long newWP = Move.makeMove(WP, currentMove, 'P');
            long newWN = Move.makeMove(WN, currentMove, 'N');
            long newWB = Move.makeMove(WB, currentMove, 'B');
            long newWR = Move.makeMove(WR, currentMove, 'R');
            long newWQ = Move.makeMove(WQ, currentMove, 'Q');
            long newWK = Move.makeMove(WK, currentMove, 'K');
            long newBP = Move.makeMove(BP, currentMove, 'p');
            long newBN = Move.makeMove(BN, currentMove, 'n');
            long newBB = Move.makeMove(BB, currentMove, 'b');
            long newBR = Move.makeMove(BR, currentMove, 'r');
            long newBQ = Move.makeMove(BQ, currentMove, 'q');
            long newBK = Move.makeMove(BK, currentMove, 'k');
            //long newEnPassant = Move.makeMove(WP|BP, currentMove)
            newWR = Move.makeCastle(newWR, WK | BK, currentMove);
            newBR = Move.makeCastle(newBR, WK | BK, currentMove);
            CastlingRights currentCastlingRights = new CastlingRights(castlingRights);
            if (Character.isDigit(currentMove.charAt(3))) {
                int departure = (Character.getNumericValue(currentMove.charAt(0)) * 8) + (Character.getNumericValue(currentMove.charAt(1)));
                if (((1L << departure) & WK) != 0) {
                    currentCastlingRights.shortWhiteCastle = false;
                    currentCastlingRights.longWhiteCastle = false;
                } else if (((1L << departure) & BK) != 0) {
                    currentCastlingRights.shortBlackCastle = false;
                    currentCastlingRights.longBlackCastle = false;
                } else if (((1L << departure) & WR & (1L << 63)) != 0)
                    currentCastlingRights.shortWhiteCastle = false;
                else if (((1L << departure) & WR & (1L << 56)) != 0)
                    currentCastlingRights.longWhiteCastle = false;
                else if (((1L << departure) & BR & (1L << 7)) != 0)
                    currentCastlingRights.shortBlackCastle = false;
                else if (((1L << departure) & BR & 1L) != 0)
                    currentCastlingRights.longBlackCastle = false;

            }
            if(((newWK & MoveGenerator.generateUnsafeSquares(Player.WHITE, newBP, newBN, newBB, newBR, newBQ, newBK, newWP, newWN, newWB, newWR, newWQ, newWK)) == 0 && currentPlayer == Player.WHITE)
                    || ((newBK & MoveGenerator.generateUnsafeSquares(Player.BLACK, newWP, newWN, newWB, newWR, newWQ, newWK, newBP, newBN, newBB, newBR, newBQ, newBK)) == 0 && currentPlayer == Player.BLACK)) {
                bestScore = Integer.min(bestScore, maxAB(alpha, beta, newWP, newWN, newWB, newWR, newWQ, newWK,
                        newBP, newBN, newBB, newBR, newBQ, newBK, enPassant,
                        currentCastlingRights, opponent, depth-1));
            }

            if(bestScore <= alpha)
                return bestScore;

            beta = Integer.min(beta, bestScore);
        }
        return bestScore;
    }

    private int maxAB(int alpha, int beta, long WP, long WN, long WB, long WR, long WQ, long WK,
                      long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                      CastlingRights castlingRights, Player currentPlayer, int depth) {

        Player opponent = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        if(depth == 0) {
            return Evaluation.getValue(currentPlayer, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        }
        int bestScore = Integer.MIN_VALUE;
        String moves = MoveGenerator.generatePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant, currentPlayer);

        for (int i = 0; i < moves.length(); i += 4) {
            String currentMove = moves.substring(i, i + 4);
            long newWP = Move.makeMove(WP, currentMove, 'P');
            long newWN = Move.makeMove(WN, currentMove, 'N');
            long newWB = Move.makeMove(WB, currentMove, 'B');
            long newWR = Move.makeMove(WR, currentMove, 'R');
            long newWQ = Move.makeMove(WQ, currentMove, 'Q');
            long newWK = Move.makeMove(WK, currentMove, 'K');
            long newBP = Move.makeMove(BP, currentMove, 'p');
            long newBN = Move.makeMove(BN, currentMove, 'n');
            long newBB = Move.makeMove(BB, currentMove, 'b');
            long newBR = Move.makeMove(BR, currentMove, 'r');
            long newBQ = Move.makeMove(BQ, currentMove, 'q');
            long newBK = Move.makeMove(BK, currentMove, 'k');
            //long newEnPassant = Move.makeMove(WP|BP, currentMove)
            newWR = Move.makeCastle(newWR, WK | BK, currentMove);
            newBR = Move.makeCastle(newBR, WK | BK, currentMove);
            CastlingRights currentCastlingRights = new CastlingRights(castlingRights);
            if (Character.isDigit(currentMove.charAt(3))) {
                int departure = (Character.getNumericValue(currentMove.charAt(0)) * 8) + (Character.getNumericValue(currentMove.charAt(1)));
                if (((1L << departure) & WK) != 0) {
                    currentCastlingRights.shortWhiteCastle = false;
                    currentCastlingRights.longWhiteCastle = false;
                } else if (((1L << departure) & BK) != 0) {
                    currentCastlingRights.shortBlackCastle = false;
                    currentCastlingRights.longBlackCastle = false;
                } else if (((1L << departure) & WR & (1L << 63)) != 0)
                    currentCastlingRights.shortWhiteCastle = false;
                else if (((1L << departure) & WR & (1L << 56)) != 0)
                    currentCastlingRights.longWhiteCastle = false;
                else if (((1L << departure) & BR & (1L << 7)) != 0)
                    currentCastlingRights.shortBlackCastle = false;
                else if (((1L << departure) & BR & 1L) != 0)
                    currentCastlingRights.longBlackCastle = false;

            }
            if(((newWK & MoveGenerator.generateUnsafeSquares(Player.WHITE, newBP, newBN, newBB, newBR, newBQ, newBK, newWP, newWN, newWB, newWR, newWQ, newWK)) == 0 && currentPlayer == Player.WHITE)
                    || ((newBK & MoveGenerator.generateUnsafeSquares(Player.BLACK, newWP, newWN, newWB, newWR, newWQ, newWK, newBP, newBN, newBB, newBR, newBQ, newBK)) == 0 && currentPlayer == Player.BLACK)) {
                bestScore = Integer.max(bestScore, minAB(alpha, beta, newWP, newWN, newWB, newWR, newWQ, newWK,
                        newBP, newBN, newBB, newBR, newBQ, newBK, enPassant,
                        currentCastlingRights, opponent, depth-1));
            }

            if(bestScore >= beta)
                return bestScore;

            alpha = Integer.max(alpha, bestScore);
        }
        return bestScore;
    }

    public String bestMove;
    private String globalBestMove;
    public int currentDepth;
    private long start;
    private boolean timeOut;
    private static final int INITIAL_DEPTH = 4;
    private static int SEARCH_TIME;

    /* ALPHA BETA + ITERATIVE DEEPENING SEARCH */
    public String deepeningSearch(int searchTime, int maxDepth, long WP, long WN, long WB, long WR, long WQ, long WK,
                                     long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                                     CastlingRights castlingRights, Player currentPlayer) {
        SEARCH_TIME = searchTime;
        timeOut = false;
        start = System.currentTimeMillis();
        String move=null;

        if(opening.begining) {
            move = opening.isRevelant();
        }
        if (opening.begining && move != null) {
            System.out.println("Opening");
            return move;
        }
        else {
            opening.begining=false;
            for (int d = 0; ; d++) {
                if (d > 0) {
                    globalBestMove = bestMove;
                    System.out.println("Search depth " + currentDepth + " OK. Best move = " + globalBestMove);
                }

                currentDepth = INITIAL_DEPTH + d;
                alphaBetaMAX(currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, enPassant, castlingRights, currentPlayer);

                if (timeOut)
                    return globalBestMove;
            }
        }
    }

    private int alphaBetaMAX(int depth, int alpha, int beta, long WP, long WN, long WB, long WR, long WQ, long WK,
                            long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                            CastlingRights castlingRights, Player currentPlayer) {

        if(System.currentTimeMillis() - start > SEARCH_TIME) {
            timeOut = true;
            return alpha;
        }

        if(depth == 0)
            return Evaluation.getValue(currentPlayer, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);

        String moves = MoveGenerator.generatePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant, currentPlayer);

        for (int i = 0; i < moves.length(); i += 4) {
            String currentMove = moves.substring(i, i + 4);
            long newWP = Move.makeMove(WP, currentMove, 'P');
            long newWN = Move.makeMove(WN, currentMove, 'N');
            long newWB = Move.makeMove(WB, currentMove, 'B');
            long newWR = Move.makeMove(WR, currentMove, 'R');
            long newWQ = Move.makeMove(WQ, currentMove, 'Q');
            long newWK = Move.makeMove(WK, currentMove, 'K');
            long newBP = Move.makeMove(BP, currentMove, 'p');
            long newBN = Move.makeMove(BN, currentMove, 'n');
            long newBB = Move.makeMove(BB, currentMove, 'b');
            long newBR = Move.makeMove(BR, currentMove, 'r');
            long newBQ = Move.makeMove(BQ, currentMove, 'q');
            long newBK = Move.makeMove(BK, currentMove, 'k');
            //long newEnPassant = Move.makeMove(WP|BP, currentMove)
            newWR = Move.makeCastle(newWR, WK | BK, currentMove);
            newBR = Move.makeCastle(newBR, WK | BK, currentMove);
            CastlingRights currentCastlingRights = new CastlingRights(castlingRights);
            if (Character.isDigit(currentMove.charAt(3))) {
                int departure = (Character.getNumericValue(currentMove.charAt(0)) * 8) + (Character.getNumericValue(currentMove.charAt(1)));
                if (((1L << departure) & WK) != 0) {
                    currentCastlingRights.shortWhiteCastle = false;
                    currentCastlingRights.longWhiteCastle = false;
                } else if (((1L << departure) & BK) != 0) {
                    currentCastlingRights.shortBlackCastle = false;
                    currentCastlingRights.longBlackCastle = false;
                } else if (((1L << departure) & WR & (1L << 63)) != 0)
                    currentCastlingRights.shortWhiteCastle = false;
                else if (((1L << departure) & WR & (1L << 56)) != 0)
                    currentCastlingRights.longWhiteCastle = false;
                else if (((1L << departure) & BR & (1L << 7)) != 0)
                    currentCastlingRights.shortBlackCastle = false;
                else if (((1L << departure) & BR & 1L) != 0)
                    currentCastlingRights.longBlackCastle = false;

            }
            Player opponent = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
            if(((newWK & MoveGenerator.generateUnsafeSquares(Player.WHITE, newBP, newBN, newBB, newBR, newBQ, newBK, newWP, newWN, newWB, newWR, newWQ, newWK)) == 0 && currentPlayer == Player.WHITE)
                    || ((newBK & MoveGenerator.generateUnsafeSquares(Player.BLACK, newWP, newWN, newWB, newWR, newWQ, newWK, newBP, newBN, newBB, newBR, newBQ, newBK)) == 0 && currentPlayer == Player.BLACK)) {
                int rating = alphaBetaMIN(depth - 1, alpha, beta, newWP, newWN, newWB, newWR, newWQ, newWK,
                        newBP, newBN, newBB, newBR, newBQ, newBK, enPassant,
                        currentCastlingRights, opponent);

                if(rating > alpha) {
                    alpha = rating;
                    if(depth == currentDepth)
                        bestMove = currentMove;
                }
                if(alpha >= beta)
                    return alpha;
            }
        } // for each moves
        return alpha;
    }

    private int alphaBetaMIN(int depth, int alpha, int beta, long WP, long WN, long WB, long WR, long WQ, long WK,
                             long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                             CastlingRights castlingRights, Player currentPlayer) {

        Player opponent = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        if(depth == 0)
            return Evaluation.getValue(opponent, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);

        String moves = MoveGenerator.generatePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant, currentPlayer);

        for (int i = 0; i < moves.length(); i += 4) {
            String currentMove = moves.substring(i, i + 4);
            long newWP = Move.makeMove(WP, currentMove, 'P');
            long newWN = Move.makeMove(WN, currentMove, 'N');
            long newWB = Move.makeMove(WB, currentMove, 'B');
            long newWR = Move.makeMove(WR, currentMove, 'R');
            long newWQ = Move.makeMove(WQ, currentMove, 'Q');
            long newWK = Move.makeMove(WK, currentMove, 'K');
            long newBP = Move.makeMove(BP, currentMove, 'p');
            long newBN = Move.makeMove(BN, currentMove, 'n');
            long newBB = Move.makeMove(BB, currentMove, 'b');
            long newBR = Move.makeMove(BR, currentMove, 'r');
            long newBQ = Move.makeMove(BQ, currentMove, 'q');
            long newBK = Move.makeMove(BK, currentMove, 'k');
            //long newEnPassant = Move.makeMove(WP|BP, currentMove)
            newWR = Move.makeCastle(newWR, WK | BK, currentMove);
            newBR = Move.makeCastle(newBR, WK | BK, currentMove);
            CastlingRights currentCastlingRights = new CastlingRights(castlingRights);
            if (Character.isDigit(currentMove.charAt(3))) {
                int departure = (Character.getNumericValue(currentMove.charAt(0)) * 8) + (Character.getNumericValue(currentMove.charAt(1)));
                if (((1L << departure) & WK) != 0) {
                    currentCastlingRights.shortWhiteCastle = false;
                    currentCastlingRights.longWhiteCastle = false;
                } else if (((1L << departure) & BK) != 0) {
                    currentCastlingRights.shortBlackCastle = false;
                    currentCastlingRights.longBlackCastle = false;
                } else if (((1L << departure) & WR & (1L << 63)) != 0)
                    currentCastlingRights.shortWhiteCastle = false;
                else if (((1L << departure) & WR & (1L << 56)) != 0)
                    currentCastlingRights.longWhiteCastle = false;
                else if (((1L << departure) & BR & (1L << 7)) != 0)
                    currentCastlingRights.shortBlackCastle = false;
                else if (((1L << departure) & BR & 1L) != 0)
                    currentCastlingRights.longBlackCastle = false;

            }

            if(((newWK & MoveGenerator.generateUnsafeSquares(Player.WHITE, newBP, newBN, newBB, newBR, newBQ, newBK, newWP, newWN, newWB, newWR, newWQ, newWK)) == 0 && currentPlayer == Player.WHITE)
                    || ((newBK & MoveGenerator.generateUnsafeSquares(Player.BLACK, newWP, newWN, newWB, newWR, newWQ, newWK, newBP, newBN, newBB, newBR, newBQ, newBK)) == 0 && currentPlayer == Player.BLACK)) {
                int rating = alphaBetaMAX(depth - 1, alpha, beta, newWP, newWN, newWB, newWR, newWQ, newWK,
                        newBP, newBN, newBB, newBR, newBQ, newBK, enPassant,
                        currentCastlingRights, opponent);

                if(rating <= beta)
                    beta = rating;

                if(alpha >= beta)
                    return beta;
            }
        } // for each moves
        return beta;
    }


}
