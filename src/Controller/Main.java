package Controller;

import Model.Bitboard;
import Model.BoardGeneration;
import Model.UCI;

public class Main {
    public static void main(String [] args)
    {
        Bitboard bitboard = new Bitboard();
        UCI chessSoftware = new UCI(bitboard);
        chessSoftware.connect();
        //BoardGeneration.initiate();
    }
}
