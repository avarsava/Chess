package chess;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-26
 */

public class Queen extends Chessman {

    public Queen() {
        super();
    }

    public Queen(Player p) {
        super(p);
    }

    public Queen(Queen q) {
        owner = q.getOwner();
        theBoard = null;
        firstMove = q.firstMove();
        location = q.getLoc();
        uuid = q.uuid;
    }

    public Queen(Chessman c) {
        owner = c.getOwner();
        theBoard = c.getBoard();
        firstMove = false;
        location = c.getLoc();
    }

    @Override
    public Object clone() {
        return new Queen(this);
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

        //up, left
        x = getLoc().x;
        y = getLoc().y;
        while (x > 0 && x <= theBoard.getMax() && y > 0 && y <= theBoard.getMax()) {
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
        while (x >= 0 && x < theBoard.getMax() && y > 0 && y <= theBoard.getMax()) {
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
        while (x > 0 && x <= theBoard.getMax() && y >= 0 && y < theBoard.getMax()) {
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
        while (x >= 0 && x < theBoard.getMax() && y >= 0 && y < theBoard.getMax()) {
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
            return "q";
        } else {
            return "Q";
        }
    }
}
