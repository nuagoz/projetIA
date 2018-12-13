package Model;

import java.security.SecureRandom;

public class Zobrist {
    // 12 pour les differents types de pieces, 64 pour la case sur l'echiquier
    static long randomTable[][] = new long[12][64];
    static long castlingTable[] = new long[4];

    public static long generateRandom64() {
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }

    public static void initRandomArray() {
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < 64; j++) {
                randomTable[i][j] = generateRandom64();
            }
        }

        for(int i = 0; i < 4; i++)
            castlingTable[i] = generateRandom64();
    }

    /**
     *
     * @param WP = 0
     * @param WN = 1
     * @param WB = 2
     * @param WR = 3
     * @param WQ = 4
     * @param WK = 5
     * @param BP = 6
     * @param BN = 7
     * @param BB = 8
     * @param BR = 9
     * @param BQ = 10
     * @param BK = 11
     * @param enPassant
     * @param castlingRights
     * @param currentPlayer
     * @return
     */
    public static long getHash(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB, long BR,
                               long BQ,long BK,long enPassant, CastlingRights castlingRights, Player currentPlayer) {
        long hash = 0L;

        for(int i = 0; i < 64; i++) {
            if(((WP >> i) & 1) == 1)
                hash ^= randomTable[0][i];
            else if(((WN >> i) & 1) == 1)
                hash ^= randomTable[1][i];
            else if(((WB >> i) & 1) == 1)
                hash ^= randomTable[2][i];
            else if(((WR >> i) & 1) == 1)
                hash ^= randomTable[3][i];
            else if(((WQ >> i) & 1) == 1)
                hash ^= randomTable[4][i];
            else if(((WK >> i) & 1) == 1)
                hash ^= randomTable[5][i];
            else if(((BP >> i) & 1) == 1)
                hash ^= randomTable[6][i];
            else if(((BN >> i) & 1) == 1)
                hash ^= randomTable[7][i];
            else if(((BB >> i) & 1) == 1)
                hash ^= randomTable[8][i];
            else if(((BR >> i) & 1) == 1)
                hash ^= randomTable[9][i];
            else if(((BQ >> i) & 1) == 1)
                hash ^= randomTable[10][i];
            else if(((BK >> i) & 1) == 1)
                hash ^= randomTable[11][i];
        }
        return hash;
    }

}
