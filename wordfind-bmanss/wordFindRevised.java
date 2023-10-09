import java.util.*;
import java.io.*;

public class wordFindRevised {

    // Determines if the word can be found at one of the cardinal directions
    public static boolean verifyDirection(char[][] letters, String word, int xPos, int yPos, int xDirection, int yDirection) {
        for (char wordLetter : word.toCharArray()) {
            if (yPos < 0 || xPos < 0 || yPos >= letters.length || xPos >= letters[0].length|| wordLetter != letters[yPos][xPos]) {
                return false;
            }
            yPos += yDirection;
            xPos += xDirection;
        }
        return true;
    }

    public static String searchWord(char[][] letterGrid,String word) {
        int row = 0;
        int col = 0;
        int rowLength = letterGrid.length;
        int colLength = letterGrid[0].length;
        
        while (row < rowLength) {
            // North
            if (verifyDirection(letterGrid, word, col, row, 0, -1)) {
                return word + " was found starting at " + (row + 1) + "," + (col + 1) + " and orientated North\n";
            }
            // Northeast
            if (verifyDirection(letterGrid, word, col, row, 1, -1)) {
                return word + " was found starting at " + (row + 1) + "," + (col + 1) + " and orientated Northeast\n";
            }
            // east
            if (verifyDirection(letterGrid, word, col, row, 1, 0)) {
                return word + " was found starting at " + (row + 1) + "," + (col + 1) + " and orientated East\n";
            }
            // southest
            if (verifyDirection(letterGrid, word, col, row, 1, 1)) {
                return word + " was found starting at " + (row + 1) + "," + (col + 1) + " and orientated Southeast\n";
            }
            // south
            if (verifyDirection(letterGrid, word, col, row, 0, 1)) {
                return word + " was found starting at " + (row + 1) + "," + (col + 1) + " and orientated South\n";
            }
            // southwest
            if (verifyDirection(letterGrid, word, col, row, 1, -1)) {
                return word + " was found starting at " + (row + 1) + "," + (col + 1) + " and orientated Southwest\n";
            }
            // west
            if (verifyDirection(letterGrid, word, col, row, -1, 0)) {
                return word + " was found starting at " + (row + 1) + "," + (col + 1) + " and orientated west\n";
            }
            // northwest
            if (verifyDirection(letterGrid, word, col, row, -1, -1)) {
                return word + " was found starting at " + (row + 1) + "," + (col + 1) + " and orientated Northwest\n";
            }
            col++;
            if (col >= colLength) {
                col = 0;
                row++;
            }
        }
        return word + " was not found";
    }

    public static void main(String[] args) {

        StringBuilder builder = new StringBuilder("");
        StringBuilder results = new StringBuilder("");
        String wordList[] = null;
        File cashiers = new File(args[0]);

        Scanner scanner = null;
        try {
            scanner = new Scanner(cashiers);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            return;
        }

        // Read in cashier file
        while (scanner.hasNext()) {
            String line = scanner.next();
            if (line.charAt(0) == '-')
                continue;
            builder.append(line);
            builder.append("\n");
        }
        
        // Filter cashier file contents
        String letterArray[] = builder.toString().replaceAll("[|]", "").split("\n");
        scanner.close();

        // read in word list and save it into and array
        builder = new StringBuilder("");
        if (args.length > 1){
            try {
                scanner = new Scanner(new File(args[1]));
                while (scanner.hasNext()) {
                    builder.append(scanner.next() + "\n");
                }
                wordList = builder.toString().split("\n");
                scanner.close();
            } catch (Exception e) {
                System.out.println("Word List File Not Found");
            }
        }

        // set array the letter grid dimensions
        int rowLength = letterArray.length;
        int colLength = letterArray[0].length();
        char letterGrid[][] = new char[rowLength][colLength];

        // fill in the letters to the letter grid
        for (int i = 0; i < letterGrid[0].length; i++) {
            letterGrid[i] = letterArray[i].toCharArray();
        }

        // go through each word and check if it exist at the current row,col
        if (wordList != null){
            for (String word : wordList) {
                results.append(searchWord(letterGrid, word));
            }
            System.out.println(results.toString());
        }
        else {
            while(true){
                System.out.print("Enter word to Search for: ");
                Scanner inputScanner = new Scanner(System.in);
                String wordToSearch = inputScanner.nextLine().trim().toUpperCase();
                if (wordToSearch == ""){
                    inputScanner.close();
                    System.exit(0);
                }
                else {
                    System.out.println(searchWord(letterGrid, wordToSearch));
                }
            }
        }
    }
}
