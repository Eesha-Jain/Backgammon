import java.util.*;

public class Checker {
    //0 - white, 1 - red
    private int side;
    private boolean inGame;

    public Checker(int side) {
        this.side = side;
        this.inGame = true;
    }

    public int getSide() {
        return this.side;
    }

    public boolean inGame() {
        return this.inGame;
    }

    public void changeInGame() {
        inGame = !inGame;
    }
}