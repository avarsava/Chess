package chess;

import java.awt.Point;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-28
 */

public class BoardState {

    private Board state;
    private BoardChange change;
    private Player activePlayer;

    public BoardState(Board b, BoardChange c, Player p) {
        state = new Board(b); //cloning b
        change = c;
        activePlayer = p;

        //If we provided a change, act on it.
        if (change != null) {
            state.moveFromChange(c);
        }
    }

    public Board getState() {
        return state;
    }

    public Player getPlayer() {
        return activePlayer;
    }

    /**
     * Creates a BoardChange given a Chessman and a Point.
     * 
     * @param c Chessman to be subject of BoardChange
     * @param p ending Point for action
     * @return new BoardChange.
     */
    public BoardChange createChange(Chessman c, Point p) {
        BoardChange b = new BoardChange(c);
        b.setEnd(p);
        return b;
    }
}
