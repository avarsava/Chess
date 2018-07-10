package chess;

import java.awt.Point;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-26
 */
public class Board {

    private final Point SHADOW_REALM;
    private int boardSize;
    private ArrayList<Chessman> chessmen;
    private Chess theChess;

    /**
     * Creates a new board with default settings
     *
     * @param c game of Chess associated with board
     */
    public Board(Chess c) {
        boardSize = 8;
        chessmen = new ArrayList<>();
        SHADOW_REALM = new Point(-1, -1);
        theChess = c;
    }

    /**
     * This constructor is used for debugging purposes.
     *
     * Creates a board with a custom size.
     *
     * @param size resulting board will be size * size
     */
    public Board(int size) {
        boardSize = size;
        chessmen = new ArrayList<>();
        SHADOW_REALM = new Point(-1, -1);
    }

    /**
     * Clones a board.
     *
     * @param b board to clone
     */
    public Board(Board b) {
        boardSize = b.boardSize;
        SHADOW_REALM = b.SHADOW_REALM;
        chessmen = new ArrayList<>();
        for (Chessman c : b.getChessmen()) {
            Chessman n = (Chessman) c.clone();
            n.setBoard(this);
            chessmen.add(n);
        }
    }

    /**
     * Chess getter.
     *
     * @return chess
     */
    public Chess getChess() {
        return theChess;
    }

    /**
     * The SHADOW_REALM constant is the location of any piece removed from the
     * board.
     *
     * @return SHADOW_REALM
     */
    public Point getShadowRealm() {
        return SHADOW_REALM;
    }

    /**
     * Gets all the pieces on the board.
     *
     * @return all pieces on the board.
     */
    public ArrayList<Chessman> getChessmen() {
        return chessmen;
    }

    /**
     * Gets a chess piece with a particular UUID
     *
     * @param id UUID to search for
     * @return chessman with matching UUID
     */
    public Chessman getManFromUUID(UUID id) {
        for (Chessman c : getChessmen()) {
            if (c.getID() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * Adds a piece to the game after assigning it a UUID.
     *
     * @param m man to add
     * @param p location on which to add it
     */
    public void addMan(Chessman m, Point p) {
        m.setID(UUID.randomUUID());
        placeMan(m, p);
    }

    /**
     * Places chessman on board
     *
     * @param m man to add
     * @param p location on which to add it
     */
    public void placeMan(Chessman m, Point p) {
        m.setBoard(this);
        m.setLoc(p);
        if (!getChessmen().contains(m)) {
            getChessmen().add(m);
        }

    }

    /**
     * Gets the piece at (x, y) on the board.
     *
     * @param x horizontal co-ordinate
     * @param y vertical co-ordinate
     * @return chessman if found, null otherwise
     */
    public Chessman getMan(int x, int y) {
        return getMan(new Point(x, y));
    }

    /**
     * Gets the piece at a point on the board
     *
     * @param p where to get piece
     * @return piece at position if it is there, else null
     */
    public Chessman getMan(Point p) {
        for (Chessman c : getChessmen()) {
            if (c.getLoc().equals(p) && c.isAlive()) {
                return c;
            }
        }
        return null;
    }

    /**
     * Returns all chess pieces on the board belonging to a player
     *
     * @param p player whose pieces we should find
     * @return list of chess pieces
     */
    public ArrayList<Chessman> getMenForPlayer(Player p) {
        ArrayList<Chessman> list = new ArrayList<>();
        for (Chessman c : chessmen) {
            if (c.getOwner() == p) {
                list.add(c);
            }
        }
        return list;
    }

    /**
     * Returns the highest value on the board
     *
     * @return size of board, 0 based
     */
    public int getMax() {
        return boardSize - 1;
    }

    /**
     * Calculate whether a spot could be taken by any enemy pieces
     *
     * @param x horizontal co-ordinate
     * @param y vertical co-ordinate
     * @param defender player we are checking if they are being attacked
     * @return true if being attacked, false if not
     */
    public boolean isAttacked(int x, int y, Player defender) {
        Point p = new Point(x, y);
        return isAttacked(p, defender);
    }

    /**
     * Calculate whether a spot could be taken by any enemy pieces
     *
     * @param p position to evaluate
     * @param defender player we are checking if they are being attacked
     * @return true if being attacked, false if not
     */
    public boolean isAttacked(Point p, Player defender) {
        ArrayList<Point> spaces = new ArrayList();
        for (Chessman man : getChessmen()) {
            if (man.getOwner() != defender) {
                spaces.addAll(man.getAggressiveMoveSet());
                if (spaces.contains(p)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Terminal test, checks for checkmates and stalemates
     *
     * @param activePlayer player who would be under checkmate
     * @param verbose whether or not to print messages
     * @return true if board is in ending state
     */
    public boolean endCheck(Player activePlayer, boolean verbose) {
        if (isCheckmate(activePlayer)) {
            if (verbose) {
                System.out.println(activePlayer.getColour()
                        + " player is checkmated! GAME OVER");
            }
            return true;
        }
        if (isStalemate(activePlayer)) {
            if (verbose) {
                System.out.println("Stalemate, " + activePlayer.getColour()
                        + " has no more valid moves!");
            }
            return true;
        }
        return false;
    }

    private boolean isCheckmate(Player activePlayer) {
        King k = activePlayer.getKing();
        boolean checkmate = false;
        ArrayList<Point> moves = new ArrayList<>();

        //if king is attacked
        if (isAttacked(k.getLoc(), activePlayer)) {
            //and activePlayer has no valid moves
            for (Chessman c : getMenForPlayer(activePlayer)) {
                moves.addAll(c.getPotentialMoves());
            }
            if (moves.isEmpty()) {
                checkmate = true;
            }
        }

        return checkmate;
    }

    //returns true if Player has no legal moves
    private boolean isStalemate(Player activePlayer) {
        ArrayList<Point> list = new ArrayList<>();

        for (Chessman m : getMenForPlayer(activePlayer)) {
            if (m.isAlive()) {
                list.addAll(m.getPotentialMoves());
            }
        }

        return list.isEmpty();
    }

    /**
     * Prints the board.
     */
    public void printBoard() {
        for (int i = 0; i < boardSize; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < boardSize; j++) {
                Chessman m = getMan(j, i);
                if (m == null) {
                    System.out.print('-');
                } else {
                    System.out.print(m.toString());
                }
                System.out.print(' ');
            }
            System.out.println();

        }
        System.out.print(" ");
        for (int k = 0; k < boardSize; k++) {
            System.out.print(" " + k);
        }
        System.out.println();
    }

    /**
     * Gets a piece at (x, y) and moves it off the board.
     * @param x horizontal co-ordinate
     * @param y vertical co-ordinate
     */
    public void clearPlace(int x, int y) {
        clearPlace(new Point(x, y));
    }

    /**
     * Gets a piece at a position and moves it off the board.
     * @param p position to clear
     */
    public void clearPlace(Point p) {
        Chessman m = getMan(p);
        if (m != null) {
            m.setLoc(SHADOW_REALM);
        }
    }

    /**
     * Enacts a board change by finding the right piece by UUID and
     * forcing its movement to its end position.
     * 
     * @param bc board change to enact
     */
    public void moveFromChange(BoardChange bc) {
        Chessman toMove = getManFromUUID(bc.getChessman().getID());
        toMove.forceMove(bc.getEnd());
    }
}
