package Model;

import java.util.Scanner;

public class UCI {
    private static final String ENGINE_NAME = "StoqueFiche";
    private static final String AUTHORS = "Louison / Zoé / Etienne";
    private Game game;

    public UCI (Game game) {
        this.game = game;
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
        String[] plateau = inputString.split(" ");

        // Dans le cas ou le plateau de depart est le plateau de base
        if(plateau[1].equals("startpos")) {
            fenToBoard("fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        }
        // Dans le cas ou le plateau de depart est different que le plateau de base
        else if(plateau[1].startsWith("fen")) {
            fenToBoard(plateau[2]);
        }

        if(plateau.length < 3)
            return;

        // On applique ensuite la liste des mouvements qui ont ete realises a partir du plateau de depart
        if(plateau[2].equals("moves")) {
            for(int i = 3; i < plateau.length; i++)
                game.makeMove(plateau[i]);
        }
        //game.displayChessBoard();
        Node node = new Node(game.chessBoard);
        game.displayChessBoard();
        int depth = 6;
        long startTime = System.currentTimeMillis();
        Search search = new Search(plateau);
        String bestMove = search.deepeningSearch(1000, depth, node.WP, node.WN, node.WB, node.WR, node.WQ, node.WK, node.BP, node.BN, node.BB, node.BR, node.BQ, node.BK, node.enPassant, game.castlingRights, game.enginePlayer);
        //search.alphaBeta(Integer.MIN_VALUE, Integer.MAX_VALUE, node.WP, node.WN, node.WB, node.WR, node.WQ, node.WK, node.BP, node.BN, node.BB, node.BR, node.BQ, node.BK, node.enPassant, game.castlingRights, game.enginePlayer, depth);

        //String bestMove = search.alphaBeta2(Integer.MIN_VALUE, Integer.MAX_VALUE, node.WP, node.WN, node.WB, node.WR, node.WQ, node.WK, node.BP, node.BN, node.BB, node.BR, node.BQ, node.BK, node.enPassant, game.castlingRights, game.enginePlayer, depth);
        //int bestmoveIndex = search.alphaBeta(Integer.MIN_VALUE, Integer.MAX_VALUE, node.WP, node.WN, node.WB, node.WR, node.WQ, node.WK, node.BP, node.BN, node.BB, node.BR, node.BQ, node.BK, node.enPassant, game.castlingRights, game.enginePlayer, depth);

        if (search.book)
            System.out.println("bestmove " + bestMove);
        else {
            System.out.println("bestmove " + moveToAlgebra(bestMove));
            System.out.println("Depth " + (search.currentDepth - 1) + " time : " + (System.currentTimeMillis() - startTime));
            System.out.println("info depth " + (search.currentDepth - 1));
        }
        //Search
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
                game.chessBoard[rank][file] = Character.toString(currentPiece);
                file++;
            }
            else if(inArray(currentPiece, numbers)) {
                while(file < currentPiece - '0') {
                    game.chessBoard[rank][file] = " ";
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

    private String moveToAlgebra(String move)
    {
        String moveString="";
        moveString+=""+(char)(move.charAt(1)+49);
        moveString+=""+('8'-move.charAt(0));
        moveString+=""+(char)(move.charAt(3)+49);
        moveString+=""+('8'-move.charAt(2));
        return moveString;
    }

    private void compute() {
        Node node = new Node(game.chessBoard);
        long startTime = System.currentTimeMillis();
        //String moves = MoveGenerator.generatePossibleMoves(node.WK,node.BQ,node.WR,node.WB,node.WN,node.WP,node.BK,
                //node.BQ,node.BR,node.BB,node.BN,node.BP,node.enPassant,Player.BLACK);
        System.out.println("Evaluation = " + Evaluation.getValue(Player.BLACK, node.WP, node.WN, node.WB, node.WR, node.WQ,
                node.WK, node.BP, node.BN, node.BB, node.BR, node.BQ, node.BK));
        /*String moves = node.getPossiblesMoves(Player.WHITE);
        System.out.println("GENERATE MOVES TIME :" + (System.currentTimeMillis() - startTime));
        System.out.println("Moves : (" + (moves.length()/4) + ") " + moves);
        Utils.drawBitboard(node.WK|node.BK);
        System.out.println();
        startTime = System.currentTimeMillis();
        for (int i=0;i<moves.length();i+=4) {
            String currentMove = moves.substring(i, i+4);
            if(currentMove.equals("7476") || currentMove.equals("7472"))
                System.out.println("CASTLE !!!");
            System.out.println("Move : " + moves.substring(i, i+4));
            long rook = node.makeMove(node.WR, currentMove, 'R');
            long king = node.makeMove(node.WK, currentMove, 'K');
            long rookAfterCastle = node.makeCastle(rook, node.WK|node.BK, currentMove);
            Utils.drawBitboard(rookAfterCastle);
            System.out.println();
        }
        System.out.println("MAKE MOVES TIME :" + (System.currentTimeMillis() - startTime));*/

    }

}
