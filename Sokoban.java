import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;


public class Sokoban {

    public static final char UP = 'W';
    public static final char DOWN = 'S';
    public static final char LEFT = 'A';
    public static final char RIGHT = 'D';
    public static final char PLAYER = 'o';
    public static final char BOX = '@';
    public static final char WALL = '#';
    public static final char GOAL = '.';
    public static final char BOXONGOAL = '%';


    public static void main(String[] args) {
        new Sokoban().runApp();
    }


    public void runApp() {

        String mapfile = "map1.txt"; //change this to test other maps
        char[][] map = readmap(mapfile); // Assign map
        char[][] oldMap = readmap(mapfile); // Assigning the original map(placeholder)

        if (map == null) { //Tells the user that the map file can't be found
            System.out.println("Map file not found");
            return;
        }
        int[] start = findPlayer(map); // Declaring where player is
        if (start.length == 0) { // to check if any coordinates has been assigned
            System.out.println("Player not found");
            return;
        }
        int row = start[0];
        int col = start[1];
        while (!gameOver(map)) { //Makes the game going until q or covered all goals
            printMap(map);
            System.out.println("\nPlease enter a move (WASD): ");
            char input = readValidInput(); // Taking input from the method that only takes valid inputs
            if (input == 'q')  // Quit game, end loop
                break;
            if (input == 'r') {  // Restart game, puts the player back and refresh map
                map = readmap(mapfile);
                row = start[0];    //Putting the Player back to the original position (line 91-92), line 90 resets
                col = start[1];
                continue;
            }
            if (input == 'h') { // Print help menu when input is h
                printHelp();
            }
            if (!isValid(map, row, col, input)) // Check if the move is a valid move
                continue;
            movePlayer(map, row, col, input); // Moves the player, and calls moveBox to move box if legible

            fixMap(map, oldMap);  // fix map, puts back goals if it needs to be shown

            int[] newPos = findPlayer(map); // Find where player is in map declaring where
            row = newPos[0]; // Assigning a placeholder for where the player is in the map
            col = newPos[1];

        }
        System.out.println("Bye!");
    }


    public void printHelp() {
        System.out.println("Sokoban Help:");
        System.out.println("Move up: W");
        System.out.println("Move down: S");
        System.out.println("Move left: A");
        System.out.println("Move right: D");
        System.out.println("Restart level: r");
        System.out.println("Quit game: q");
        System.out.println("Help: h");
    }

   // Get input
    public char readValidInput() {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();

        while (input.length() == 0) // Check if there's a character
            input = in.nextLine();

        char move = input.charAt(0);

        while (move != UP && move != LEFT && move != DOWN && move != RIGHT && move != 'q' && move != 'r' && move != 'h') {
            System.out.println("Invalid input, please enter again: ");
            input = in.nextLine();
            while (input.length() == 0)
                input = in.nextLine();
            move = input.charAt(0);
        }

        return move;
    }

//Put back checkpoints
    public void fixMap(char[][] map, char[][] oldMap) {
        char[][] placeholderMap = new char[map.length][];
        for (int i = 0; i < map.length; i++)
            placeholderMap[i] = new char[map[i].length];

        for (int i = 0; i < oldMap.length; i++)
            for (int j = 0; j < oldMap[i].length; j++) {
                if (oldMap[i][j] == GOAL || oldMap[i][j] == WALL || oldMap[i][j] == BOXONGOAL) {
                    if (oldMap[i][j] == BOXONGOAL)
                        placeholderMap[i][j] = GOAL;
                    else
                        placeholderMap[i][j] = oldMap[i][j];
                } else
                    placeholderMap[i][j] = map[i][j];
            }

        for (int i = 0; i < oldMap.length; i++)
            for (int j = 0; j < oldMap[i].length; j++) {
                if (map[i][j] != BOXONGOAL && map[i][j] != PLAYER && map[i][j] != BOX)
                    map[i][j] = placeholderMap[i][j];


            }
    }

   //Move Box
    public void moveBox(char[][] map, int row, int col, char direction) {
        if (direction == DOWN && (map[row][col] == BOX || map[row][col] == BOXONGOAL)) {
            if (map[row][col] == BOXONGOAL)
                map[row][col] = GOAL;
            else
                map[row][col] = ' ';
            if (map[row + 1][col] == GOAL)
                map[row + 1][col] = BOXONGOAL;
            else
                map[row + 1][col] = BOX;
        }
        if (direction == UP && (map[row][col] == BOX || map[row][col] == BOXONGOAL)) {
            if (map[row][col] == BOXONGOAL)
                map[row][col] = GOAL;
            else
                map[row][col] = ' ';
            if (map[row - 1][col] == GOAL)
                map[row - 1][col] = BOXONGOAL;
            else
                map[row - 1][col] = BOX;
        }
        if (direction == RIGHT && (map[row][col] == BOX || map[row][col] == BOXONGOAL)) {
            if (map[row][col] == BOXONGOAL)
                map[row][col] = GOAL;
            else
                map[row][col] = ' ';
            if (map[row][col + 1] == GOAL)
                map[row][col + 1] = BOXONGOAL;
            else
                map[row][col + 1] = BOX;
        }
        if (direction == LEFT && (map[row][col] == BOX || map[row][col] == BOXONGOAL)) {
            if (map[row][col] == BOXONGOAL)
                map[row][col] = GOAL;
            else
                map[row][col] = ' ';
            if (map[row][col - 1] == GOAL)
                map[row][col - 1] = BOXONGOAL;
            else
                map[row][col - 1] = BOX;
        }

    }

   // Move Player
    public void movePlayer(char[][] map, int row, int col, char direction) {
        if (direction == UP || direction == DOWN || direction == LEFT || direction == RIGHT) {
            map[row][col] = ' ';

            if (direction == UP) {
                row -= 1;
                moveBox(map, row, col, direction);

            }
            if (direction == DOWN) {
                row += 1;
                moveBox(map, row, col, direction);
            }
            if (direction == LEFT) {
                col -= 1;
                moveBox(map, row, col, direction);
            }
            if (direction == RIGHT) {
                col += 1;
                moveBox(map, row, col, direction);
            }
            map[row][col] = PLAYER;

        }
    }


    public boolean gameOver(char[][] map) {
        boolean over = true;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == GOAL) {
                    over = false;
                    return over;
                }
            }
        }
        return over;
    }


    public int numberOfRows(String fileName) {
        try {

            File inputFile = new File(fileName);
            Scanner fin = new Scanner(inputFile);
            int counter = 0;
            while (fin.hasNextLine()) {
                counter++;
                fin.nextLine();
            }
            return counter;
        } catch (FileNotFoundException e) {
            return -1;
        }

    }

    //Read map file
    public char[][] readmap(String fileName) {
        try {
            File inputFile = new File(fileName);
            Scanner fin = new Scanner(inputFile);
            int tempcol;
            int rows = numberOfRows(fileName);
            int cols = 0;

            fin = new Scanner(inputFile);
            char[][] theMap = new char[rows][];
            while (fin.hasNextLine()) {
                String line = fin.nextLine();
                tempcol = line.length();
                theMap[cols] = new char[tempcol];
                cols += 1;
            }
            fin = new Scanner(inputFile);
            for (int i = 0; i < theMap.length; i++) {
                String line = fin.nextLine();
                for (int j = 0; j < theMap[i].length; j++) {
                    theMap[i][j] = line.charAt(j);
                }
            }
            fin.close();
            return theMap;

        } catch (FileNotFoundException e) {
            return null;
        }
    }


    public int[] findPlayer(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == PLAYER) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }


    public boolean isValid(char[][] map, int row, int col, char direction) {

        if (direction == UP && map[row - 1][col] == WALL) {
            return false;
        }
        if (direction == DOWN && map[row + 1][col] == WALL) {
            return false;
        }
        if (direction == LEFT && map[row][col - 1] == WALL) {
            return false;
        }
        if (direction == RIGHT && map[row][col + 1] == WALL) {
            return false;
        }

        //IF BOX IS BLOCKED
        if (direction == UP && (map[row - 1][col] == BOX || map[row - 1][col] == BOXONGOAL) && (map[row - 2][col] == WALL || map[row - 2][col] == BOX || map[row - 2][col] == BOXONGOAL)) {
            return false;
        }
        if (direction == DOWN && (map[row + 1][col] == BOX || map[row + 1][col] == BOXONGOAL) && (map[row + 2][col] == WALL || map[row + 2][col] == BOX || map[row + 2][col] == BOXONGOAL)) {
            return false;
        }
        if (direction == LEFT && (map[row][col - 1] == BOX || map[row][col - 1] == BOXONGOAL) && (map[row][col - 2] == WALL || map[row][col - 2] == BOX || map[row][col - 2] == BOXONGOAL)) {
            return false;
        }
        if (direction == RIGHT && (map[row][col + 1] == BOX || map[row][col + 1] == BOXONGOAL) && (map[row][col + 2] == WALL || map[row][col + 2] == BOX || map[row][col + 2] == BOXONGOAL)) {
            return false;
        }

        return true;
    }

    
    public void printMap(char[][] map) {
        //Finding longest array
        int max = map[0].length;
        for (int r = 0; r < map.length; r++) {
            if (max < map[r].length)
                max = map[r].length;
        }

        System.out.print("  ");
        int l = 0;
        for(int p = 0; p < max; p++) {
            System.out.print(l);
            l++;
            if ( l == 10)
                l -= 10;
        }

        System.out.println();
        for (int i = 0; i < map.length; i++) {
            System.out.printf("%-2d", i);
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }

            System.out.println();

        }
    }
}