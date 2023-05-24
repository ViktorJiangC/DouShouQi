package model;

import Start.ImageComponent;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class ChessPiece extends JComponent implements Serializable {
    @Serial
    private static final long serialVersionUID = 7330431136406250138L;
    // the owner of the chess
    private PlayerColor owner;
    // Elephant? Cat? Dog? ...
    private String name;
    private int rank;
    private int trappedturn = 0;
    private boolean isTrapped = false;
    private String path;
    private int repeatTurn;
    private boolean isAlive = true;

    public boolean isAlive() {
        return isAlive;
    }
    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public int getRepeatTurn() {
        return repeatTurn;
    }
    public void setRepeatTurn(int repeatTurn) {
        this.repeatTurn = repeatTurn;
    }
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
    public ChessPiece(PlayerColor owner,String name, int rank,String path){
        this.owner=owner;
        this.name=name;
        this.rank=rank;
        this.path=path;
    }
    public int getRank() {
        return rank;
    }
    public String getName() {
        return name;
    }
    public String getPath(){return path;}

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