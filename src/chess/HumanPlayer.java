package chess;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-28
 */

public class HumanPlayer extends Player {

    static BufferedReader reader;

    public HumanPlayer(COLOURS c, Board b) {
        super(c, b);
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Provides text-based interface for user to move pieces.
     * 
     * @return Board change of action taken 
     */
    @Override
    public BoardChange movePiece() {
        BoardChange c;
        int x, y;
        String s;
        Chessman pieceToMove = null;
        ArrayList<Point> list = null;
        boolean result = false;

        do {
            System.out.println("Please enter the piece you'd like to move");

            System.out.print("x >> ");
            try {
                s = reader.readLine();
                x = Integer.parseInt(s);
                if (x < 0 || x > theBoard.getMax()) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.err.println("Please enter a valid number.");
                continue;
            }

            System.out.print("y >> ");
            try {
                s = reader.readLine();
                y = Integer.parseInt(s);
                if (y < 0 || y > theBoard.getMax()) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.err.println("Please enter a valid number.");
                continue;
            }

            pieceToMove = theBoard.getMan(x, y);
            if (pieceToMove == null) {
                System.err.println("There's no piece there!");
                continue;
            }

            if (pieceToMove.owner.getColour() != colour) {
                System.err.println("That's not yours to move!");
                pieceToMove = null;
                continue;
            }

            try {
                list = pieceToMove.getPotentialMoves();

                if (list == null || list.isEmpty()) {
                    pieceToMove = null;
                }

            } catch (Exception e) {
                System.err.println("No valid moves??");
                continue;
            }
        } while (pieceToMove == null);

        c = new BoardChange(pieceToMove);

        do {
            System.out.println("Please enter where you'll move to.");
            System.out.println("Valid locations are:");
            for (Point p : list) {
                System.out.print(p.toString() + ", ");
                if (theBoard.isAttacked(p, this)) {
                    System.out.print("(Under attack!) ");
                }
            }
            System.out.println();

            System.out.print("x >> ");
            try {
                s = reader.readLine();
                x = Integer.parseInt(s);
                if (x < 0 || x > theBoard.getMax()) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.err.println("Please enter a valid number.");
                continue;
            }

            System.out.print("y >> ");
            try {
                s = reader.readLine();
                y = Integer.parseInt(s);
                if (y < 0 || y > theBoard.getMax()) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.err.println("Please enter a valid number.");
                continue;
            }

            result = pieceToMove.move(x, y);
        } while (!result);

        c.setEnd(new Point(pieceToMove.getX(), pieceToMove.getY()));

        return c;
    }

    @Override
    public Chessman promotePawn(Pawn p) {
        String s;
        Chessman toReplace = null;

        do {
            System.out.println("Please select which to promote to:");
            System.out.print("(Q, N, R, B)> ");
            try {
                s = reader.readLine();
                switch (s) {
                    case "Q":
                    case "q":
                        toReplace = new Queen(p);
                        break;
                    case "N":
                    case "n":
                        toReplace = new Knight(p);
                        break;
                    case "R":
                    case "r":
                        toReplace = new Rook(p);
                        break;
                    case "B":
                    case "b":
                        toReplace = new Bishop(p);
                        break;
                    default:
                        throw new Exception();
                }
            } catch (Exception ex) {
                System.err.println("Please enter Q, N, R, or B.");
                s = "";
            }
        } while (s.equals(""));

        return toReplace;
    }
}
