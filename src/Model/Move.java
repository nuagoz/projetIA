package Model;

public class Move {
    public static long makeMove(long bitboard, String move, char type) {
        int departure, destination;
        if(Character.isDigit(move.charAt(3))) { // Mouvement normal
            departure = (Character.getNumericValue(move.charAt(0)) * 8) + (Character.getNumericValue(move.charAt(1)));
            destination = (Character.getNumericValue(move.charAt(2)) * 8) + (Character.getNumericValue(move.charAt(3)));
            if(((bitboard >>> departure) & 1) == 1) {
                bitboard &= ~(1L << departure);
                bitboard |= (1L << destination);
            }
            else
                bitboard &= ~(1L << destination); // Update piece
        }
        else if(move.charAt(3) == 'P') { // Promotion
            System.out.println("PROMOTION ! " + move);
            if(Character.isUpperCase(move.charAt(2))) {
                departure = Long.numberOfTrailingZeros(Utils.FILES[move.charAt(0)-'0'] & Utils.RANKS[1]);
                destination = Long.numberOfTrailingZeros(Utils.FILES[move.charAt(1)-'0'] & Utils.RANKS[0]);
            }
            else {
                departure = Long.numberOfTrailingZeros(Utils.FILES[move.charAt(0)-'0'] & Utils.RANKS[6]);
                destination = Long.numberOfTrailingZeros(Utils.FILES[move.charAt(1)-'0'] & Utils.RANKS[7]);
            }
            if(type == move.charAt(2))
                bitboard |= (1L << destination);
            else {
                bitboard &= (1L << departure);
                bitboard &= ~(1L << departure);
            }
        }

        return bitboard;
    }

    public static long makeCastle(long rooks, long king, String move) {
        int departure = (Character.getNumericValue(move.charAt(0)) * 8) + (Character.getNumericValue(move.charAt(1)));
        if(((king >>> departure) & 1) == 1) { // Si c'est bien qui est bouge par le mouvement
            if(move.equals("0402") || move.equals("0406") || move.equals("7472") || move.equals("7476")) {
                switch(move) {
                    case "7476": // Petit roque, joueur blanc
                        rooks &= ~(1L << Utils.castleRooks[0]);
                        rooks |= (1L << (Utils.castleRooks[0] - 2)); // on decale la tour de 2 cases vers la gauche
                        break;
                    case "7472": // Grand roque, joueur blanc
                        rooks &= ~(1L << Utils.castleRooks[1]);
                        rooks |= (1L << (Utils.castleRooks[1] + 3)); // on decale la tour de 3 cases vers la droite
                        break;
                    case "0406": // Petit roque, joueur noir
                        rooks &= ~(1L << Utils.castleRooks[2]);
                        rooks |= (1L << (Utils.castleRooks[2] - 2)); // on decale la tour de 2 cases vers la gauche
                        break;
                    case "0402": // Grand roque, joueur noir
                        rooks &= ~(1L << Utils.castleRooks[3]);
                        rooks |= (1L << (Utils.castleRooks[3] + 3)); // on decale la tour de 3 cases vers la droite
                        break;
                }
            }
        }
        return rooks;
    }
}
