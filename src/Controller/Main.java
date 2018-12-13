package Controller;

import Model.Game;
import Model.Player;
import Model.Search;
import Model.UCI;

public class Main {
    public static void main(String [] args)
    {
        /*String[] moves = { "ijorsjg", "orsgjoid", "sporfk", "a2a4", "e7e5",  "a7a5" };
        Search search = new Search(moves);
        String bestMove = search.deepeningSearchtest();
        System.out.println(bestMove);*/

        Game bitboard = new Game(Player.BLACK);
        UCI chessSoftware = new UCI(bitboard);
        chessSoftware.connect();
        //BoardGeneration.initiate();
    }
}
