package Model;

import java.util.Arrays;

public class BoardGeneration {
    private static long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0, WK = 0, BP = 0, BN = 0, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;
    public static void initiate() {
        String chessBoard[][] = {
                { "r", "n", "b", "q", "k", "b", "n", "r" },
                { "p", "p", "p", "p", "p", "p", "p", "p" },
                { " ", " ", " ", " ", " ", " ", " ", " " },
                { " ", " ", " ", " ", " ", " ", " ", " " },
                { " ", " ", " ", " ", " ", " ", " ", " " },
                { " ", " ", " ", " ", " ", " ", " ", " " },
                { "P", "P", "P", "P", "P", "P", "P", "P" },
                { "R", "N", "B", "Q", "K", "B", "N", "R" }
        };
        arrayToBitboards(chessBoard);
        drawArray();
    }

    private static void arrayToBitboards(String[][] chessBoard) {
        String binary;
        for(int i = 0; i < 64; i++) {
            binary = "0000000000000000000000000000000000000000000000000000000000000000";
            binary = binary.substring(i+1) + "1" + binary.substring(0, i);
            switch (chessBoard[i/8][i%8]) {
                case "P": WP += convertStringToBitboard(binary); break;
                case "N": WN += convertStringToBitboard(binary); break;
                case "B": WB += convertStringToBitboard(binary); break;
                case "R": WR += convertStringToBitboard(binary); break;
                case "Q": WQ += convertStringToBitboard(binary); break;
                case "K": WK += convertStringToBitboard(binary); break;
                case "p": BP += convertStringToBitboard(binary); break;
                case "n": BN += convertStringToBitboard(binary); break;
                case "b": BB += convertStringToBitboard(binary); break;
                case "r": BR += convertStringToBitboard(binary); break;
                case "q": BQ += convertStringToBitboard(binary); break;
                case "k": BK += convertStringToBitboard(binary); break;
            }
        }
    }

    private static long convertStringToBitboard(String binary) {
        return binary.charAt(0) == '0' ? Long.parseLong(binary, 2) : Long.parseLong("1" + binary.substring(2), 2) * 2;
    }

    private static void drawArray() {
        String chessBoard[][] = new String[8][8];
        for(int i = 0; i < 64; i++)
            chessBoard[i/8][i%8] = " ";

        for(int i = 0; i < 64; i++) {
            if(((WP >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "P";
            if(((WN >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "N";
            if(((WB >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "B";
            if(((WR >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "R";
            if(((WQ >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "Q";
            if(((WK >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "K";
            if(((BP >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "p";
            if(((BN >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "n";
            if(((BB >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "b";
            if(((BR >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "r";
            if(((BQ >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "q";
            if(((BK >> i) & 1) == 1)
                chessBoard[i/8][i%8] = "k";
        }

        for(int i = 0; i < 8; i++)
            System.out.println(Arrays.toString(chessBoard[i]));
    }

}
