import java.util.*;

public class Triangle {
    private List<Checker> pieces;
    private int white;
    private int black;

    public Triangle(ArrayList<Checker> pieces) {
        this.pieces = pieces;
        reset();
    }

    private void reset() {
        this.white = 0;
        this.black = 0;

        for (Checker c: pieces) {
            if (c.getSide() == 0)
                white++;
            else
                black++;
        }
    }

    public int getWhite() {
        return this.white;
    }

    public int getBlack() {
        return this.black;
    }

    public int getOpposite(int side) {
        if (side == 0)
            return getBlack();
        else
            return getWhite();
    }

    public int getSame(int side) {
        if (side == 0)
            return getWhite();
        else
            return getBlack();
    }

    public int getTotal() {
        return this.white + this.black;
    }

    public int getLength() {
        return pieces.size();
    }

    public Checker getPieceAtIndex(int index) {
        return pieces.get(index);
    }

    public void removePiece(Checker c) {
        pieces.remove(c);
        reset();
    }

    public void addPiece(Checker c) {
        pieces.add(c);
        reset();
    }

    public List<Checker> checkerList() {
        return pieces;
    }
}