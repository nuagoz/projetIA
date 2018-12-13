package Model;

import java.util.Arrays;

/**
 * Classe qui regroupe toutes les variables utiles pour la
 * gestion des bitboards
 */
public class Utils {
    public static final long fileA = 72340172838076673L; // pour les pions
    public static final long fileH = -9187201950435737472L; // pour les pions
    public static final long filesAB = 217020518514230019L; // pour les cavaliers
    public static final long filesGH = -4557430888798830400L; // pour les cavaliers
    public static final long rank1 = -72057594037927936L; // pour les pions noirs (promotion)
    public static final long rank8 = 255L; // pour les pions blanc (promotion)
    public static final long rank4 = 1095216660480L; // pour les pions (en passant)
    public static final long rank5 = 4278190080L; // pour les pions (en passant)
    public static final long kingSpan = 460039L;
    public static final long knightSpan = 43234889994L;

    public static final long[] RANKS = {
            0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L
    }; // Pour les deplacements en diagonale

    public static final long[] FILES = {
            0x0101010101010101L, /* A */
            0x0202020202020202L, /* B */
            0x0404040404040404L, /* C */
            0x0808080808080808L, /* D */
            0x1010101010101010L, /* E */
            0x2020202020202020L, /* F */
            0x4040404040404040L, /* G */
            0x8080808080808080L  /* H */
    };

    public static final long[] diagonalMasks = {
            0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
            0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
            0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
    }; // Masques pour les mouvements en diagonale (haut gauche à bas droite)
    public static final long[] antiDiagonalMasks = {
            0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
            0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
            0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
    };// Masques pour les mouvements en anti diagonale (haut droite a bas gauche)

    public static final long[] castleRooks = { 63, 56, 7, 0 };

    public static void drawBitboard(long bitBoard) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]="";
        }
        for (int i=0;i<64;i++) {
            if (((bitBoard>>>i)&1)==1) {chessBoard[i/8][i%8]="x";}
            if ("".equals(chessBoard[i/8][i%8])) {chessBoard[i/8][i%8]=" ";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }


}
