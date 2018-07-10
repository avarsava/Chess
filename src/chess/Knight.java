package chess;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-26
 */

public class Knight extends Chessman {

    public Knight() {
        super();
    }

    public Knight(Player p) {
        super(p);
    }

    public Knight(Knight k) {
        owner = k.getOwner();
        theBoard = null;
        firstMove = k.firstMove();
        location = k.getLoc();
        uuid = k.uuid;
    }

    public Knight(Chessman c) {
        owner = c.getOwner();
        theBoard = c.getBoard();
        firstMove = false;
        location = c.getLoc();
        uuid = c.uuid;
    }

    @Override
    public Object clone() {
        return new Knight(this);
    }

    @Override
    public ArrayList<Point> getAggressiveMoveSet() {
        ArrayList<Point> list = new ArrayList();
        if (getLoc() == theBoard.getShadowRealm()) {
            return list;
        }
        int x = getLoc().x, y = getLoc().y;

        if (x + 1 <= theBoard.getMax() && y - 2 >= 0) {
            if (theBoard.getMan(x + 1, y - 2) == null 
                    || theBoard.getMan(x + 1, y - 2).getOwner() != owner) {
                list.add(new Point(x + 1, y - 2));
            }
        }

        if (x + 2 <= theBoard.getMax() && y - 1 >= 0) {
            if (theBoard.getMan(x + 2, y - 1) == null 
                    || theBoard.getMan(x + 2, y - 1).getOwner() != owner) {
                list.add(new Point(x + 2, y - 1));
            }
        }

        if (x + 2 <= theBoard.getMax() && y + 1 <= theBoard.getMax()) {
            if (theBoard.getMan(x + 2, y + 1) == null 
                    || theBoard.getMan(x + 2, y + 1).getOwner() != owner) {
                list.add(new Point(x + 2, y + 1));
            }
        }

        if (x + 1 <= theBoard.getMax() && y + 2 <= theBoard.getMax()) {
            if (theBoard.getMan(x + 1, y + 2) == null 
                    || theBoard.getMan(x + 1, y + 2).getOwner() != owner) {
                list.add(new Point(x + 1, y + 2));
            }
        }

        if (x - 1 >= 0 && y + 2 <= theBoard.getMax()) {
            if (theBoard.getMan(x - 1, y + 2) == null 
                    || theBoard.getMan(x - 1, y + 2).getOwner() != owner) {
                list.add(new Point(x - 1, y + 2));
            }
        }

        if (x - 2 >= 0 && y + 1 <= theBoard.getMax()) {
            if (theBoard.getMan(x - 2, y + 1) == null 
                    || theBoard.getMan(x - 2, y + 1).getOwner() != owner) {
                list.add(new Point(x - 2, y + 1));
            }
        }

        if (x - 2 >= 0 && y - 1 >= 0) {
            if (theBoard.getMan(x - 2, y - 1) == null 
                    || theBoard.getMan(x - 2, y - 1).getOwner() != owner) {
                list.add(new Point(x - 2, y - 1));
            }
        }

        if (x - 1 >= 0 && y - 2 >= 0) {
            if (theBoard.getMan(x - 1, y - 2) == null 
                    || theBoard.getMan(x - 1, y - 2).getOwner() != owner) {
                list.add(new Point(x - 1, y - 2));
            }
        }

        return list;
    }

    @Override
    public String toString() {
        if (owner.getColour() == COLOURS.white) {
            return "n";
        } else {
            return "N";
        }
    }
}
