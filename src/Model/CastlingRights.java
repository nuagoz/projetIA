package Model;

public class CastlingRights {
    public boolean shortWhiteCastle;
    public boolean longWhiteCastle;
    public boolean shortBlackCastle;
    public boolean longBlackCastle;

    public CastlingRights(boolean SWC, boolean LWC, boolean SBC, boolean LBC) {
        shortWhiteCastle = SWC;
        longWhiteCastle = LWC;
        shortBlackCastle = SBC;
        longBlackCastle = LBC;
    }
    public CastlingRights(CastlingRights newCastlingRights) {
        this.shortWhiteCastle = newCastlingRights.shortWhiteCastle;
        this.longWhiteCastle = newCastlingRights.longWhiteCastle;
        this.shortBlackCastle = newCastlingRights.shortBlackCastle;
        this.longBlackCastle = newCastlingRights.longBlackCastle;
    }
}
