package chess;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-28
 */
public class AIPlayer extends Player {

    int maxPly;
    Player opponent;
    BoardChange toMake;
    static BufferedReader reader;

    /**
     * Construct an AI Player
     *
     * prompts the user to choose a ply
     *
     * @param c Colour to assign to this player
     * @param b Board on which this players pieces go
     */
    public AIPlayer(COLOURS c, Board b) {
        super(c, b);
        String s = "";
        reader = new BufferedReader(new InputStreamReader(System.in));
        for (Player p : theBoard.getChess().getPlayers()) {
            if (p != this) {
                opponent = p;
            }
        }
        System.out.println("Please enter the search tree depth.");
        System.out.print("> ");
        do {
            try {
                s = reader.readLine();
                maxPly = Integer.parseInt(s);
            } catch (Exception ex) {
                System.err.println("Please enter a number.");
                continue;
            }
        } while (s.equals(""));
    }

    /**
     * Initiates alpha-beta search and makes move
     *
     * Based on algorithm found in Russell & Norvig, 2010 (3e)
     *
     * @return BoardChange to add to history
     */
    @Override
    public BoardChange movePiece() {
        toMake = null;
        double v = Double.NEGATIVE_INFINITY;
        double w;
        BoardState currentState = new BoardState(theBoard, null, this);
        BoardChange result = null;

        //iterate through possible moves in order to be able to select one
        //to carry out on the board
        for (BoardChange bc : actions(currentState)) {
            w = minValue(new BoardState(currentState.getState(), bc, opponent),
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
            if (w > v) {
                result = bc;
                v = w;
            }
        }
        if (result == null) {
            System.err.println("MovePiece never could make a decision.");
            return null;
        }
        theBoard.moveFromChange(result);

        return result;
    }

    private double maxValue(BoardState s, double a, double b, int d) {
        double v;
        if (cutoffTest(s, d)) {
            return utility(s);
        }
        v = Double.NEGATIVE_INFINITY;

        for (BoardChange bc : actions(s)) {
            v = Math.max(v, minValue(result(s, bc), a, b, d + 1));
            if (v >= b) {
                toMake = bc;
                return v;
            }
            a = Math.max(a, v);
        }
        return v;
    }

    private double minValue(BoardState s, double a, double b, int d) {
        double v;
        if (cutoffTest(s, d)) {
            return utility(s);
        }
        v = Double.POSITIVE_INFINITY;

        for (BoardChange bc : actions(s)) {
            v = Math.min(v, maxValue(result(s, bc), a, b, d + 1));
            if (v <= a) {
                toMake = bc;
                return v;
            }
            b = Math.min(b, v);
        }
        return v;
    }

    /**
     * Cuts off a branch if a terminal node is found
     *
     * Terminal nodes may be either a winning condition, or the leaf of a branch
     * having hit the maximum ply.
     *
     * @param s state to test
     * @param depth depth on the tree
     * @return whether or not this is a terminal node
     */
    private boolean cutoffTest(BoardState s, int depth) {
        if (depth >= maxPly) {
            return true;
        }
        if (s.getState().endCheck(this, false)
                || s.getState().endCheck(opponent, false)) {
            return true;
        }
        return false;
    }

    /**
     * Creates a collection of actions that could be carried out by the
     * opponent.
     *
     * @param s state to test
     * @return list of board changes (actions)
     */
    private ArrayList<BoardChange> actions(BoardState s) {
        Player otherPlayer = s.getPlayer() == this ? opponent : this;
        ArrayList<BoardChange> result = new ArrayList<>();
        ArrayList<Chessman> enemyPieces = new ArrayList<>();
        enemyPieces.addAll(s.getState().getChessmen());
        enemyPieces.removeAll(s.getState().getMenForPlayer(otherPlayer));

        for (Chessman c : enemyPieces) {
            if (!c.isAlive()) {
                continue;
            }
            for (Point p : c.getPotentialMoves()) {
                result.add(s.createChange(c, p));
            }
        }

        return result;
    }

    /**
     * Creates a new board state in which an action occurs.
     *
     * @param bs starting state
     * @param bc action to apply
     * @return new state
     */
    private BoardState result(BoardState bs, BoardChange bc) {
        Player otherPlayer = bs.getPlayer() == this ? opponent : this;
        return new BoardState(bs.getState(), bc, otherPlayer);
    }

    /**
     * Based on Shannon 1949 via Levy 1983.
     *
     * Estimates the relative value of a chess board by assigning a weight to
     * the difference in amounts of pieces between players, as well as certain
     * pawn arrangements
     *
     * @param s state to evaluate
     * @return relative utility score
     */
    private double utility(BoardState s) {
        return 200 * (count(s, King.class, this)
                     - count(s, King.class, opponent))
                + 9 * (count(s, Queen.class, this)
                        - count(s, Queen.class, opponent))
                + 5 * (count(s, Rook.class, this)
                        - count(s, Rook.class, opponent))
                + 3 * (count(s, Bishop.class, this)
                        - count(s, Bishop.class, opponent)
                + count(s, Knight.class, this)
                        - count(s, Knight.class, opponent))
                + (count(s, Pawn.class, this)
                        - count(s, Pawn.class, opponent))
                - 0.5 * (doubledPawns(s, this)
                        - doubledPawns(s, opponent)
                + isolatedPawns(s, this)
                        - isolatedPawns(s, opponent))
                + 0.1 * (mobility(s, this)
                        - mobility(s, opponent));
    }

    private int count(BoardState s, Class<? extends Chessman> t, Player p) {
        int sum = 0;
        for (Chessman c : s.getState().getChessmen()) {
            if (t.isInstance(c) && c.getOwner() == p
                    && c.isAlive()) {
                sum++;
            }
        }
        return sum;
    }

    /**
     * Counts the number of pawns of same colour on same file.
     *
     * @param s state to evaluate
     * @param p player whose pawns to count
     * @return number of doubled pawns
     */
    private int doubledPawns(BoardState s, Player p) {
        int result = 0, fileSum;
        Board tempBoard = s.getState();
        Chessman c;

        for (int i = 0; i <= tempBoard.getMax(); i++) {
            fileSum = 0;

            for (int j = 0; j <= tempBoard.getMax(); j++) {
                c = tempBoard.getMan(i, j);
                if (c instanceof Pawn && c.getOwner() == p) {
                    fileSum++;
                }
            }

            if (fileSum > 1) {
                result += fileSum;
            }
        }

        return result;
    }

    /**
     * Counts number of pawns with no friendly neighbour pawns.
     *
     * @param s state to evaluate
     * @param p player whose pawns we should count
     * @return number of isolated pawns
     */
    private int isolatedPawns(BoardState s, Player p) {
        int result = 0;
        boolean inc;
        Chessman b = null;
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

        for (Chessman c : s.getState().getMenForPlayer(p)) {
            if (c instanceof Pawn) {
                inc = true;
                for (Point a : offsets) {
                    b = s.getState().getMan((int) c.getX() + (int) a.getX(),
                            (int) c.getY() + (int) a.getY());
                    if (b instanceof Pawn && b.getOwner() == p) {
                        inc = false;
                        break;
                    }
                }
                if (inc) {
                    result++;
                }
            }
        }

        return result;
    }

    /**
     * Counts total number of moves a player has at their disposal for a given
     * board state.
     *
     * @param s state to evaluate
     * @param pl player whose moves we should be counting
     * @return number of potential moves
     */
    private int mobility(BoardState s, Player pl) {
        int ret = 0;
        ArrayList<Chessman> pieces = s.getState().getMenForPlayer(pl);

        for (Chessman c : pieces) {
            if (!c.isAlive()) {
                continue;
            }
            for (Point p : c.getPotentialMoves()) {
                ret++;
            }
        }

        return ret;
    }

    /**
     * Currently, AI always queens pawns
     * 
     * @param p pawn to promote
     * @return new queen
     */
    @Override
    public Chessman promotePawn(Pawn p) {
        return new Queen(p);
    }
}
