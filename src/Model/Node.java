package Model;

public class Node {
    private long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0, WK = 0, BP = 0, BN = 0, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;
    private String chessBoard[][] = {
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " }
    };
    public Node(String chessBoard[][]) {
        generateBitboards(chessBoard);
        System.out.println("bitboards ok");
        System.out.println(WP);
    }

    public String getPossiblesMoves() {
        MoveGenerator moveGen = new MoveGenerator(Player.WHITE);
        return moveGen.generatePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP);
    }

    private void generateBitboards(String[][] chessBoard) {
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

    private long convertStringToBitboard(String binary) {
        return binary.charAt(0) == '0' ? Long.parseLong(binary, 2) : Long.parseLong("1" + binary.substring(2), 2) * 2;
    }


}
