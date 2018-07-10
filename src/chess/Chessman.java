package chess;

import java.awt.Point;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-26
 */

public abstract class Chessman implements Cloneable {

    /**
     * Unique identifier for each piece
     */
    protected UUID uuid;

    /**
     * the board on which these pieces reside
     */
    protected Board theBoard;

    /**
     * Player to whom this piece belongs
     */
    protected Player owner;

    /**
     * Whether or not this is the piece's first move
     */
    protected boolean firstMove;

    /**
     * location of the piece on the board
     */
    protected Point location;

    /**
     * Constructor.
     * 
     * On creation of the piece, set first move to true.
     */
    public Chessman() {
        firstMove = true;
    }

    /**
     * Constructor.
     * 
     * Allows user to specify that the piece belongs to a certain player.
     * 
     * @param p Player this piece belongs to.
     */
    public Chessman(Player p) {
        owner = p;
        firstMove = true;
    }

    public UUID getID() {
        return uuid;
    }

    public void setID(UUID n) {
        uuid = n;
    }

    public Player getOwner() {
        return owner;
    }

    public int getX() {
        return (int) getLoc().getX();
    }

    public int getY() {
        return (int) getLoc().getY();
    }

    public Point getLoc() {
        return location;
    }

    public void setLoc(Point p) {
        location = p;
    }

    public Board getBoard() {
        return theBoard;
    }

    public boolean firstMove() {
        return firstMove;
    }

    public void setBoard(Board b) {
        theBoard = b;
    }

    /**
     * Moves the piece to (nx, ny) without concern for whether it is a 
     * valid move.
     * @param nx Horizontal co-ordinate
     * @param ny Vertical co-ordinate
     */
    public void forceMove(int nx, int ny) {
        forceMove(new Point(nx, ny));
    }

    /**
     * Moves the piece to the specified points without concern for whether
     * it is a valid move.
     * 
     * @param p Point to move to.
     */
    public void forceMove(Point p) {
        theBoard.clearPlace(getLoc());
        theBoard.clearPlace(p);
        theBoard.placeMan(this, p);
        firstMove = false;
    }

    /**
     * Checks if (nx, ny) is a valid move for the piece, and if it is,
     * moves the piece to that place. Ensures that firstMove becomes false
     * after moving.
     * 
     * @param nx horizontal coordinate
     * @param ny vertical coordinate
     * @return true if move was successful, false on error.
     */
    public boolean move(int nx, int ny) {
        Point p = new Point(nx, ny);
        if (getPotentialMoves().contains(p)) {
            theBoard.clearPlace(p);
            theBoard.placeMan(this, p);
        } else {
            System.err.println("Not a valid move!");
            return false;
        }
        firstMove = false;
        return true;
    }

    /**
     * @return List of all moves the piece can make which are capable of 
     * capturing pieces, and moves that must capture a piece (pawns capture)
     */
    public abstract ArrayList<Point> getAggressiveMoveSet();

    /**
     * @return List of all moves which are strictly pacifist. (Pawn movement)
     */
    public ArrayList<Point> getMoveSet() {
        return new ArrayList<>();
    }

    @Override
    public abstract Object clone();

    /**
     * Gathers potential aggressive and pacifist moves made by a piece,
     * and rejects them if they would end up with the player's own King
     * under attack. 
     * 
     * @return 
     */
    protected ArrayList<Point> getPotentialMoves() {
        ArrayList<Point> list = new ArrayList<>();
        ArrayList<Point> rejected = new ArrayList<>();
        Board tempBoard;
        Chessman toMove;
        King tempKing;

        list.addAll(getAggressiveMoveSet());
        list.addAll(getMoveSet());

        for (Point target : list) {
            tempBoard = new Board(getBoard());
            toMove = tempBoard.getMan(getLoc());
            toMove.forceMove(target);

            tempKing = getOwner().getKing();
            if (tempKing == null) {
                System.err.println("Where did my king go?? This is bad.");
                System.exit(1);
            }
            if (tempBoard.isAttacked(tempKing.getLoc(), getOwner())) {
                rejected.add(target);
            }
        }
        list.removeAll(rejected);
        return list;
    }

    /**
     * @return True if the piece is on the board, false if not.
     */
    public boolean isAlive() {
        if (getLoc() == theBoard.getShadowRealm()) {
            return false;
        }
        return true;
    }

    /**
     * Resets all flags for a piece.
     */
    public void resetFlags() {
    }

    @Override
    public abstract String toString();
}
