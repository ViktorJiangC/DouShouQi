package model;
public class ChessPiece {
    // the owner of the chess
    private PlayerColor owner;
    // Elephant? Cat? Dog? ...
    private String name;
    private int rank;
    private int trappedturn = 0;
    private boolean isTrapped = false;
    public boolean isTrapped() {
        return isTrapped;
    }
    public void setTrapped(boolean trapped) {
        isTrapped = trapped;
    }

    public ChessPiece(PlayerColor owner, String name, int rank) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
    }
    public int getRank() {
        return rank;
    }
    public String getName() {
        return name;
    }

    public PlayerColor getOwner() {
        return owner;
    }

    public void setTrappedturn(int trappedturn) {
        this.trappedturn = trappedturn;
    }
    public int getTrappedturn() {
        return trappedturn;
    }


}