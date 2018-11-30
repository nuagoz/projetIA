package Model;

public class Bitboard {
    /*public String chessBoard[][] = {
        { "r", "n", "b", "q", "k", "b", "n", "r" },
        { "p", "p", "p", "p", "p", "p", "p", "p" },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { "P", "P", "P", "P", "P", "P", "P", "P" },
        { "R", "N", "B", "Q", "K", "B", "N", "R" }
    };*/
    public String chessBoard[][] = {
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " }
    };
    private long masks[];
    private int WHITE = 0;
    private int BLACK = 0;

    // Files
    public static final int A = 0;
    public static final int B = 1;
    public static final int C = 2;
    public static final int D = 3;
    public static final int E = 4;
    public static final int F = 5;
    public static final int G = 6;
    public static final int H = 7;

    private static final long[] RANKS = {
        0xFF00000000000000L,
        0x00FF000000000000L,
        0x0000FF0000000000L,
        0x000000FF00000000L,
        0x00000000FF000000L,
        0x0000000000FF0000L,
        0x000000000000FF00L,
        0x00000000000000FFL
    };

    private static final long[] FILES = {
        0x0101010101010101L, /* A */
        0x0202020202020202L, /* B */
        0x0404040404040404L, /* C */
        0x0808080808080808L, /* D */
        0x1010101010101010L, /* E */
        0x2020202020202020L, /* F */
        0x4040404040404040L, /* G */
        0x8080808080808080L, /* H */
    };

    long WK = 0x1000000000000000L; // White King
    long WQ = 0x0800000000000000L; // White Queen
    long WR = 0x8100000000000000L; // White Rook
    long WB = 0x2400000000000000L; // White Bishop
    long WN = 0x4200000000000000L; // White Knight
    long WP = 0x00FF000000000000L; // White Pawn

    long BK = 0x0000000000000010L; // Black King
    long BQ = 0x0000000000000008L; // Black Queen
    long BR = 0x0000000000000081L; // Black Rook
    long BB = 0x0000000000000024L; // Black Bishop
    long BN = 0x0000000000000042L; // Black Knight
    long BP = 0x000000000000FF00L; // Black Pawn

    long AllWhite = WK | WQ | WR | WB | WN | WP;
    long AllBlack = BK | BQ | BR | BB | BN | BP;

    long ALL = AllWhite | AllBlack; // All pieces
    long EMPTY = -ALL;

    public Bitboard() {
        System.out.println("Bitboard created");
    }



    public void displayChessBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(chessBoard[i][j].equals(" "))
                    System.out.print(". ");
                else
                    System.out.print(chessBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Methode qui permet de realiser un coup
     * Notation d'un coup : https://en.wikipedia.org/wiki/Algebraic_notation_(chess)
     * @param move
     */
    public void makeMove(String move) {
        System.out.println("Move to do : " + move);
        boolean isWhiteMoving;
        String[] tokens = move.split("(?!^)");
        boolean capture = false;
        int offset = 0;

        // Etape 1 : Si le coup est un "roque" (petit ou grand)
        if(move.equals("e1g1")){
            whiteShortCastle();
            return;
        }
        if(move.equals("e1c1")) {
            whiteLongCastle();
            return;
        }
        if(move.equals("e8g8")) {
            blackShortCastle();
            return;
        }
        if(move.equals("e8c8")) {
            blackLongCastle();
            return;
        }

        // Etape 2 : On verifie si le coup est une capture
        if(tokens[2].equalsIgnoreCase("x")) { // Le coup est une capture
            capture = true;
            offset = 1;
        }
        Square departure = new Square(charToIndex(tokens[0]), 8-Integer.parseInt(tokens[1]));
        Square destination = new Square(charToIndex(tokens[2 + offset]), 8-Integer.parseInt(tokens[3 + offset]));
        String pieceMoved = chessBoard[departure.rank][departure.file];

        // Etape 3 : Detecter si c'est les blancs ou les noirs qui jouent
        isWhiteMoving = pieceMoved.equals(pieceMoved.toLowerCase());

        // Etape 4 : Mise a jour du plateau
        chessBoard[departure.rank][departure.file] = Piece.empty;
        chessBoard[destination.rank][destination.file] = pieceMoved;

        // Etape 5 : Detecter si le coup a ete une promotion de pion
        if(tokens.length > (4 + offset)) {
            pawnPromotion(destination, tokens[4 + offset], isWhiteMoving);
        }
    }

    private void pawnPromotion(Square destination, String promotedTo, boolean isWhiteMoving) {
        if(isWhiteMoving) {
            chessBoard[destination.rank][destination.file] = promotedTo.toLowerCase();
        }
        else {
            chessBoard[destination.rank][destination.file] = promotedTo;
        }
    }

    private void whiteShortCastle() {
        chessBoard[7][4] = Piece.empty;
        chessBoard[7][7] = Piece.empty;
        chessBoard[7][6] = Piece.whiteKing;
        chessBoard[7][5] = Piece.whiteRook;
    }

    private void whiteLongCastle() {
        chessBoard[7][4] = Piece.empty;
        chessBoard[7][0] = Piece.empty;
        chessBoard[7][2] = Piece.whiteKing;
        chessBoard[7][3] = Piece.whiteRook;
    }

    private void blackShortCastle() {
        chessBoard[0][4] = Piece.empty;
        chessBoard[0][7] = Piece.empty;
        chessBoard[0][6] = Piece.blackKing;
        chessBoard[0][5] = Piece.blackRook;
    }

    private void blackLongCastle() {
        chessBoard[0][4] = Piece.empty;
        chessBoard[0][0] = Piece.empty;
        chessBoard[0][2] = Piece.blackKing;
        chessBoard[0][3] = Piece.blackRook;
    }
    /**
     * Methode qui convertit une lettre par l'indice correspondant
     * sur l'echiquier (ex a = 0, b = 1, c = 2...)
     * @param letter
     * @return
             */
    public int charToIndex(String letter) {
        return (int) Character.toLowerCase(letter.charAt(0)) - 97;
    }

}
