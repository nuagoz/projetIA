package Model;

public class Evaluation {

    private static final int pawnValue = 100;
    private static final int bishopValue = 300;
    private static final int knightValue = 300;
    private static final int rookValue = 500;
    private static final int queenValue = 900;
    private static final int kingValue = 10000000;

    int pawnScores[] = {
            0,  0,  0,  0,  0,  0,  0,  0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5,  5, 10, 25, 25, 10,  5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5, -5,-10,  0,  0,-10, -5,  5,
            5, 10, 10,-20,-20, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    int rookScores[] = {
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10, 10, 10, 10, 10,  5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            0,  0,  0,  5,  5,  0,  0,  0
    };

    int knightScores[] = {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50
    };

    int bishopScores[] = {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
    };

    int queenScores [] = {
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -5,  0,  5,  5,  5,  5,  0, -5,
            0,  0,  5,  5,  5,  5,  0, -5,
            -10,  5,  5,  5,  5,  5,  0,-10,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20
    };

    int kingMidGame[] = {
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 30, 10,  0,  0, 10, 30, 20
    };

    int kingEndGame[] = {
            -50,-40,-30,-20,-20,-30,-40,-50,
            -30,-20,-10,  0,  0,-10,-20,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-30,  0,  0,  0,  0,-30,-30,
            -50,-30,-30,-30,-30,-30,-30,-50
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

        whiteKingUnsafe = WK & MoveGenerator.generateUnsafeSquares(Player.WHITE, BP, BN, BB, BR, BQ, BK, WP, WN, WB, WR, WQ, WK);
        blackKingUnsafe = BK & MoveGenerator.generateUnsafeSquares(Player.BLACK, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);

        if(currentPlayer == Player.WHITE) {
            if(whiteKingUnsafe != 0) // DANGER
                totalValue -= 10000000;
            if(blackKingUnsafe != 0) // BIEN
                totalValue += 5000;
        }
        else {
            if(blackKingUnsafe != 0) // DANGER
                totalValue -= 10000000;
            if(whiteKingUnsafe != 0) // BIEN
                totalValue += 5000;
        }

        totalValue += currentPlayer == Player.WHITE ? scoreWhite - scoreBlack : scoreBlack - scoreWhite;
        return totalValue;
    }
}
