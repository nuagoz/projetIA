package Controller;

import Model.Game;
import Model.Player;
import Model.UCI;

public class Main {
    public static void main(String [] args)
    {
        Game bitboard = new Game(Player.BLACK);
        UCI chessSoftware = new UCI(bitboard);
        chessSoftware.connect();
        //BoardGeneration.initiate();
    }
}
