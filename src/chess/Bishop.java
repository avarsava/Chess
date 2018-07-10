package chess;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-26
 */
public class Bishop extends Chessman {

    public Bishop() {
        super();
    }

    public Bishop(Player p) {
        super(p);
    }


    public Bishop(Bishop b) {
        owner = b.getOwner();
        theBoard = null;
        firstMove = b.firstMove();
        location = b.getLoc();
        uuid = b.uuid;
    }


    public Bishop(Chessman c) {
        owner = c.getOwner();
        theBoard = c.getBoard();
        firstMove = false;
        location = c.getLoc();
    }

    @Override
    public Object clone() {
        return new Bishop(this);
    }

    @Override
    public ArrayList<Point> getAggressiveMoveSet() {
        ArrayList<Point> list = new ArrayList();
        if (getLoc() == theBoard.getShadowRealm()) {
            return list;
        }
        int x = getLoc().x, y = getLoc().y;

        //up, left
        while (x > 0 && x <= theBoard.getMax() 
                && y > 0 && y <= theBoard.getMax()) {
            x--;
            y--;
            if (theBoard.getMan(x, y) == null) {
                list.add(new Point(x, y));
            } else {
                if (theBoard.getMan(x, y).getOwner() != owner) {
                    list.add(new Point(x, y));
                }
                break;
            }
        }

        //up, right
        x = getLoc().x;
        y = getLoc().y;
        while (x >= 0 && x < theBoard.getMax() && y > 0 
                && y <= theBoard.getMax()) {
            x++;
            y--;
            if (theBoard.getMan(x, y) == null) {
                list.add(new Point(x, y));
            } else {
                if (theBoard.getMan(x, y).getOwner() != owner) {
                    list.add(new Point(x, y));
                }
                break;
            }
        }

        //down, left
        x = getLoc().x;
        y = getLoc().y;
        while (x > 0 && x <= theBoard.getMax() && y >= 0 
                && y < theBoard.getMax()) {
            x--;
            y++;
            if (theBoard.getMan(x, y) == null) {
                list.add(new Point(x, y));
            } else {
                if (theBoard.getMan(x, y).getOwner() != owner) {
                    list.add(new Point(x, y));
                }
                break;
            }
        }

        //down, right
        x = getLoc().x;
        y = getLoc().y;
        while (x >= 0 && x < theBoard.getMax() 
                && y >= 0 && y < theBoard.getMax()) {
            x++;
            y++;
            if (theBoard.getMan(x, y) == null) {
                list.add(new Point(x, y));
            } else {
                if (theBoard.getMan(x, y).getOwner() != owner) {
                    list.add(new Point(x, y));
                }
                break;
            }
        }

        return list;
    }

    @Override
    public String toString() {
        if (owner.getColour() == COLOURS.white) {
            return "b";
        } else {
            return "B";
        }
    }
}
