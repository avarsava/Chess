package chess;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-26
 */
public class Pawn extends Chessman {

    //whether this piece may be captured via en passant in the coming turn
    private boolean enPassant;

    public Pawn() {
        super();
        enPassant = false;
    }

    public Pawn(Player p) {
        super(p);
        enPassant = false;
    }

    public Pawn(Pawn p) {
        owner = p.getOwner();
        theBoard = null;
        firstMove = p.firstMove();
        location = p.getLoc();
        enPassant = p.enPassantPossible();
        uuid = p.uuid;
    }

    @Override
    public Object clone() {
        return new Pawn(this);
    }


    public boolean enPassantPossible() {
        return enPassant;
    }

    public void enableEnPassant() {
        enPassant = true;
    }

    public void disableEnPassant() {
        enPassant = false;
    }

    @Override
    public ArrayList<Point> getAggressiveMoveSet() {
        ArrayList<Point> list = new ArrayList<>();
        if (getLoc() == theBoard.getShadowRealm()) {
            return list;
        }
        Pawn p;
        Point up;

        //if there are enemy pieces on the diagonal
        //white pieces look up
        if (owner.getColour() == COLOURS.white) {
            //look to the upper left
            if (theBoard.getMan(getX() - 1, getY() - 1) != null
                    && theBoard.getMan(getX() - 1, getY() - 1).getOwner() 
                    != getOwner()) {
                list.add(new Point(getX() - 1, getY() - 1));
            }

            //look to the upper right
            if (theBoard.getMan(getX() + 1, getY() - 1) != null
                    && theBoard.getMan(getX() + 1, getY() - 1).getOwner() 
                    != getOwner()) {
                list.add(new Point(getX() + 1, getY() - 1));
            }

            //black pieces look down
        } else {
            //look to the lower left
            if (theBoard.getMan(getX() - 1, getY() + 1) != null
                    && theBoard.getMan(getX() - 1, getY() + 1).getOwner() 
                    != getOwner()) {
                list.add(new Point(getX() - 1, getY() + 1));
            }

            //look to the lower right
            if (theBoard.getMan(getX() + 1, getY() + 1) != null
                    && theBoard.getMan(getX() + 1, getY() + 1).getOwner() 
                    != getOwner()) {
                list.add(new Point(getX() + 1, getY() + 1));
            }
        }

        //all pieces look to the sides for pawns to capture via en passant
        //look left
        if (theBoard.getMan(getX() - 1, getY()) instanceof Pawn
                && theBoard.getMan(getX() - 1, 
                        getY()).getOwner() != getOwner()) {
            p = (Pawn) theBoard.getMan(getX() - 1, getY());
            if (p.enPassantPossible()) {
                up = getOwner().getUp();
                list.add(new Point((int) (p.getLoc().getX() + up.getX()),
                        (int) (p.getLoc().getY() + up.getY())));
            }
        }

        //look right
        if (theBoard.getMan(getX() + 1, getY()) instanceof Pawn
                && theBoard.getMan(getX() + 1, 
                        getY()).getOwner() != getOwner()) {
            p = (Pawn) theBoard.getMan(getX() + 1, getY());
            if (p.enPassantPossible()) {
                up = getOwner().getUp();
                list.add(new Point((int) (p.getLoc().getX() + up.getX()),
                        (int) (p.getLoc().getY() + up.getY())));
            }
        }

        return list;
    }

    @Override
    public ArrayList<Point> getMoveSet() {
        ArrayList<Point> list = new ArrayList();
        if (getLoc() == theBoard.getShadowRealm()) {
            return list;
        }
        if (owner.getColour() == COLOURS.white) {
            Point p = new Point(getLoc().x, getLoc().y - 1);
            if (p.y >= 0 && theBoard.getMan(p) == null) {
                list.add(p);
                if (firstMove) {
                    Point p2 = new Point(getLoc().x, getLoc().y - 2);
                    if (p2.y >= 0 && theBoard.getMan(p2) == null) {
                        list.add(p2);
                    }
                }
            }
        } else {
            Point p = new Point(getLoc().x, getLoc().y + 1);
            if (p.y <= theBoard.getMax() && theBoard.getMan(p) == null) {
                list.add(p);
                if (firstMove) {
                    Point p2 = new Point(getLoc().x, getLoc().y + 2);
                    if (p2.y <= theBoard.getMax() 
                            && theBoard.getMan(p2) == null) {
                        list.add(p2);
                    }
                }
            }
        }

        return list;
    }


    /**
     * Standard move, plus logic for en passant 
     * 
     * @param nx new horizontal coordinate
     * @param ny new vertical coordinate
     * @return whether or not move was successful
     */
    @Override
    public boolean move(int nx, int ny) {
        Point oldLoc = getLoc(), up;
        Chessman replacement;

        if (super.move(nx, ny)) {
            //if we moved forward 2, this pawn can be captured via en passant
            if (Math.abs(ny - oldLoc.getY()) == 2) {
                enableEnPassant();
            }

            //if we moved diagonally & enPassant was possible 
            //(ie we made an en passant capture)
            //capture the pawn we moved around
            //it's safe to assume it was a pawn, due to the check in
            //getAggressiveMoveSet()
            if (Math.abs(nx - oldLoc.getX()) == 1
                    && Math.abs(ny - oldLoc.getY()) == 1) {
                up = getOwner().getUp();
                theBoard.clearPlace((int) (nx + up.getX()),
                        (int) (ny - up.getY()));
            }

            //if we have reached the other side, promote ourself
            if (getLoc().getY() == getOwner().getOppositeSide()) {
                replacement = getOwner().promotePawn(this);
                theBoard.clearPlace(nx, ny);
                theBoard.placeMan(replacement, new Point(nx, ny));
            }

            return true;
        }
        return false;
    }

    /**
     * Resets the en passant flag.
     */
    @Override
    public void resetFlags() {
        disableEnPassant();
    }

    @Override
    public String toString() {
        if (owner.getColour() == COLOURS.white) {
            return "p";
        } else {
            return "P";
        }
    }
}
