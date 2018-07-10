package chess;

import java.awt.Point;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-28
 */

public abstract class Player {

    protected COLOURS colour;
    protected Board theBoard;

    /**
     * Create a player
     * 
     * Assigns the player a colour and records the board to which 
     * their pieces belong
     * 
     * @param c The colour of the player's pieces
     * @param b the board associated with the player
     */
    public Player(COLOURS c, Board b) {
        colour = c;
        theBoard = b;
    }

    public COLOURS getColour() {
        return colour;
    }

    /**
     * Resets all flags for all this player's pieces
     * 
     * This is used primarily to make en passant work.
     */
    public void resetFlags() {
        for (Chessman c : theBoard.getMenForPlayer(this)) {
            c.resetFlags();
        }
    }

    /**
     * Assuming there is one King per player, return the player's King.
     * @return this player's king
     */
    public King getKing() {
        for (Chessman c : theBoard.getMenForPlayer(this)) {
            if (c instanceof King) {
                return (King) c;
            }
        }
        return null;
    }

    /**
     * @return a vector pointing towards the opposite side of the board 
     */
    public Point getUp() {
        if (getColour() == COLOURS.white) {
            return new Point(0, -1);
        }
        return new Point(0, 1);
    }
    
    
    /**
     * @return 0 if white player, max rank if black
     */
    public int getOppositeSide() {
        if (getColour() == COLOURS.white) {
            return 0;
        }
        return theBoard.getMax();
    }

    /**
     * Provides method for moving a chessman on the board
     * 
     * @return if successful, returns a board change noting the movement 
     */
    public abstract BoardChange movePiece();

    /**
     * Provides method for promoting a Pawn.
     * 
     * @param p pawn to promote
     * @return the chessman introduced to replace the pawn.
     */
    public abstract Chessman promotePawn(Pawn p);
}
