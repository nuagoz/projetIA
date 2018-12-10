package Model;

public class Game {

    public String chessBoard1[][] = {
        { "r", " ", "b", " ", "k", " ", "n", "r" },
        { "p", "p", "p", "p", " ", "p", "p", "p" },
        { "n", " ", " ", " ", "p", "q", " ", " " },
        { " ", " ", " ", " ", " ", " ", " ", " " },
        { " ", "b", " ", "P", "P", " ", " ", " " },
        { " ", " ", "N", " ", " ", " ", " ", " " },
        { "P", "B", "P", " ", " ", "P", "P", "P" },
        { "R", " ", " ", "Q", "K", "B", "N", "R" }
    };

    public String chessBoard[][] = {
            { " ", " ", " ", "k", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", "q", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", " ", " ", " ", " " },
            { " ", " ", " ", " ", "K", " ", " ", " " }
    };

    CastlingRights castlingRights = new CastlingRights(true, true, true, true);
    public Player enginePlayer;

    public Game(Player enginePlayer) {
        this.enginePlayer = enginePlayer;
        System.out.println("Game created");
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
