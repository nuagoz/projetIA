package Model;

public class Evaluation {

    public static final int MATE_VALUE = 300000;

    /* Valeurs des pieces */
    private static final int pawnValue = 100;
    private static final int bishopValue = 330;
    private static final int knightValue = 330;
    private static final int rookValue = 500;
    private static final int queenValue = 900;
    private static final int kingValue = MATE_VALUE;

    /* Malus pour les cavaliers, tours et pions */
    private static final int knightPenalty = -10;
    private static final int rookPenalty = -20;
    private static final int noPawnsPenalty = -20;

    // Bonus en fonction du nombre de pions restants
    private static final int[] knightPawnAdjustment =
            {-30, -20, -15, -10, -5, 0, 5, 10, 15};

    private static final int[] rookPawnAdjustment =
            {25, 20, 15, 10, 5, 0, -5, -10, -15};

    private static final int[] dualBishopPawnAdjustment =
            {40, 40, 35, 30, 25, 20, 20, 15, 15};

    private static final int tempoBonus = 10;


    private static final int pawnScores[][] = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {5, 10, 10,-20,-20, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static final int rookScores[][] = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {0,  0,  0,  5,  5,  0,  0,  0}
    };

    private static final int knightScores[][] = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}
    };

    private static final int bishopScores[][] = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}
    };

    private static final int queenScores [][] = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}
    };

    private static final int kingMidGame[][] = {
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {20, 30, 10,  0,  0, 10, 30, 20}
    };

    private static final int kingEndGame[][] = {
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}
    };

    public static int getValue(Player currentPlayer, long WP, long WN, long WB, long WR, long WQ, long WK,
                               long BP, long BN, long BB, long BR, long BQ, long BK) {

        int totalValue = 0;
        int scoreWhite = 0, scoreBlack = 0;
        long whiteKingUnsafe, blackKingUnsafe;

        scoreWhite += Long.bitCount(WP) * pawnValue;
        scoreWhite += Long.bitCount(WN) * knightValue;
        scoreWhite += Long.bitCount(WB) * bishopValue;
        scoreWhite += Long.bitCount(WR) * rookValue;
        scoreWhite += Long.bitCount(WQ) * queenValue;
        scoreWhite += Long.bitCount(WK) * kingValue;

        scoreBlack += Long.bitCount(BP) * pawnValue;
        scoreBlack += Long.bitCount(BN) * knightValue;
        scoreBlack += Long.bitCount(BB) * bishopValue;
        scoreBlack += Long.bitCount(BR) * rookValue;
        scoreBlack += Long.bitCount(BQ) * queenValue;
        scoreBlack += Long.bitCount(BK) * kingValue;

        long squaresAttackedByBlack = MoveGenerator.generateUnsafeSquares(Player.WHITE, BP, BN, BB, BR, BQ, BK, WP, WN, WB, WR, WQ, WK);
        long squaresAttackedByWhite = MoveGenerator.generateUnsafeSquares(Player.BLACK, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
        whiteKingUnsafe = WK & squaresAttackedByBlack;
        blackKingUnsafe = BK & squaresAttackedByWhite;

        //totalValue +=

        //Utils.drawBitboard(MoveGenerator.generateUnsafeSquares(Player.BLACK, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK));

        if(currentPlayer == Player.WHITE) {
            totalValue += addPiecesPlacement(WP, WB, WR, WN, WQ, WK);
            if(whiteKingUnsafe != 0) // DANGER
                totalValue -= 10000000;
            if(blackKingUnsafe != 0) // BIEN
                totalValue += 500;
        }
        else {
            totalValue += addPiecesPlacement(Long.reverse(BP), Long.reverse(BB), Long.reverse(BR), Long.reverse(BN), Long.reverse(BQ), Long.reverse(BK));
            if(blackKingUnsafe != 0) // DANGER
                totalValue -= 10000000;
            if(whiteKingUnsafe != 0) // BIEN
                totalValue += 500;
        }

        totalValue += currentPlayer == Player.WHITE ? scoreWhite - scoreBlack : scoreBlack - scoreWhite;
        return totalValue;
    }

    private static int addPiecesPlacement(long pawns, long bishops, long rooks, long knights, long queens, long king) {

        int placementValue = 0;

        long currentPawn = pawns & -pawns;
        while(currentPawn != 0) {
            int pawnLocation = Long.numberOfTrailingZeros(currentPawn);
            placementValue += pawnScores[pawnLocation / 8][pawnLocation % 8];
            pawns &= ~currentPawn;
            currentPawn = pawns & -pawns;
        }

        long currentBishop = bishops & -bishops;
        while(currentBishop != 0) {
            int bishopLocation = Long.numberOfTrailingZeros(currentBishop);
            placementValue += bishopScores[bishopLocation / 8][bishopLocation % 8];
            bishops &= ~currentBishop;
            currentBishop = bishops & -bishops;
        }

        long currentRook = rooks & -rooks;
        while(currentRook != 0) {
            int rookLocation = Long.numberOfTrailingZeros(currentRook);
            placementValue += rookScores[rookLocation / 8][rookLocation % 8];
            rooks &= ~currentRook;
            currentRook = rooks & -rooks;
        }

        long currentKnight = knights & -knights;
        while(currentKnight != 0) {
            int knightLocation = Long.numberOfTrailingZeros(currentKnight);
            placementValue += knightScores[knightLocation / 8][knightLocation % 8];
            knights &= ~currentKnight;
            currentKnight = knights & -knights;
        }

        long currentQueen = queens & -queens;
        while(currentQueen != 0) {
            int queenLocation = Long.numberOfTrailingZeros(currentQueen);
            placementValue += knightScores[queenLocation / 8][queenLocation % 8];
            queens &= ~currentQueen;
            currentQueen = queens & -queens;
        }

        int kingLocation = Long.numberOfTrailingZeros(king);
        placementValue += kingMidGame[kingLocation / 8][kingLocation % 8];

        return placementValue;
    }
}
