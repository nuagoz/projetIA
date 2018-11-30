package Model;

import java.util.Scanner;

public class UCI {
    private static final String ENGINE_NAME = "StoqueFiche";
    private static final String AUTHORS = "Paul / Louison / Zoé / Etienne";
    private Bitboard bitboard;

    public UCI (Bitboard bitboard) {
        this.bitboard = bitboard;
    }
    /**
     * Méthode permettant de communiquer avec ChessArena via le
     * protocole UCI
     */
    public void connect() {
        Scanner scan = new Scanner(System.in);
        while(true){
            String inputString = scan.nextLine();
            if(inputString.equals("uci"))
                inputUCI();
            else if(inputString.startsWith("setoption"))
                setOptions(inputString);
            else if(inputString.equals("isready"))
                isReady();
            else if(inputString.startsWith("setboard"))
              setBoard(inputString);
            else if(inputString.equals("ucinewgame"))
                newGame();
            else if(inputString.startsWith("position"))
                newPosition(inputString);
            else if(inputString.startsWith("go"))
                go();
            else if(inputString.equals("print"))
                print();
            else if(inputString.equals("quit")){
                quit();
                break;
            }
            else if(inputString.equals("compute"))
                compute();
            else {
                System.out.println("Manual communication");
            }
        }
        scan.close();
    }

    private void inputUCI() {
        System.out.println("Engine : " + ENGINE_NAME);
        System.out.println("Authors : " + AUTHORS);

        // ChessBoardFactory
    }

    private void setOptions(String inputString) {
        System.out.println("Set options : " + inputString);
    }

    private void isReady() {
        System.out.println("Ready to start !");
        // Initiate ChessBoard
    }

    private void newGame() {
        System.out.println("New game !");
        // Initiate ChessBoard
    }

    /**
     * Fonction appelee lorsqu'un coup est jouee. Elle permet d'obtenir le
     * contenu du plateau sous format FEN
     * @param inputString
     */
    private void newPosition(String inputString) {
        System.out.println("New Position");
        String[] plateau = inputString.split(" ");

        // Dans le cas ou le plateau de depart est le plateau de base
        if(plateau[1].equals("startpos")) {
            fenToBoard("fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        }
        // Dans le cas ou le plateau de depart est different que le plateau de base
        else if(plateau[1].startsWith("fen")) {
            fenToBoard(plateau[2]);
        }

        // On applique ensuite la liste des mouvements qui ont ete realises a partir du plateau de depart
        if(plateau[2].equals("moves")) {
            System.out.println("Moves : " + plateau);
            for(int i = 3; i < plateau.length; i++)
                bitboard.makeMove(plateau[i]);
        }

        bitboard.displayChessBoard();
        //System.out.println("bestmove c7c5");

    }

    private void fenToBoard(String FEN) {
        char[] blackPieces = { 'r', 'n', 'b', 'q', 'k', 'p' };
        char[] whitePieces = { 'R', 'N', 'B', 'Q', 'K', 'P' };
        char[] numbers = { '1', '2', '3', '4', '5', '6', '7', '8'};
        int rank = 0, file = 0;
        char currentPiece;
        String[] parts = FEN.split(" ");
        for(int i = 0; i < parts[1].length(); i++) {
            currentPiece = parts[1].charAt(i);
            if(inArray(currentPiece, whitePieces) || inArray(currentPiece, blackPieces)){
                bitboard.chessBoard[rank][file] = Character.toString(currentPiece);
                file++;
            }
            else if(inArray(currentPiece, numbers)) {
                while(file < currentPiece - '0') {
                    bitboard.chessBoard[rank][file] = " ";
                    file++;
                }
            }
            else if(currentPiece == '/') {
                rank++;
                file = 0;
            }
        }
    }

    /**
     * Renvoie true si le caractere est dans le tableau, false sinon
     * @param elt : caractere a tester
     * @param array : liste de caracteres
     * @return boolean
     */
    private boolean inArray(char elt, char[] array) {
        for(int i = 0; i < array.length; i++) {
            if(elt == array[i])
                return true;
        }
        return false;
    }

    private void setBoard(String inputString) {
        System.out.println("Setboard received");
        inputString = inputString.substring(9).concat(" ");
        System.out.println("{input} " + inputString);
    }

    private void go() {
        System.out.println("Go : start AI");
    }

    private void print() {
        System.out.println("Print some infos");
    }

    private void quit() {
        System.out.println("Game over");
    }

    private void compute() {
        Node node = new Node(bitboard.chessBoard);
        System.out.println("Moves for white bishops :" + node.getPossiblesMoves());
    }

}
