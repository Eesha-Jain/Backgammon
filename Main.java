import java.util.*;

class Main {
    //P1 (WHITE) PERSPECTIVE
    //0-5: P1 Outboard, 6-11: P1 Homeboard, 12-17: P2 Outboard, 18-23: P2 Homeboard
    private static Triangle [] board;
    private static List<Checker> outPieces;

    public static void main(String[] args) {
        Scanner scan = new Scanner (System.in);

        board = new Triangle [24];
        outPieces = new ArrayList<>();

        welcome();
        initalizeBoard();

        //Which player goes first
        int wh = rollDice();
        int re = rollDice();

        while (wh == re) {
            wh = rollDice();
            re = rollDice(); 
        }

        System.out.println("White rolled " + wh + " and Red rolled " + re);

        String current = "White";

        if (wh < re)
            current = "Red";

        System.out.println(current + " goes first.");

        //Set up players
        Player white = new Player(0);
        Player red = new Player(1);

        //Actual game
        int won = 0;
        while (won == 0) {
            System.out.println("\n" + current + " player turn.\n");

            printBoard();
            int dice = rollDice();
            int dice2 = rollDice();

            List<Integer> possibleMoves = new ArrayList<>();
            possibleMoves.add(dice);
            possibleMoves.add(dice2);

            System.out.println("\n\nYou rolled " + dice + " and " + dice2);
            
            if (dice == dice2) {
                System.out.println("You rolled doubles!");
                possibleMoves.add(dice);
                possibleMoves.add(dice2);
            }
        
            while (possibleMoves.size() > 0) {
                System.out.print("Enter Your Move: ");
                String input = scan.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Sorry you had to leave");
                    System.out.println("Hope you come back soon!");
                    System.exit(0);
                }

                if (current.equals("White")) {
                    won = white.input(input, board, possibleMoves, outPieces);
                    board = white.getBoard();
                } else {
                    won = red.input(input, board, possibleMoves, outPieces);
                    board = red.getBoard();
                }
                
                System.out.print(Colors.RESET);
            }

            if (won == 0) {
                if (current.equals("White"))
                    current = "Red";
                else 
                    current = "White";
            }
        }

        System.out.println("Congratulations " + current + " for winning!");
    }

    public static int rollDice() {
        return (int)(Math.random() * 6) + 1;
    }

    public static void initalizeBoard() {
        ArrayList<Checker> arr = new ArrayList<>();
        for (int i = 1; i <= board.length; i++) {
            arr = new ArrayList<Checker>();
            int num = (28 - i);
            int side = 0;

            if (i == 1 || i == 7 || i == 13 || i == 19) {
                if (i == 1 || i == 19)
                    side = 1;
                
                Checker a = new Checker (side);
                Checker b = new Checker (side);
                Checker c = new Checker (side);
                Checker d = new Checker (side);
                Checker e = new Checker (side);

                arr.add(a);
                arr.add(b);
                arr.add(c);
                arr.add(d);
                arr.add(e); 
            } else if (i == 5 || i == 17) {
                if (i == 17)
                    side = 1;
                
                Checker a = new Checker (side);
                Checker b = new Checker (side);
                Checker c = new Checker (side);

                arr.add(a);
                arr.add(b);
                arr.add(c);
            } else if (i == 12 || i == 24) {
                if (i == 12)
                    side = 1;
                
                Checker a = new Checker (side);
                Checker b = new Checker (side);

                arr.add(a);
                arr.add(b);
            }

            Triangle t = new Triangle(arr);
            board[i - 1] = t;
        }
    }

    public static void printBoard() {
        int large = 0;
        for (int i = 12; i < board.length; i++) {
            if (board[i].getTotal() > large)
                large = board[i].getTotal();
            if (i >= 18)
                System.out.print(Colors.CYAN_BOLD);
            else
                System.out.print(Colors.GREEN_BOLD);
            System.out.print((i+1) + "\t");
        }

        System.out.println(Colors.RESET);        

        for (int i = 1; i <= large; i++) {
            for (int j = 12; j < board.length; j++) {
                if (board[j].getTotal() >= i) {
                    if (board[j].getPieceAtIndex(i - 1).getSide() == 0)
                        System.out.print(Colors.WHITE + "W");
                    else
                        System.out.print(Colors.RED + "R");
                } else {
                    System.out.print(Colors.RESET + " ");
                }

                System.out.print("\t");
            }
            System.out.println(Colors.RESET); 
        }

        large = 0;
        for (int i = 0; i < 12; i++) {
            if (board[i].getTotal() > large)
                large = board[i].getTotal();
        }

        System.out.println(Colors.RESET); 

        for (int i = large; i > 0; i--) {
            for (int j = 0; j < 12; j++) {
                if (board[j].getTotal() >= i) {
                    if (board[j].getPieceAtIndex(i - 1).getSide() == 0)
                        System.out.print(Colors.WHITE + "W");
                    else
                        System.out.print(Colors.RED + "R");
                } else {
                    System.out.print(Colors.RESET + " ");
                }

                System.out.print("\t");
            }
            System.out.println(Colors.RESET); 
        }

        for (int i = 0; i < 12; i++) {
            if (i >= 6)
                System.out.print(Colors.CYAN_BOLD);
            else
                System.out.print(Colors.GREEN_BOLD);
            System.out.print((i+1) + "\t");
        }
        System.out.print("\n" + Colors.BLUE_BOLD + "Bar: ");
        for (int i = 0; i < outPieces.size(); i++) {
            if (outPieces.get(i).getSide() == 0)
                System.out.print(Colors.WHITE + "W");
            else
                System.out.print(Colors.RED + "R");
            if (i + 1 != outPieces.size())
                System.out.print(Colors.BLUE + ", ");
        }

        System.out.println(Colors.RESET);
    }

    public static void welcome() {
        System.out.println("-----------------------");
        System.out.println("Welcome to this game of Backgammon!");
        System.out.println("Here are the rules on how to play the game: https://www.wikihow.com/Play-Backgammon\n");
        System.out.println("The computer will roll the dice for you. \nThen, you will input: ");
        System.out.println("\n- Enter 'exit' to exit early");
        System.out.println("- Enter 'no' if there is no valid move");
        System.out.println("- Enter 'get' to get your piece out of the bar");
        System.out.println("- In your input, type the trianglecurrent triangleending");
        System.out.println("If you wanted to move a checker from triangle 1 to triangle 5, you would write: 1 5");
        System.out.println("Have fun!");
        System.out.println("-----------------------");
    }
}