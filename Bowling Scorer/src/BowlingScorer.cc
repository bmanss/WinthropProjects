#include<iostream>
#include<vector>
#include<string>
#include<fstream>
#include<sstream>
using namespace std;


/**
 * This function takes a vector of integers that represent all the balls
 * thrown in a single game of bowling, and it computes the total score
 * for that game.  If the 'print' boolean flag is true, it will also
 * print the game in a format that you can easily see.
 *   @param game Vector of integers representing the pins knocked down on each throw of a Game
 *   @param print Boolean flag indicating whether or not you wish to print the Game
 *   @return The total score of the game.
 **/
double ScoreBowlingGame(const vector<int> game, bool print) throw (int){
  int totalPins = 0;
  int ballIdx = 0;
  bool error;
  vector<int> frameTotals;

  // If we are printing, put the frame number at the top
  if (print) {
    for (int frameCounter=1; frameCounter<=10; frameCounter++)
      cout << "F" << frameCounter << "\t";
    cout << endl;
  }

  // Count for just the first nine frames
  for (int frameCounter=1; frameCounter<10; frameCounter++) {

    // If there was a strike, it's 10 plus the next two throws
    if (game[ballIdx] == 10) {
      if (print) cout << "X" << "\t";
    //Exception to catch if procceding throws after a strike are out of range
        if (game[ballIdx+2] < 0 || game[ballIdx+2] > 10){
          frameTotals.push_back(-1);
          ballIdx += 1;
        }
        else if (game[ballIdx+1] < 0 || game[ballIdx+1] > 10) {
          frameTotals.push_back(-1);
          ballIdx += 1;
        }
        else {
          totalPins += game[ballIdx] + game[ballIdx+1]  + game[ballIdx+2];
          frameTotals.push_back(totalPins);
          ballIdx += 1;  // Strikes means only one ball was thrown
        }
      }

    // If there was a spare, it's 10 plus the next throw
    else if (game[ballIdx] + game[ballIdx+1] == 10) {
      if (print) cout << game[ballIdx] << "/" << "\t";
    //Exception to catch if throw after a spare is out of range
      if (game[ballIdx+2] < 0 || game[ballIdx+2] > 10){
        frameTotals.push_back(-1);
        ballIdx += 2;
        }
      else{
      totalPins += game[ballIdx] + game[ballIdx+1]  + game[ballIdx+2];
      frameTotals.push_back(totalPins);
      ballIdx += 2;  // Spare means two balls were thrown
      }
    }

    // If not all the pins were knocked down, just total those two balls
    else if (game[ballIdx] + game[ballIdx+1] < 10 && game[ballIdx] + game[ballIdx+1] >= 0) {
      totalPins += game[ballIdx] + game[ballIdx+1];
      frameTotals.push_back(totalPins);
      if (print) cout << game[ballIdx] << "," << game[ballIdx+1] << "\t";
      ballIdx += 2;  // Not marking means two balls were thrown
    }

    // If there was some other situation, then there was a problem
    else {
      cout << "ERROR\t";
      frameTotals.push_back(-1);
      ballIdx += 2;
    }
  }// End of For loop

  // -- We're on the 10'th frame now --

  // Check if following throws would be out range to catch non-existent throws or bad input and skip other checks
  try{
    if (game[ballIdx+1] < 0 || game[ballIdx+1] > 10)                           //check if next throw will be out of range
      throw out_of_range("Score not within range");
    if ((game[ballIdx] == 10) && (game[ballIdx+2] < 0 || game[ballIdx+2] > 10))    //check if next throws after a strike will be out of range
      throw out_of_range("Score not within range");
    if ((game[ballIdx] + game[ballIdx+1] == 10) && (game[ballIdx+2] < 0 || game[ballIdx+2] > 10))//check if next throws after a strike will be out of range
      throw out_of_range("Score not within range");
  }
  catch(out_of_range& e){
    goto end;
    }

// A strike on the first throw gets you two more throws
  if (game[ballIdx] == 10) {
    totalPins += game[ballIdx] + game[ballIdx+1]  + game[ballIdx+2];
    if (print) {
      cout << "X";
      if (game[ballIdx+1] == 10) cout << "X";
      else cout << game[ballIdx+1];
      if (game[ballIdx+2] == 10) cout << "X";
      else cout << "," << game[ballIdx+2];
    }
    frameTotals.push_back(totalPins);
  }

  // A spare on the first two throws gets you one more throw
  else if (game[ballIdx] + game[ballIdx+1] == 10) {
    totalPins += game[ballIdx] + game[ballIdx+1]  + game[ballIdx+2];
    if (print) {
      cout << game[ballIdx] << "/";
      if (game[ballIdx+2] == 10) cout << "X";
      else cout << game[ballIdx+2];
    }
    frameTotals.push_back(totalPins);
  }

  // If you didn't mark, your game is over
  else if (game[ballIdx] + game[ballIdx+1] < 10 && game[ballIdx] + game[ballIdx+1] >= 0) {
    totalPins += game[ballIdx] + game[ballIdx+1];
    if (print) cout << game[ballIdx] << "," << game[ballIdx+1];
    frameTotals.push_back(totalPins);
  }

  // Any other situation meant something is wrong
  else {
    end:
    cout << "ERROR";
    frameTotals.push_back(-1);
  }

  // If we are printing, put the frame totals at the bottom
  if (print) {
    cout << endl;
    for (int frameCounter=0; frameCounter<=9; frameCounter++){
      if (frameTotals.at(frameCounter) == -1 || error == true){
        cout << "ERROR\t";
        error = true;
      }
      else{
        cout <<frameTotals[frameCounter] << "\t";
      }
    }
  cout << endl;
  }

//checks if an error occured and throws -1 so alert that the total cannot be calculated
  if (error == true)
    throw -1;
  return totalPins;
}

/**
 *  This is a simple function to test to make sure the scoring routine works.
 **/
void TestScorer() {
  vector<int> myGame;

  // Frame 1
  myGame.push_back(10); // Strike!

  // Frame 2
  myGame.push_back(8);
  myGame.push_back(1);

  // Frame 3
  myGame.push_back(7);
  myGame.push_back(3); // Spare!

  // Frame 4
  myGame.push_back(5);
  myGame.push_back(3);

  // Frame 5
  myGame.push_back(10); // Strike!

  // Frame 6
  myGame.push_back(6);
  myGame.push_back(4); // Spare!

  // Frame 7
  myGame.push_back(7);
  myGame.push_back(1);

  // Frame 8
  myGame.push_back(6);
  myGame.push_back(3);

  // Frame 9
  myGame.push_back(9);
  myGame.push_back(1); // Spare!

  // Frame 10
  myGame.push_back(10); // Strike!
  myGame.push_back(8);
  myGame.push_back(0);

  cout << "Game Score: " << endl << ScoreBowlingGame(myGame, true) << endl;
}

int main(){

ifstream gameTotals;
vector<int> games;
string player;
string playerFrames;
string filePath ="../input/";
string fileName;
int playerThrow = 0;
stringstream frameBuffer;

cout << "Input File name: ";
cin >> fileName;

gameTotals.open(filePath + fileName);
if (gameTotals.fail()){                         // test for file failing to open
  cout << "Could not open file.";
  return 0;
}

//Read in contents from file while file is not empty
while (!gameTotals.eof()){
  gameTotals >> player;
  if (gameTotals.eof())
    break;
  getline(gameTotals,playerFrames);
  frameBuffer.str(playerFrames);

 // catch any errors while inputting data into vector of player scores
  try{
    for (char c: player){     //check if new line does not start with player's name
      if (isdigit(c) != 0)
      throw runtime_error("ERROR: Improper Format Check Input");
    }

  // transfer contents (player's score) in to vector
    while(frameBuffer >> playerThrow || !frameBuffer.eof()){
      if (frameBuffer.fail())                                          // throw an error if non-numbers are detected
        throw runtime_error("ERROR: Failed To Read line. Check Input");
      games.push_back(playerThrow);
      }

//throw error if game is not within minimum and maximum possible length for a game
    if(games.size() < 11 || games.size() > 21)
      throw out_of_range("ERROR: Frame count not within range");
    cout << "Player: " << player << endl;
    cout << "Game Score: " << ScoreBowlingGame(games, true) << endl << endl;
    games.clear();
    frameBuffer.clear();
    }

//Possible errors
    catch(runtime_error& e){                      //catch if user input something other than numbers
      cout << "Player: "<< player << " " << e.what() << endl << endl;
      games.clear();
      frameBuffer.clear();
    }
    catch(out_of_range& e){                        //catch if game is too short or long
      cout << e.what() << endl;
      games.clear();
      frameBuffer.clear();
    }
    catch(int x){                                   //catch error in function calculation
      cout << "Game Score: ERROR: Invalid Score Input." << endl << endl;
      games.clear();
      frameBuffer.clear();
    }
  }
gameTotals.close();
  return 0;
}
