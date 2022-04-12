import java.util.*;
import java.io.*;

public class WordFind{

public static int count;
public enum Direction {North,South,East,West,Northeast,Northwest,Southeast,Southwest}

//This function will take a List of a List as a parameter and return it converted to a 2D char array
//to be used in checking adjacent grid letters for simplicity
static char[][] convertToArray(ArrayList<List<Character>> gridrows){

    char[][] allLetters = new char[gridrows.size()][gridrows.get(0).size()];
    int startindex = 0;

    while (startindex < gridrows.size()){
        for ( List<Character> rows : gridrows){
            Character [] currentRow = new Character[rows.size()];
            currentRow = rows.toArray(currentRow);
                for (int j = 0; j < rows.size(); j++){
                    allLetters[startindex][j] = currentRow[j];
            }
            startindex++;
        }
    }
    return allLetters;
}

//This function checks surronding letters and returns all directions where a match for the target is found
static List<Direction> checkAdjacent(char target, char letters[][],int row, int col){

    List<Direction> Adjacent = new ArrayList<>();

    //check north
    if (row != 0 && target == letters[row - 1][col]){
        Adjacent.add(Direction.North);
        ++count;
    }

    //check South
    if (row != letters.length - 1 && target == letters[row + 1][col]){
        Adjacent.add(Direction.South);
        ++count;
    }

    //check east
    if (col != letters[0].length - 1 && target == letters[row][col + 1]){
        Adjacent.add(Direction.East);
        ++count;
    }

    //check west
    if (col != 0 && target == letters[row][col - 1])    {
        Adjacent.add(Direction.West);
        ++count;
    }
    
    //check northeast
    if ((row != 0 && col !=letters[0].length - 1)  && target == letters[row - 1][col + 1]){
        Adjacent.add(Direction.Northeast);
        ++count;
    }

    //check northwest
    if ((row != 0 && col != 0) && target == letters[row - 1][col - 1]){
        Adjacent.add(Direction.Northwest);
        ++count;
    }

    //check southeast
    if ((row != letters.length - 1 && col != letters[0].length - 1) && target == letters[row + 1][col + 1]){
        Adjacent.add(Direction.Southeast);
        ++count;
    }

    //check southwest
    if ((row != letters.length - 1 && col != 0) && target == letters[row + 1][col - 1]){
        Adjacent.add(Direction.Southwest);
        ++count;
    }

    return Adjacent;
}
public static void main(String[] args) throws FileNotFoundException {

    boolean findWord = true;
    boolean found;

    //List to track increasing amount of gridrows
    ArrayList<List<Character>> gridRows = new ArrayList<List<Character>>();
    List<Character> gridCol;

    //2D char array to ultimately hold each letter
    char [][] letters;

    File cashiers = new File(args[0]);
    File wordList = new File("");
    
    if (args.length > 1)
        wordList = new File(args[1]);
    
    Scanner input;
    Scanner gridScan = new Scanner(cashiers);
    Scanner wordScan = null;

    if(wordList.exists())                       //check if a wordlist exist and create a scanner for that file
        wordScan = new Scanner(wordList);

    String [] gridLetters;
    String line;

    //Scan the file and parse contents into the correct format. Stores accepted charcters into a list to later be converted to a 2d char array 
    while(gridScan.hasNext()){
        line = gridScan.next();
        line = line.replace("|", "").replace("-", "");                    //remove unwanted characters from line read in from file
        gridLetters = line.split("");                                     //turn string of allowed characters into an array
        if ( !line.equals("") ){ 
            gridCol = new ArrayList<Character>();                         //create temp list to hold all characters
            for(int j = 0; j < gridLetters.length;j++){        
                String nextletter = gridLetters[j].toUpperCase();         //temp string to hold current letter
                if (nextletter.length() > 0)
                  gridCol.add(nextletter.charAt(0));                      //character is added to next column letter in current working row
            }
            gridRows.add(gridCol);                                        //add all letters in each column to the row
        }       
    }
    gridScan.close(); 
    letters = convertToArray(gridRows);                                   //convert parsed letters into workable 2D grid

    //start of loop to check through each letter in the array
    while (findWord){         
        count = 0;
        found = false;
        String word;
        String searchWord;
        int currentLetter = 0;

        //if a word file exist get the next word from the there otherwise get user input and end program if they enter nothing
        if (wordList.exists()){
            word = wordScan.nextLine().toUpperCase();                            //default to all uppercase
            searchWord = word.replace(" ", "");
        }
        else{
            System.out.print("Enter word to Search for: ");
            input = new Scanner(System.in);
            word = input.nextLine().toUpperCase(); 
            searchWord = word.replace(" ", "");     
                if (word.isEmpty()){  
                    input.close();
                    return;
                }
        }

        //Starting loop for checking each letter in entire grid
        for (int row = 0; row < letters.length && found == false; row++){
            
            // search through array looking for first match, can use letter[0].length since each row will have the same amount of characters
            for(int col = 0; col < letters[0].length && found == false ;col++){
                char findLetter = letters[row][col];
                ++count;
                if (searchWord.charAt(currentLetter) == findLetter){
                    ++currentLetter;

                //check if word only consist of one character and display where it was found
                if (searchWord.length() == 1){
                    found = true;
                    System.out.println(word + " was found at " + (row + 1) + ", " + (col + 1) + ". " + "Total Comparisons: " + count);
                    break;
                }
                
                    //Check each direction a match for the second letter was found 
                    for (Direction next: checkAdjacent(searchWord.charAt(currentLetter), letters, row, col)){
                        if (found == true)     //break from searching if a match has already been found 
                            break;

                        //check case if word only consist of 2 letters and display location if found
                        if (searchWord.length() == 2){
                            found = true;
                            System.out.println(word + " was found at " + (row + 1) + ", " + (col + 1) + 
                                                " and orientated " + next + ". " + "Total Comparisons: " + count);
                            break;
                        }
                        currentLetter = 2;               //start at third letter in word. Start here because checkAdjacent() already checks second letter

                        //check through each returned direction the next letter was found
                        switch (next) {

                            //Check remaining letters going North
                            case North:
                            for (int i = 2; currentLetter + 1 <= searchWord.length(); ++i){
                                ++count;
                                if((row - i) < 0)
                                    break;
                                if(searchWord.charAt(currentLetter) == letters[row - i][col]){
                                    if (currentLetter + 1 == searchWord.length()){
                                        System.out.println(word + " was found starting at " + (row + 1) + ", " + (col + 1) +
                                                             " and orientated " + next + ". " + "Total Comparisons: " + count);
                                        found = true;
                                    }
                                    ++currentLetter;
                                }
                                else { break; }
                            }
                                break;
                            
                            //Check remaining letters going South
                            case South:
                            for (int i = 2; currentLetter + 1 <= searchWord.length(); ++i){
                                ++count;
                                if((row + i) == letters.length)
                                    break;
                                if(searchWord.charAt(currentLetter) == letters[row + i][col]){
                                    if (currentLetter + 1 == searchWord.length()){
                                        System.out.println(word + " was found starting at " + (row + 1) + ", " + (col + 1) +  
                                                            " and orientated " + next + ". " + "Total Comparisons: " + count);
                                        found = true;
                                    }
                                    ++currentLetter;
                                }
                                else { break; }
                            }
                                break;
    
                            //Check remaining letters going East
                            case East:
                            for (int i = 2; currentLetter + 1 <= searchWord.length(); ++i){
                                ++count;
                                if((col + i) == letters[0].length){
                                    break;
                                }
                                if(searchWord.charAt(currentLetter) == letters[row][col + i]){
                                    if (currentLetter + 1 == searchWord.length()){
                                        System.out.println(word + " was found starting at " + (row + 1) + ", " + (col + 1) +  
                                                            " and orientated " + next + ". " + "Total Comparisons: " + count);
                                        found = true;
                                    }
                                    ++currentLetter;
                                }
                                else { break; }
                            }
                                break;
    
                            //Check remaining letters going West
                            case West:
                            for (int i = 2; currentLetter + 1 <= searchWord.length(); ++i){
                                ++count;
                                if((col - i) < 0){
                                    break;
                                }
                                if(searchWord.charAt(currentLetter) == letters[row][col - i]){
                                    if (currentLetter + 1 == searchWord.length()){
                                        System.out.println(word + " was found starting at " + (row + 1) + ", " + (col + 1) +  
                                                            " and orientated " + next + ". " + "Total Comparisons: " + count);
                                        found = true;          
                                    }
                                    ++currentLetter;
                                }
                                else { break; }
                            }
                                break; 
    
                            //Check remaining letters going Northest
                            case Northeast:
                            for (int i = 2; currentLetter + 1 <= searchWord.length(); ++i){
                                ++count;
                                if((row - i) < 0 || (col + i) == letters[0].length)
                                    break;
                                if(searchWord.charAt(currentLetter) == letters[row - i][col + i]){
                                    if (currentLetter + 1 == searchWord.length()){
                                        System.out.println(word + " was found starting at " + (row + 1) + ", " + (col + 1) +  
                                                            " and orientated " + next + ". " + "Total Comparisons: " + count);
                                        found = true;
                                    }
                                    ++currentLetter;
                                }
                                else { break; }
                            }
                                break;
    
                            //Check remaining letters going Northwest
                            case Northwest:
                            for (int i = 2; currentLetter + 1 <= searchWord.length(); ++i){
                                ++count;
                                if((row - i) < 0 || (col - i) < 0)
                                    break;
                                if(searchWord.charAt(currentLetter) == letters[row - i][col - i]){
                                    if (currentLetter + 1 == searchWord.length()){
                                        System.out.println(word + " was found starting at " + (row + 1) + ", " + (col + 1) +  
                                                            " and orientated " + next + ". " + "Total Comparisons: " + count);
                                        found = true;
                                    }
                                    ++currentLetter;
                                }
                                else { break; }
                            }
                                break;
    
                            //Check remaining letters going Southeast
                            case Southeast:
                            for (int i = 2; currentLetter + 1 <= searchWord.length(); ++i){
                                ++count;
                                if(row + i == letters.length || col + i == letters[0].length)
                                    break;
                                if(searchWord.charAt(currentLetter) == letters[row + i][col + i]){
                                    if (currentLetter + 1 == searchWord.length()){
                                        System.out.println(word + " was found starting at " + (row + 1) + ", " + (col + 1) +  
                                                            " and orientated " + next + ". " + "Total Comparisons: " + count);
                                        found = true;
                                    }
                                    ++currentLetter;
                                }
                                else { break; }
                            }
                                break;
    
                            //Check remaining letters going SouthWest
                            case Southwest:
                            for (int i = 2; currentLetter + 1 <= searchWord.length(); ++i){
                                ++count;
                                if(row + i == letters.length || col - i < 0 )
                                    break;
                                if(searchWord.charAt(currentLetter) == letters[row + i][col - i]){
                                    if (currentLetter + 1 == searchWord.length()){
                                        System.out.println(word + " was found starting at " + (row + 1) + ", " + (col + 1) +  
                                                            " and orientated " + next + ". " + "Total Comparisons: " + count);
                                        found = true;
                                    }
                                    ++currentLetter;
                                }
                                else { break; }
                            }
                             break;
                            }
                        }
                    }

                    //if word has not been found after searching each letter say it was not found 
                    if (row == letters.length - 1 && col == letters[0].length -1 && found == false)
                        System.out.println(word + " was not found" + ". " + "Total Comparisons: " + count);
                        
                    currentLetter = 0;                               //reset word starting position
                }   
                if (wordList.exists() && !wordScan.hasNext())        //stop searching if no words are left
                    findWord = false;
            }
        }
        return;
    }
}