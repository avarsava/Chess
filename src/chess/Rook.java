package chess;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-26
 */
public class Rook extends Chessman {


    public Rook() {
        super();
    }

    public Rook(Player p) {
        super(p);
    }

    public Rook(Rook r) {
        owner = r.getOwner();
        theBoard = null;
        firstMove = r.firstMove();
        location = r.getLoc();
        uuid = r.uuid;
    }

    public Rook(Chessman c) {
        owner = c.getOwner();
        theBoard = c.getBoard();
        firstMove = false;
        location = c.getLoc();
    }

    @Override
    public Object clone() {
        return new Rook(this);
    }

    @Override
    public ArrayList<Point> getAggressiveMoveSet() {
        ArrayList<Point> list = new ArrayList();
        if (getLoc() == theBoard.getShadowRealm()) {
            return list;
        }
        int x = getLoc().x, y = getLoc().y;

        //up
        while (y > 0) {
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

        //down
        x = getLoc().x;
        y = getLoc().y;
        while (y < theBoard.getMax()) {
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

        //left
        x = getLoc().x;
        y = getLoc().y;
        while (x < theBoard.getMax()) {
            x++;
            if (theBoard.getMan(x, y) == null) {
                list.add(new Point(x, y));
            } else {
                if (theBoard.getMan(x, y).getOwner() != owner) {
                    list.add(new Point(x, y));
                }
                break;
            }
        }

        //right
        x = getLoc().x;
        y = getLoc().y;
        while (x > 0) {
            x--;
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
            return "r";
        } else {
            return "R";
        }
    }
}
