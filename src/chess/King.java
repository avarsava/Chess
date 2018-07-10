package chess;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-26
 */

public class King extends Chessman {

    public King() {
        super();
    }

    public King(Player p) {
        super(p);
    }

    public King(King k) {
        owner = k.getOwner();
        theBoard = null;
        firstMove = k.firstMove();
        location = k.getLoc();
        uuid = k.uuid;
    }

    @Override
    public Object clone() {
        return new King(this);
    }

    @Override
    public ArrayList<Point> getAggressiveMoveSet() {
        ArrayList<Point> list = new ArrayList();
        if (getLoc() == theBoard.getShadowRealm()) {
            return list;
        }
        int x = getLoc().x, y = getLoc().y;

        Point[] offsets = {
            new Point(-1, -1),
            new Point(0, -1),
            new Point(+1, -1),
            new Point(+1, 0),
            new Point(+1, +1),
            new Point(0, +1),
            new Point(-1, +1),
            new Point(-1, 0)
        };

        for (Point p : offsets) {
            if (x + p.x >= 0 && x + p.x <= theBoard.getMax()
                    && y + p.y >= 0 && y + p.y <= theBoard.getMax()) {
                if (theBoard.getMan(x + p.x, y + p.y) == null
                        || theBoard.getMan(x + p.x, y + p.y).getOwner() 
                            != owner) {
                    list.add(new Point(x + p.x, y + p.y));
                }
            }
        }

        return list;
    }

    @Override
    public ArrayList<Point> getMoveSet() {
        int x, y = getLoc().y;
        Chessman c;
        ArrayList<Point> list = new ArrayList();
        if (getLoc() == theBoard.getShadowRealm()) {
            return list;
        }
        //check for castling
        if (firstMove && (y == 0 || y == theBoard.getMax())
                && !theBoard.isAttacked(getLoc(), owner)) {
            x = getLoc().x;
            y = getLoc().y;
            //Castling to the right
            if (x + 1 <= theBoard.getMax()
                    && x + 2 <= theBoard.getMax()
                    && theBoard.getMan(x + 1, y) == null
                    && theBoard.getMan(x + 2, y) == null) {
                c = null;
                for (int i = x + 1; i <= theBoard.getMax(); i++) {
                    if (theBoard.isAttacked(i, y, owner)) {
                        break;
                    }
                    c = theBoard.getMan(i, y);
                    if (c != null) {
                        break;
                    }
                }
                if (c instanceof Rook && c.getOwner() == owner
                        && c.firstMove()) {
                    list.add(new Point(x + 2, y));
                }
            }

            //Castling to the left
            if (x - 1 >= 0
                    && x - 2 >= 0
                    && theBoard.getMan(x - 1, y) == null
                    && theBoard.getMan(x - 2, y) == null) {
                c = null;
                for (int i = x - 1; i >= 0; i--) {
                    if (theBoard.isAttacked(i, y, owner)) {
                        break;
                    }
                    c = theBoard.getMan(i, y);
                    if (c != null) {
                        break;
                    }
                }
                if (c instanceof Rook && c.getOwner() == owner
                        && c.firstMove()) {
                    list.add(new Point(x - 2, y));
                }
            }
        }
        return list;
    }

    @Override
    public boolean move(int nx, int ny) {
        Point oldLoc = getLoc();
        Rook toMove;
        //provided the King was able to move
        if (super.move(nx, ny)) {
            //if castling to the right 
            if (nx - oldLoc.getX() == 2) {
                //safe to assume that the Rook will be there,
                //rules require that the piece has not yet moved
                toMove = (Rook) theBoard.getMan(0, ny);
                toMove.forceMove(nx + 1, ny);

                //if castling to the left
            } else if (nx - oldLoc.getX() == -2) {
                //same deal
                toMove = (Rook) theBoard.getMan(theBoard.getMax(), ny);
                toMove.forceMove(nx - 1, ny);
            }
            //whether we castled or not, if super.move() worked, we did move
            return true;
        }
        //if super.move() failed, then we didn't move
        return false;
    }

    @Override
    public String toString() {
        if (owner.getColour() == COLOURS.white) {
            return "k";
        } else {
            return "K";
        }
    }
}
