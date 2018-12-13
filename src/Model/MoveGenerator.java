package Model;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Permet de generer tous les mouvements possibles pour les pieces
 * d'un joueur.
 * --------------
 * La liste des mouvements est representee par une chaine de caracteres
 * dont la structure est la suivante :
 * Une position (x1y1) = 2 caracteres
 * Un coup (x1y1x2y2) = position de depart (x1y1) + position d'arrivee (x2y2) = 4 caracteres
 */
public class MoveGenerator {

    static long occupied;
    static long empty;
    static long whiteCaptures;
    static long blackCaptures;
    static long whiteCanGo;
    static long blackCanGo;
    static long enPassant;
    static long NOT_MY_PIECES;
    static long unsafeSquares;
    static Player player;

    public MoveGenerator(Player player) {
        //this.player = player;
    }
    public static String generatePossibleMoves(long WK, long WQ, long WR, long WB, long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP, long enPassant, Player player) {
        long AllWhite = WK | WQ | WR | WB | WN | WP;
        long AllBlack = BK | BQ | BR | BB | BN | BP;
        //Utils.drawBitboard(WP);
        occupied = AllWhite | AllBlack;
        empty = ~occupied;
        whiteCanGo = ~AllWhite;
        blackCanGo = ~AllBlack;
        whiteCaptures = AllBlack;
        blackCaptures = AllWhite;

        return player == Player.WHITE ?
                generateWhitePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant)
                :
                generateBlackPossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, enPassant);
    }

    private static String generateWhitePossibleMoves(long WK, long WQ, long WR, long WB, long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP, long enPassant) {
        NOT_MY_PIECES = ~(WP|WN|WB|WR|WQ|WK|BK);
        unsafeSquares = generateUnsafeSquares(player, BP, BN, BB, BR, BQ, BK, WP, WN, WB, WR, WQ, WK);
        String allMoves = "";
        allMoves += generateBishopMoves(WB);
        allMoves += generateRookMoves(WR);
        allMoves += generateKnightMoves(WN);
        allMoves += generateQueenMoves(WQ);
        allMoves += generateKingMoves(WK);
        allMoves += generateWhitePawnMoves(WP, BP, enPassant);
        allMoves += generateWhiteCastleMoves(unsafeSquares, WK, WR, true, true);

        return allMoves;
    }

    private static String generateBlackPossibleMoves(long WK, long WQ, long WR, long WB, long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP, long enPassant) {
        NOT_MY_PIECES = ~(BP|BN|BB|BR|BQ|BK|WK);
        unsafeSquares = generateUnsafeSquares(player, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        String allMoves = "";
        allMoves += generateBishopMoves(BB);
        allMoves += generateRookMoves(BR);
        allMoves += generateKnightMoves(BN);
        allMoves += generateQueenMoves(BQ);
        allMoves += generateKingMoves(BK);
        allMoves += generateBlackPawnMoves(BP, WP, enPassant);
        allMoves += generateBlackCastleMoves(unsafeSquares, BK, BR, true, true);
        return allMoves;
    }

    /**
     * Génère la liste des coups possibles pour les fous de
     * la couleur "player" sous la forme : x1y1x2y3...
     * @param bishops
     * @return
     */
    private static String generateBishopMoves(long bishops) {
        StringBuilder bishopsMovelist = new StringBuilder();
        String moveCoords;
        long currentBishop = bishops &- bishops;
        long possibility, currentMove;
        int bishopsLocations, index;
        while(currentBishop != 0) {
            bishopsLocations = Long.numberOfTrailingZeros(currentBishop);
            possibility = diagonalMoves(bishopsLocations) & NOT_MY_PIECES;
            currentMove = possibility & -possibility;
            while (currentMove != 0) {
                index = Long.numberOfTrailingZeros(currentMove);
                moveCoords = "" + (bishopsLocations/8) + (bishopsLocations%8) + (index/8) + (index%8);
                bishopsMovelist.append(moveCoords);
                possibility &= ~currentMove;
                currentMove = possibility &- possibility;
            }
            bishops &= ~currentBishop;
            currentBishop = bishops & -bishops;
        }
        return bishopsMovelist.toString();
    }

    /**
     * Génère la liste des coups possibles pour les tours
     * @param rooks
     * @return
     */
    private static String generateRookMoves(long rooks) {
        StringBuilder rookMoveList = new StringBuilder();
        String moveCoords;
        long currentRook = rooks &- rooks;
        long possibility, currentMove;
        int rooksLocations, index;
        while(currentRook != 0) {
            rooksLocations = Long.numberOfTrailingZeros(currentRook);
            possibility = verticalMoves(rooksLocations) & NOT_MY_PIECES;
            currentMove = possibility & -possibility;
            while(currentMove != 0) {
                index = Long.numberOfTrailingZeros(currentMove);
                moveCoords = "" + (rooksLocations/8) + (rooksLocations%8) + (index/8) + (index%8);
                rookMoveList.append(moveCoords);
                possibility &= ~currentMove;
                currentMove = possibility &- possibility;
            }
            rooks &= ~currentRook;
            currentRook = rooks & -rooks;
        }
        return rookMoveList.toString();
    }

    private static String generateKnightMoves(long knights) {
        StringBuilder knightMoveList = new StringBuilder();
        String moveCoords;
        long currentKnight = knights & -knights;
        long possibility, currentMove;
        int knightsLocations, index;
        while(currentKnight != 0) {
            knightsLocations = Long.numberOfTrailingZeros(currentKnight);
            possibility = knightsLocations > 18 ?
                    Utils.knightSpan << (knightsLocations - 18)
                    :
                    Utils.knightSpan >> (18 - knightsLocations);
            possibility &= knightsLocations % 8 < 4 ?
                    ~Utils.filesGH & NOT_MY_PIECES
                    :
                    ~Utils.filesAB & NOT_MY_PIECES;
            currentMove = possibility & -possibility;
            while(currentMove != 0) {
                index = Long.numberOfTrailingZeros(currentMove);
                moveCoords = "" + (knightsLocations/8) + (knightsLocations%8) + (index/8) + (index%8);
                knightMoveList.append(moveCoords);
                possibility &= ~currentMove;
                currentMove = possibility &- possibility;
            }
            knights &= ~currentKnight;
            currentKnight = knights & -knights;
        }
        return knightMoveList.toString();
    }

    private static String generateQueenMoves(long queens) {
        StringBuilder queenMoveList = new StringBuilder();
        String moveCoords;
        long currentQueen = queens & -queens;
        long possibility, currentMove;
        int queenLocations, index;
        while(currentQueen != 0) {
            queenLocations = Long.numberOfTrailingZeros(currentQueen);
            possibility = (verticalMoves(queenLocations) | diagonalMoves(queenLocations)) & NOT_MY_PIECES;
            currentMove = possibility & -possibility;
            while(currentMove != 0) {
                index = Long.numberOfTrailingZeros(currentMove);
                moveCoords = "" + (queenLocations/8) + (queenLocations%8) + (index/8) + (index%8);
                queenMoveList.append(moveCoords);
                possibility &= ~currentMove;
                currentMove = possibility &- possibility;
            }
            queens &= ~currentQueen;
            currentQueen = queens & -queens;
        }
        return queenMoveList.toString();
    }

    private static String generateKingMoves(long king) {
        StringBuilder kingMoveList = new StringBuilder();
        String moveCoords;
        long possibility, currentMove;
        int kingLocation, index;
        kingLocation = Long.numberOfTrailingZeros(king);
        possibility = kingLocation > 9 ? Utils.kingSpan << (kingLocation - 9) : Utils.kingSpan >> (9 - kingLocation);
        possibility &= kingLocation % 8 < 4 ? ~Utils.filesGH & NOT_MY_PIECES : ~Utils.filesAB & NOT_MY_PIECES;
        currentMove = possibility & -possibility;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (kingLocation/8) + (kingLocation%8) + (index/8) + (index%8);
            kingMoveList.append(moveCoords);
            possibility &= ~currentMove;
            currentMove = possibility & -possibility;
        }
        return kingMoveList.toString();
    }

    /**
     * https://www.chessprogramming.org/Pawn_Attacks_(Bitboards)
     * @param WP
     * @param BP
     * @param enPassant
     */
    private static String generateWhitePawnMoves(long WP, long BP, long enPassant) {
        StringBuilder whitePawnMoveList = new StringBuilder();
        long pawnMoves, currentMove;
        int index;
        String moveCoords;

        /* Mouvement : avancer d'une case */
        pawnMoves = (WP >> 8) & empty & ~Utils.rank8;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index/8+1) + (index%8) + (index/8) + (index%8);
            whitePawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }
        /* Mouvement : avancer de deux cases (possible si le pion (blanc) est sur le rang 2) */
        pawnMoves = (WP >> 16) & empty & (empty >> 8) & Utils.rank4;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index/8+2) + (index%8) + (index/8) + (index%8);
            whitePawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }
        /* Mouvement : capture a droite */
        pawnMoves = (WP >> 7) & NOT_MY_PIECES & occupied & ~Utils.rank8 & ~Utils.fileA;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index/8+1) + (index%8-1) + (index/8) + (index%8);
            whitePawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }

        /* Mouvement : capture a gauche */
        pawnMoves = (WP >> 9) & NOT_MY_PIECES & occupied & ~Utils.rank8 & ~Utils.fileH;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index/8+1) + (index%8+1) + (index/8) + (index%8);
            whitePawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }
        /**************
         * PROMOTIONS *
         **************/

        /* Mouvement : Promotion de pion reguliere (sans capture) */
        pawnMoves = (WP >> 8) & empty & Utils.rank8;
        currentMove = pawnMoves & -pawnMoves;
        while (currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index % 8) + (index % 8) + "QP"
                    + (index % 8) + (index % 8) + "RP"
                    + (index % 8) + (index % 8) + "BP"
                    + (index % 8) + (index % 8) + "NP";
            whitePawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }
        /* Mouvement : promotion de pion (capture a gauche) */
        pawnMoves = (WP >> 9) & NOT_MY_PIECES & occupied & Utils.rank8 & ~Utils.fileH;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index % 8+1) + (index % 8) + "QP"
                    + (index % 8+1) + (index % 8) + "RP"
                    + (index % 8+1) + (index % 8) + "BP"
                    + (index % 8+1) + (index % 8) + "NP";
            whitePawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }

        /* Mouvement : promotion de pion (capture a droite) */
        pawnMoves = (WP >> 7) & NOT_MY_PIECES & occupied & Utils.rank8 & ~Utils.fileA;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index % 8+1) + (index % 8) + "QP"
                    + (index % 8+1) + (index % 8) + "RP"
                    + (index % 8+1) + (index % 8) + "BP"
                    + (index % 8+1) + (index % 8) + "NP";
            whitePawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }

        /**************
         * EN PASSANT *
         **************/
        // Gauche
        pawnMoves = (WP >> 1) & BP & Utils.rank5 & ~Utils.fileH & enPassant;
        if(pawnMoves != 0) {
            index = Long.numberOfTrailingZeros(pawnMoves);
            moveCoords = "" + (index % 8+1) + (index % 8) + "WE";
            whitePawnMoveList.append(moveCoords);
        }
        // Droite
        pawnMoves = (WP << 1) & BP & Utils.rank5 & ~Utils.fileA & enPassant;
        if(pawnMoves != 0) {
            index = Long.numberOfTrailingZeros(pawnMoves);
            moveCoords = "" + (index % 8-1) + (index % 8) + "WE";
            whitePawnMoveList.append(moveCoords);
        }

        return whitePawnMoveList.toString();
    }

    private static String generateBlackPawnMoves(long BP, long WP, long enPassant) {
        StringBuilder blackPawnMoveList = new StringBuilder();
        long pawnMoves, currentMove;
        int index;
        String moveCoords;

        /* Mouvement : avancer d'une case */
        pawnMoves = (BP << 8) & empty & ~Utils.rank1;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index / 8-1) + (index % 8) + (index / 8) + (index % 8);
            blackPawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }
        /* Mouvement : avancer de deux cases (possible si le pion (noir) est sur le rang 2) */
        pawnMoves = (BP << 16) & empty & (empty << 8) & Utils.rank5;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index/8-2) + (index%8) + (index/8) + (index%8);
            blackPawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }

        /* Mouvement : capture a droite */
        pawnMoves = (BP << 7) & NOT_MY_PIECES & occupied & ~Utils.rank1 & ~Utils.fileH;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index / 8-1) + (index % 8+1) + (index / 8) + (index % 8);
            blackPawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }

        /* Mouvement : capture a gauche */
        pawnMoves = (BP << 9) & NOT_MY_PIECES & occupied & ~Utils.rank1 & ~Utils.fileA;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index / 8-1) + (index % 8-1) + (index / 8) + (index % 8);
            blackPawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }

        /**************
         * PROMOTIONS *
         **************/

        /* Mouvement : Promotion de pion reguliere (sans capture) */
        pawnMoves = (BP << 8) & empty & Utils.rank1;
        currentMove = pawnMoves & -pawnMoves;
        while (currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index % 8) + (index % 8) + "qP"
                    + (index % 8) + (index % 8) + "rP"
                    + (index % 8) + (index % 8) + "bP"
                    + (index % 8) + (index % 8) + "nP";
            blackPawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }

        /* Mouvement : promotion de pion (capture a gauche) */
        pawnMoves = (BP << 9) & NOT_MY_PIECES & occupied & Utils.rank1 & ~Utils.fileA;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index % 8+1) + (index % 8) + "qP"
                    + (index % 8-1) + (index % 8) + "rP"
                    + (index % 8-1) + (index % 8) + "bP"
                    + (index % 8-1) + (index % 8) + "nP";
            blackPawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }

        /* Mouvement : promotion de pion (capture a droite) */
        pawnMoves = (BP << 7) & NOT_MY_PIECES & occupied & Utils.rank1 & ~Utils.fileH;
        currentMove = pawnMoves & -pawnMoves;
        while(currentMove != 0) {
            index = Long.numberOfTrailingZeros(currentMove);
            moveCoords = "" + (index % 8+1) + (index % 8) + "qP"
                    + (index % 8-1) + (index % 8) + "rP"
                    + (index % 8-1) + (index % 8) + "bP"
                    + (index % 8-1) + (index % 8) + "nP";
            blackPawnMoveList.append(moveCoords);
            pawnMoves &= ~currentMove;
            currentMove = pawnMoves & -pawnMoves;
        }
        /**************
         * EN PASSANT *
         **************/
        // Gauche
        pawnMoves = (BP >> 1) & WP & Utils.rank4 & ~Utils.fileH & enPassant;
        if(pawnMoves != 0) {
            index = Long.numberOfTrailingZeros(pawnMoves);
            moveCoords = "" + (index % 8+1) + (index % 8) + "BE";
            blackPawnMoveList.append(moveCoords);
        }
        // Droite
        pawnMoves = (BP << 1) & WP & Utils.rank4 & ~Utils.fileA & enPassant;
        if(pawnMoves != 0) {
            index = Long.numberOfTrailingZeros(pawnMoves);
            moveCoords = "" + (index % 8-1) + (index % 8) + "BE";
            blackPawnMoveList.append(moveCoords);
        }

        return blackPawnMoveList.toString();
    }

    /**
     * On genere les roques
     * @param unsafeSquares
     * @param shortCastle
     * @param longCastle
     * @return
     */
    private static String generateWhiteCastleMoves(long unsafeSquares, long king, long rook, boolean shortCastle, boolean longCastle) {
        StringBuilder castleMoveList = new StringBuilder();
        if((unsafeSquares & king) == 0) {
            if(shortCastle && ((( 1L << Utils.castleRooks[0] & rook)) != 0)) {
                if(((occupied | unsafeSquares) & ((1L << 61) | (1L << 62))) == 0)
                    castleMoveList.append("7476");
            }
            if(longCastle && ((( 1L << Utils.castleRooks[1]) & rook) != 0)) {
                if(((occupied | (unsafeSquares & ~(1L << 57))) & ((1L << 57) | (1L << 58)| (1L << 59))) == 0) {
                    castleMoveList.append("7472");
                }
            }
        }
        return castleMoveList.toString();
    }

    private static String generateBlackCastleMoves(long unsafeSquares, long king, long rook, boolean shortCastle, boolean longCastle) {
        StringBuilder castleMoveList = new StringBuilder();
        if((unsafeSquares & king) == 0) {
            if(shortCastle && ((( 1L << Utils.castleRooks[2] & rook)) != 0)) {
                if(((occupied | unsafeSquares) & ((1L << 5) | (1L << 6))) == 0)
                    castleMoveList.append("0406");
            }
            if(longCastle && ((( 1L << Utils.castleRooks[3]) & rook) != 0)) {
                if(((occupied | (unsafeSquares & ~(1L << 1))) & ((1L << 1) | (1L << 2)| (1L << 3))) == 0) {
                    castleMoveList.append("0402");
                }
            }
        }
        return castleMoveList.toString();
    }

    public static long generateUnsafeSquares(Player player, long oppP, long oppN, long oppB, long oppR, long oppQ, long oppK, long myP, long myN, long myB, long myR, long myQ, long myK) {
        long unsafeSquares; // bitboard pour stocker les positions controlees par l'adversaire
        long opponentPossibleMoves;
        long currentPiece;
        int piecePosition;
        occupied = oppP|oppN|oppB|oppR|oppQ|oppK|myP|myN|myB|myR|myQ|myK;
               // WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK

        if(player == Player.WHITE) {
            unsafeSquares = ((oppP << 7) & ~Utils.fileH); // capture pion a droite
            unsafeSquares |= ((oppP << 9) & ~Utils.fileA); // capture pion a gauche
        }
        else {
            unsafeSquares = ((oppP >>> 7) & ~Utils.fileA); // capture pion a gauche
            unsafeSquares |= ((oppP >>> 9) & ~Utils.fileH); // capture pion a droite
        }

        // Attaques des cavaliers adverses
        currentPiece = oppN & -oppN;
        while(currentPiece != 0) {
            piecePosition = Long.numberOfTrailingZeros(currentPiece);
            opponentPossibleMoves = piecePosition > 18 ?
                    Utils.knightSpan << (piecePosition - 18)
                    :
                    Utils.knightSpan >> (18 - piecePosition);
            opponentPossibleMoves &= piecePosition % 8 < 4 ? ~Utils.filesGH : ~Utils.filesAB;
            unsafeSquares |= opponentPossibleMoves;
            oppN &= ~currentPiece;
            currentPiece = oppN & -oppN;
        }

        // Attaques dans les diagonales (fous et reines)
        long oppQB = oppQ | oppB;
        currentPiece = oppQB & -oppQB;
        while(currentPiece != 0) {
            piecePosition = Long.numberOfTrailingZeros(currentPiece);
            opponentPossibleMoves = diagonalMoves(piecePosition);
            unsafeSquares |= opponentPossibleMoves;
            oppQB &= ~currentPiece;
            currentPiece = oppQB & -oppQB;
        }

        // Attaques dans les lignes (tours et reines)
        long oppQR = oppQ | oppR;
        currentPiece = oppQR & -oppQR;
        while(currentPiece != 0) {
            piecePosition = Long.numberOfTrailingZeros(currentPiece);
            opponentPossibleMoves = verticalMoves(piecePosition);
            unsafeSquares |= opponentPossibleMoves;
            oppQR &= ~currentPiece;
            currentPiece = oppQR & -oppQR;
        }

        // Attaques du roi
        piecePosition = Long.numberOfTrailingZeros(oppK);
        opponentPossibleMoves = piecePosition > 9 ?
                Utils.kingSpan << (piecePosition - 9)
                :
                Utils.kingSpan >> (9 - piecePosition);
        opponentPossibleMoves &= piecePosition % 8 < 4 ? ~Utils.filesGH : ~Utils.filesAB;
        unsafeSquares |= opponentPossibleMoves;

        return unsafeSquares;
    }

    private static long verticalMoves(int s) {
        long binaryS = 1L << s;
        long possibilitiesHorizontal = (occupied - 2 * binaryS) ^ Long.reverse(Long.reverse(occupied) - 2 * Long.reverse(binaryS));
        long possibilitiesVertical = ((occupied&Utils.FILES[s % 8]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(occupied&Utils.FILES[s % 8]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesHorizontal&Utils.RANKS[s / 8]) | (possibilitiesVertical&Utils.FILES[s % 8]);
    }

    private static long diagonalMoves(int s) {
        long binaryS = 1L << s;
        long possibilitiesDiagonal = ((occupied&Utils.diagonalMasks[(s / 8) + (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(occupied&Utils.diagonalMasks[(s / 8) + (s % 8)]) - (2 * Long.reverse(binaryS)));
        long possibilitiesAntiDiagonal = ((occupied&Utils.antiDiagonalMasks[(s / 8) + 7 - (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(occupied&Utils.antiDiagonalMasks[(s / 8) + 7 - (s % 8)]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesDiagonal&Utils.diagonalMasks[(s / 8) + (s % 8)]) | (possibilitiesAntiDiagonal&Utils.antiDiagonalMasks[(s / 8) + 7 - (s % 8)]);
    }

    public static long unsafeForBlack(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        long unsafe;
        occupied=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        //pawn
        unsafe=((WP>>>7)&~Utils.fileA);//pawn capture right
        unsafe|=((WP>>>9)&~Utils.fileH);//pawn capture left
        long possibility;
        //knight
        long i=WN&~(WN-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            if (iLocation>18)
            {
                possibility=Utils.knightSpan<<(iLocation-18);
            }
            else {
                possibility=Utils.knightSpan>>(18-iLocation);
            }
            if (iLocation%8<4)
            {
                possibility &=~Utils.filesGH;
            }
            else {
                possibility &=~Utils.filesAB;
            }
            unsafe |= possibility;
            WN&=~i;
            i=WN&~(WN-1);
        }
        //bishop/queen
        long QB=WQ|WB;
        i=QB&~(QB-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=diagonalMoves(iLocation);
            unsafe |= possibility;
            QB&=~i;
            i=QB&~(QB-1);
        }
        //rook/queen
        long QR=WQ|WR;
        i=QR&~(QR-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=verticalMoves(iLocation);
            unsafe |= possibility;
            QR&=~i;
            i=QR&~(QR-1);
        }
        //king
        int iLocation=Long.numberOfTrailingZeros(WK);
        if (iLocation>9)
        {
            possibility=Utils.kingSpan<<(iLocation-9);
        }
        else {
            possibility=Utils.kingSpan>>(9-iLocation);
        }
        if (iLocation%8<4)
        {
            possibility &=~Utils.filesGH;
        }
        else {
            possibility &=~Utils.filesAB;
        }
        unsafe |= possibility;
        return unsafe;
    }

}