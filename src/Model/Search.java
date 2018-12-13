package Model;

public class Search {

    public String bestMove;
    private String globalBestMove;
    public int currentDepth;
    private long start;
    private boolean timeOut;
    private static final int INITIAL_DEPTH = 4;
    private static int SEARCH_TIME;
    private Hashtable transpositionTable;
    public int boardEvalSkipped;

    public Search() {
        transpositionTable = new Hashtable();
        Zobrist.initRandomArray();
    }

    /* ALPHA BETA + ITERATIVE DEEPENING SEARCH */
    public String deepeningSearch(int searchTime, int maxDepth, long WP, long WN, long WB, long WR, long WQ, long WK,
                                     long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                                     CastlingRights castlingRights, Player currentPlayer) {
        SEARCH_TIME = searchTime;
        timeOut = false;
        start = System.currentTimeMillis();
        boardEvalSkipped = 0;
        for(int d = 0;; d++) {
            if(d > 0) {
                globalBestMove = bestMove;
                System.out.println("Search depth " + currentDepth + " OK. Best move = " + globalBestMove);
            }
                currentDepth = INITIAL_DEPTH + d;
                alphaBetaMAX(currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, enPassant, castlingRights, currentPlayer);

            if(timeOut)
                return globalBestMove;
        }
    }

    public int alphaBetaMAX(int depth, int alpha, int beta, long WP, long WN, long WB, long WR, long WQ, long WK,
                            long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                            CastlingRights castlingRights, Player currentPlayer) {

        int originalAlpha = alpha;
        if(System.currentTimeMillis() - start > SEARCH_TIME) {
            timeOut = true;
            return alpha;
        }
        long hash = Zobrist.getHash(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, enPassant, castlingRights, currentPlayer);
        if(transpositionTable.isExist(hash)) { // position deja calculee, on recupere son resultat
            boardEvalSkipped++;
            TableItem item = transpositionTable.get(hash);
            if(item.depth >= currentDepth) {
                if(item.valueType == ValueType.Exact) {
                    bestMove = item.bestMove;
                    return item.value;
                }
                else if(item.valueType == ValueType.Lower) {
                    if(item.value > alpha)
                        alpha = item.value;
                }
                else if(item.valueType == ValueType.Upper){
                    if(item.value < beta)
                        beta = item.value;
                }
                if(alpha >= beta) {
                    bestMove = item.bestMove;
                    return item.value;
                }
            }
        }

        if(depth == 0)
            return Evaluation.getValue(currentPlayer, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);

        String moves = MoveGenerator.generatePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant, currentPlayer);

        // On regarde s'il existe un mouvement legal dans la liste generee
        int legalMove = getOneLegalMove(moves, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, enPassant, castlingRights, currentPlayer);
        if(legalMove == -1)
            return Evaluation.MATE_VALUE;

        for (int i = legalMove; i < moves.length(); i += 4) {
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

                /*if(Math.abs(rating) == Evaluation.MATE_VALUE) {
                    bestMove = currentMove;
                    return rating;
                }*/

                if(rating > alpha) {
                    alpha = rating;
                    if(depth == currentDepth) {
                        bestMove = currentMove;
                    }
                }
                if(alpha >= beta)
                    break;
                    //return alpha;
            }
        } // for each moves
        /*if(bestMove != null) {
            TableItem item = new TableItem(alpha, currentDepth, bestMove, ValueType.None);
            if (alpha <= originalAlpha)
                item.valueType = ValueType.Upper;
            else if (alpha >= beta)
                item.valueType = ValueType.Lower;
            else
                item.valueType = ValueType.Exact;
            transpositionTable.add(hash, item);
        }*/
        return alpha;
    }

    private int alphaBetaMIN(int depth, int alpha, int beta, long WP, long WN, long WB, long WR, long WQ, long WK,
                             long BP, long BN, long BB, long BR, long BQ, long BK, long enPassant,
                             CastlingRights castlingRights, Player currentPlayer) {

        long hash = Zobrist.getHash(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, enPassant, castlingRights, currentPlayer);
        if(transpositionTable.isExist(hash)) { // position deja calculee, on recupere son resultat
            boardEvalSkipped++;
            TableItem item = transpositionTable.get(hash);
            if(item.depth >= currentDepth) {
                if(item.valueType == ValueType.Exact) {
                    bestMove = item.bestMove;
                    return item.value;
                }
                else if(item.valueType == ValueType.Lower) {
                    if(item.value > alpha)
                        alpha = item.value;
                }
                else if(item.valueType == ValueType.Upper){
                    if(item.value < beta)
                        beta = item.value;
                }
                if(alpha >= beta) {
                    bestMove = item.bestMove;
                    return item.value;
                }
            }
        }

        Player opponent = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        if(depth == 0)
            return Evaluation.getValue(opponent, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);

        String moves = MoveGenerator.generatePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant, currentPlayer);

        // On regarde s'il existe un mouvement legal dans la liste generee
        int legalMove = getOneLegalMove(moves, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, enPassant, castlingRights, currentPlayer);
        if(legalMove == -1)
            return -Evaluation.MATE_VALUE;

        for (int i = legalMove; i < moves.length(); i += 4) {
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

    /**
     * Prend en parametre la liste de tous les mouvements (legaux et pseudo-legaux)
     * Parcours la liste et selectionne l'indice du premier mouvement legal trouve.
     * S'il n'y en a aucun c'est que la partie est terminee
     * et qu'il y a soit echec et mat, soit draw.
     * @return i (indice du mouvement)
     */
    private int getOneLegalMove(String moves, long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long enPassant, CastlingRights castlingRights, Player currentPlayer) {
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
            if (((newWK & MoveGenerator.generateUnsafeSquares(Player.WHITE, newBP, newBN, newBB, newBR, newBQ, newBK, newWP, newWN, newWB, newWR, newWQ, newWK)) == 0 && currentPlayer == Player.WHITE)
                    || ((newBK & MoveGenerator.generateUnsafeSquares(Player.BLACK, newWP, newWN, newWB, newWR, newWQ, newWK, newBP, newBN, newBB, newBR, newBQ, newBK)) == 0 && currentPlayer == Player.BLACK)) {
                return i; // Un mouvement legal a ete trouve, on retourne l'indice de ce mouvement
            }
        }
        return -1; // Aucun mouvement legal dispo : fin de partie
    }
}
