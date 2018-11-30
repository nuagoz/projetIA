package Model;

import java.util.LinkedList;

/**
 * Classe utilisee pour determiner l'etat de la partie
 */
public class GameState {
    private long occupied;
    private long empty;
    private long whiteCaptures;
    private long blackCaptures;
    private long whiteCanGo;
    private long blackCanGo;
    private long enpassant;
    LinkedList<String> moveList;

    GameState() {
        moveList = new LinkedList<>();
    }


}
