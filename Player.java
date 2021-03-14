import java.util.*;

public class Player {
    //0 - white, 1 - red
    private int side;
    private Triangle [] board;

    public Player(int side) {
        this.side = side;
    }

    public int input(String str, Triangle [] board, List<Integer> possibleMoves, List<Checker> outs) {
        Scanner scan = new Scanner (System.in);
        
        //Can't play a valid move (or doesn't want to)
        if (str.equalsIgnoreCase("no"))
            return 0;
        
        //Getting a piece out of the bar
        boolean open = getPieceOut(str, outs, possibleMoves);

        if (str.equals("get")) {
            if (!open) {
                possibleMoves.remove(0);
                return 0;
            } else if (open) {
                Checker fin = new Checker(side);
                for (Checker c: outs) {
                    if (c.getSide() == side)
                        fin = c;
                }

                outs.remove(fin);
                board[possibleMoves.get(0)].addPiece(fin);
                return 0;
            }
        }
                
        String [] split = str.split(" ");

        while (!str.equals("get") && (!validStr(split) || !validPos(split, board, possibleMoves) || hasBarPiece(outs))) {
            System.out.print("\nThat wasn't valid. Enter Your Move: ");
            str = scan.nextLine();

            if (str.equalsIgnoreCase("exit")) {
                System.out.println("Sorry you had to leave");
                System.out.println("Hope you come back soon!");
                System.exit(0);     
            }

            if (str.equalsIgnoreCase("no"))
                return 0;
            
            open = getPieceOut(str, outs, possibleMoves);
            if (!open)
                return 0;
            
            split = str.split(" ");
        }

        return movePiece(split, board, possibleMoves, outs);
    }

    public boolean getPieceOut(String str, List<Checker> outs, List<Integer> possible) {
        if (str.equals("get") && hasBarPiece(outs)) {
            int dice = possible.get(0);
            boolean open = true;

            if (side == 0) {
                if (board[24 - dice].getOpposite(side) >= 2 || board[25 - dice].getSame(side) <= 0)
                    open = false;
            } else {
                if (board[dice - 1].getOpposite(side) >= 2 || board[dice].getSame(side) <= 0)
                    open = false;
            }

            System.out.println("You are using your " + dice);

            if (!open) {
                System.out.println("That is not an open square. Try again next turn.");
            }
            
            return open;
        }

        return true;
    }

    public Triangle [] getBoard() {
        return board;
    }
    
    private boolean hasBarPiece(List<Checker> outs) {
        if (outs.size() != 0) {
            for (Checker c: outs) {
                if (c.getSide() == side)
                    return true;
            }
        }

        return false;
    }

    public int movePiece(String [] input, Triangle [] board, List<Integer> possibleMoves, List<Checker> out) {
        //The character positions
        int char1 = Integer.parseInt(input[0]) - 1;
        int char2 = Integer.parseInt(input[1]) - 1;

        int char1a = char1 + 1;
        int char2a = char2 + 1;

        int num = Math.abs(char1a - char2a);
        if ((char1a > 12 && char2a < 13) || (char2a > 12 && char1a < 13))
            num = acrossBoard(char1a, char2a);
        
        //Moving the piece
        possibleMoves.remove(new Integer(num));

        Triangle r = board[char1];
        int x = board[char1].getLength() - 1;
        while (board[char1].getPieceAtIndex(x).getSide() != side)
            x--;
        
        Checker ch = board[char1].getPieceAtIndex(x);
        board[char1].removePiece(ch);
        board[char2].addPiece(ch);

        //blot opponent piece with single
        if (board[char2].getOpposite(side) == 1) {
            Checker outsider = new Checker(side);

            for (Checker c: board[char2].checkerList()) {
                if (c.getSide() != side) {
                    outsider = c;
                    break;
                }
            }
            
            board[char2].removePiece(outsider);
            out.add(outsider);
        }

        //All pieces are in home?
        int low = 18;
        int high = 23;

        if (side == 0) {
            low = 6;
            high = 11;
        }
        
        int counter = 0;
        for (int i = low; i <= high; i++) {
            for (Checker c: board[i].checkerList())
                if (c.getSide() == side)
                    counter++;
        }

        //End return statements
        this.board = board;

        if (counter == 15)
            return 1;

        return 0;
    }

    public boolean validStr(String [] input) {
        
        if (input.length != 2)
            return false;

        try {
            int num1 = Integer.parseInt(input[0]);
            int num2 = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public boolean validPos(String [] input, Triangle [] board, List<Integer> possibleMoves) {
        //Setting up the variables for position
        int char1 = Integer.parseInt(input[0]);
        int char2 = Integer.parseInt(input[1]);

        int num = char2 - char1;
        int num2 = char1 - char2;

        if (char1 >= 13 && char2 >= 13) {
            num = char1 - char2;
            num2 = char2 - char1;
        }

        //Moving normally along a side
        if (!possibleMoves.contains(num) && ((char1 > 12 && char2 > 12) || (char2 < 13 && char1 < 13) ))
            return false;
        
        //Moving from bottom to top
        if ((char1 <= 12 && char2 >= 13) && (!possibleMoves.contains(acrossBoard(char1, char2)) || char1 < 6 || char2 < 18))
            return false;
                
        //Moving from top to bottom
        if ((char2 <= 12 && char1 >= 13) && (!possibleMoves.contains(acrossBoard(char1, char2)) || char1 > 19 || char2 > 7))
            return false;

        //If moving to a position with 2 or more opponent     
        if (board[char2 - 1].getOpposite(side) >= 2 || board[char1 - 1].getSame(side) <= 0)
            return false;

        //Valid move
        return true;
    }

    private int acrossBoard(int char1, int char2) {
        if (char1 < char2) {
            int counter = 0;
            while (char1 != char2) {
                counter++;
                if (char1 <= 12)
                    char1++;
                else if (char1 == 13)
                    char1 = 24;
                else
                    char1--;
            }

            return counter - 1;
        }

        int counter = 0;
        while (char1 != char2) {
            counter++;
            if (char1 >= 13)
                char1--;
            else if (char1 == 12)
                char1 = 1;
            else
                char1++;
        }

        return counter - 1;
    }
}