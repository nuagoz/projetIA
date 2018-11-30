package Model;

import java.util.Arrays;
import java.util.LinkedList;

public class MoveGenerator {

    long occupied;
    long empty;
    long whiteCaptures;
    long blackCaptures;
    long whiteCanGo;
    long blackCanGo;
    long enPassant;
    long NOT_MY_PIECES;
    Player player;
    LinkedList<String> moveList; // linked list of all possible white moves

    public MoveGenerator(Player player) {
        this.player = player;
        moveList = new LinkedList<>();
    }
    public String generatePossibleMoves(long WK, long WQ, long WR, long WB, long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP) {
        long AllWhite = WK | WQ | WR | WB | WN | WP;
        long AllBlack = BK | BQ | BR | BB | BN | BP;
        drawBitboard(WB);
        occupied = AllWhite | AllBlack;
        empty = ~occupied;
        whiteCanGo = ~AllWhite;
        blackCanGo = ~AllBlack;
        whiteCaptures = AllBlack;
        blackCaptures = AllWhite;

        System.out.println(occupied);

        return player == Player.WHITE ?
                generateWhitePossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP)
                :
                generateBlackPossibleMoves(WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP);
    }

    private String generateWhitePossibleMoves(long WK, long WQ, long WR, long WB, long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP) {
        NOT_MY_PIECES = ~(WP|WN|WB|WR|WQ|WK|BK);

        String allMoves = "";
        allMoves += generateB(occupied, WB);//generateBishopsMoves(WB, whiteCanGo);
        System.out.println("allmoves : " + allMoves);

        return allMoves;
    }

    private String generateBlackPossibleMoves(long WK, long WQ, long WR, long WB, long WN, long WP, long BK, long BQ, long BR, long BB, long BN, long BP) {
        return "";
    }

    private String generateB(long occupied, long B) {
        String list="";
        long i=B&-B;
        long possibility;
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=dMoves(iLocation)&NOT_MY_PIECES;
            long j=possibility&-possibility;
            while (j != 0)
            {
                int index=Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j;
                j=possibility&-possibility;
            }
            B&=~i;
            i=B&-B;
        }
        return list;
    }

    private String generateBishopsMoves(long bishops, long destinationSquares) {
        StringBuilder bishopMoves = new StringBuilder();
        long currentBishop = bishops & -bishops;
        long moves;
        while(currentBishop != 0) {
            int bishopPosition = Long.numberOfTrailingZeros(currentBishop);
            moves = diagonalMoves(bishopPosition) & destinationSquares;
            long nextMove = moves & -moves; // Premier coup possible du fou actuel
            while(nextMove != 0) {
                int index = Long.numberOfTrailingZeros(nextMove);
                bishopMoves.append((bishopPosition / 8) + (bishopPosition % 8) + (index / 8) + (index % 8));
                moves &= ~nextMove; // On enleve le coup actuel de la liste
                nextMove = moves & -moves; // On prend le coup suivant
                System.out.println(nextMove);
            }
            bishops &= ~nextMove; // On enleve le fou actuel de la liste
            currentBishop = bishops & -bishops; // On passe au fou suivant
        }

        return bishopMoves.toString();
    }

    private long diagonalMoves(int position) {
        long bitPos = 1L << position;
        long posDiagonal = ((occupied & Utils.diagonalMasks[(position / 8) + (position % 8)]) - (2 * bitPos)) ^ Long.reverse(Long.reverse(occupied & Utils.diagonalMasks[(position / 8) + (position % 8)]) - (2 * Long.reverse(bitPos)));
        long posAntiDiagonal = ((occupied & Utils.antiDiagonalMasks[(position / 8) + 7 - (position % 8)]) - (2 * bitPos)) ^ Long.reverse(Long.reverse(occupied & Utils.antiDiagonalMasks[(position / 8) + 7 - (position % 8)]) - (2 * Long.reverse(bitPos)));
        return (posDiagonal & Utils.diagonalMasks[(position / 8) + (position % 8)]) | (posAntiDiagonal & Utils.antiDiagonalMasks[(position / 8) + 7 - (position % 8)]);
    }

    private long dMoves(int s) {
        long binaryS=1L<<s;
        long possibilitiesDiagonal = ((occupied&Utils.diagonalMasks[(s / 8) + (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(occupied&Utils.diagonalMasks[(s / 8) + (s % 8)]) - (2 * Long.reverse(binaryS)));
        long possibilitiesAntiDiagonal = ((occupied&Utils.antiDiagonalMasks[(s / 8) + 7 - (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(occupied&Utils.antiDiagonalMasks[(s / 8) + 7 - (s % 8)]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesDiagonal&Utils.diagonalMasks[(s / 8) + (s % 8)]) | (possibilitiesAntiDiagonal&Utils.antiDiagonalMasks[(s / 8) + 7 - (s % 8)]);
    }

    public static void drawBitboard(long bitBoard) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]="";
        }
        for (int i=0;i<64;i++) {
            if (((bitBoard>>>i)&1)==1) {chessBoard[i/8][i%8]="P";}
            if ("".equals(chessBoard[i/8][i%8])) {chessBoard[i/8][i%8]=" ";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }

}
