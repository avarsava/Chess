package chess;

import java.awt.Point;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-28
 */
public class BoardChange {

    private Chessman piece;
    private Point prevLoc;
    private Point curLoc;

    public BoardChange(Chessman c) {
        piece = c;
        prevLoc = (Point) piece.getLoc().clone();
    }

    public void setEnd(Point e) {
        curLoc = e;
    }

    public Chessman getChessman() {
        return piece;
    }

    public Point getEnd() {
        return curLoc;
    }

    /**
     * @return The difference on the x-axis between the two 
     */
        public int getXDiff() {
        return (int) (curLoc.getX() - prevLoc.getX());
    }

    /**
     *
     * @return The difference on the y-axis between the two
     */
    public int getYDiff() {
        return (int) (curLoc.getY() - prevLoc.getY());
    }
}
