package de.juliamiller.minesweeper;

public class feld {
    boolean bombe;
    int x,y;
    int adjacent; // benachbart

    //Constructor
    public feld(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
