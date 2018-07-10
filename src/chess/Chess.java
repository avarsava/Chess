package chess;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Alexis Varsava <av11sl@brocku.ca>
 * @version 1
 * @since 2015-12-26
 */

public class Chess {

    final int NUM_PLAYERS;
    static BufferedReader reader;
    Board theBoard;
    ArrayList<Player> players;
    Stack<BoardState> history;

    /**
     * Constructor.
     * 
     * Sets up the game and launches run loop.
     */
    public Chess() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        theBoard = new Board(this);
        this.NUM_PLAYERS = 2;
        players = new ArrayList();
        history = new Stack();
        greeting();
        setUpPlayers();

        //Swap out defaultBoard for any of the test boards if you so desire
        //Do not enable more than one!!
        defaultBoard();
        //checkMateTest();
        //staleMateTest();
        //enPassantTest();
        //promotionTest();
        run();
    }

    private void greeting() {
        System.out.println("Let's Play Chess!");
    }

    /**
     * Allows the player to choose between playing against the AI or against
     * another human.
     */
    public void setUpPlayers() {
        int num = 0;

        do {
            System.out.println("How many human players?");
            System.out.print("(1, 2)> ");

            try {
                num = Integer.parseInt(reader.readLine());
            } catch (Exception ex) {
                System.err.println("Input error!");
            }
            if (num == 1) {
                players.add(new HumanPlayer(COLOURS.white, theBoard));
                players.add(new AIPlayer(COLOURS.black, theBoard));
            } else if (num == 2) {
                players.add(new HumanPlayer(COLOURS.white, theBoard));
                players.add(new HumanPlayer(COLOURS.black, theBoard));
            } else {
                System.err.println("Please enter 1 or 2");
            }
        } while (num != 1 && num != 2);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Alternates between plays, allowing them to move the pieces alternately.
     */
    private void run() {
        boolean run = true;
        BoardState toAdd;
        int idx = 0;
        while (run) {
            Player current = players.get(idx);

            if (current instanceof HumanPlayer) {
                theBoard.printBoard();
            }

            if (theBoard.endCheck(current, true)) {
                break;
            } else {
                current.resetFlags();

                toAdd = new BoardState(theBoard, current.movePiece(), current);
                history.push(toAdd);

                idx = ++idx % players.size();
            }
        }
    }

    //uses magic numbers because the default board never changes
    private void defaultBoard() {
        //White pieces
        for (int i = 0; i <= theBoard.getMax(); i++) {
            theBoard.addMan(new Pawn(players.get(0)), new Point(i, 6));
        }
        theBoard.addMan(new Rook(players.get(0)), new Point(0, 7));
        theBoard.addMan(new Knight(players.get(0)), new Point(1, 7));
        theBoard.addMan(new Bishop(players.get(0)), new Point(2, 7));
        theBoard.addMan(new Queen(players.get(0)), new Point(3, 7));
        theBoard.addMan(new King(players.get(0)), new Point(4, 7));
        theBoard.addMan(new Bishop(players.get(0)), new Point(5, 7));
        theBoard.addMan(new Knight(players.get(0)), new Point(6, 7));
        theBoard.addMan(new Rook(players.get(0)), new Point(7, 7));

        //Black pieces
        for (int i = 0; i <= theBoard.getMax(); i++) {
            theBoard.addMan(new Pawn(players.get(1)), new Point(i, 1));
        }
        theBoard.addMan(new Rook(players.get(1)), new Point(0, 0));
        theBoard.addMan(new Knight(players.get(1)), new Point(1, 0));
        theBoard.addMan(new Bishop(players.get(1)), new Point(2, 0));
        theBoard.addMan(new Queen(players.get(1)), new Point(3, 0));
        theBoard.addMan(new King(players.get(1)), new Point(4, 0));
        theBoard.addMan(new Bishop(players.get(1)), new Point(5, 0));
        theBoard.addMan(new Knight(players.get(1)), new Point(6, 0));
        theBoard.addMan(new Rook(players.get(1)), new Point(7, 0));
    }

    //move white King to left or right to trigger checkmate
    private void checkMateTest() {
        theBoard.addMan(new King(players.get(0)), new Point(2, 2));
        theBoard.addMan(new Queen(players.get(0)), new Point(2, 1));
        theBoard.addMan(new King(players.get(1)), new Point(2, 0));
    }

    //move white King to right to trigger stalemate
    private void staleMateTest() {
        theBoard.addMan(new King(players.get(0)), new Point(4, 1));
        theBoard.addMan(new Queen(players.get(0)), new Point(6, 2));
        theBoard.addMan(new King(players.get(1)), new Point(7, 0));
    }

    //move white Pawn forward 2 and test
    private void enPassantTest() {
        theBoard.addMan(new Pawn(players.get(0)), new Point(0, 6));
        theBoard.addMan(new Pawn(players.get(1)), new Point(1, 4));
        theBoard.addMan(new King(players.get(0)), new Point(7, 7));
        theBoard.addMan(new King(players.get(1)), new Point(0, 0));
    }

    private void promotionTest() {
        theBoard.addMan(new King(players.get(0)), new Point(3, 3));
        theBoard.addMan(new King(players.get(1)), new Point(1, 5));

        theBoard.addMan(new Pawn(players.get(0)), new Point(1, 1));
        theBoard.addMan(new Pawn(players.get(1)), new Point(6, 6));
    }

    /**
     * Main method, creates new chess object, launching game.
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        Chess c = new Chess();
    }

}
