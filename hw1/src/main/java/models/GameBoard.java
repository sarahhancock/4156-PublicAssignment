package models;

import com.google.gson.Gson;

public class GameBoard {
  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;
  
  private char[][] boardState;

  private int winner;
    
  private boolean isDraw;
  
  /**
   * Creates an instance of a gameboard
   * with player 1. Creates an empty board
   * with no winner or draw yet, and with 
   * the turn starting with player 1.
   */
  public GameBoard(Player p) {
    p1 = p;
    gameStarted = false;
    turn = 1;
    boardState = new char[3][3];
    winner = 0;
    isDraw = false;
  }
    
  public void addP2(Player p) {
    p2 = p;
  }
  
  public char getP1Type() {
    return p1.getType();
  }
  
  public Player getP1() {
    return p1;
  }
  
  public Player getP2() {
    return p2;
  }
  
  /**
   * Returns all attributes of GameBoard
   * as a json string.
   */
  public String getBoard() {
    Gson gson = new Gson();
    String json = gson.toJson(this);
    return json;
  }
  
  public void startGame() {
    gameStarted = true;
  }
  
  /**
   * Checks for a valid move and returns a message.
   * If not a valid move, returns a Message with the
   * corresponding error code and message to display.
   * If move is valid, returns a Message indicating the
   * move is valid. Also checks if the player has won the
   * game or if the game is a draw.
   */
  public Message move(Move move) {
    Message message;
    Player p = move.getPlayer();
    int x = move.getMoveX();
    int y = move.getMoveY();
    if (boardState[x][y] == 0) {
      if (p.getId() == turn) {
        boardState[x][y] = p.getType();
        if (turn == 1) {
          turn = 2;
        } else {
          turn = 1;
        }
        message = new Message(true, 100, "");
        this.checkWin(p, x, y);
      } else {
        message = new Message(false, 400, String.format("It's Player %d's turn!", turn));
      }
    } else {
      message = new Message(false, 400, "Invalid move!");
    }
    return message;
  }

  /**
   * Checks rows, columns, and diagonals associated
   * with a certain move to see if a player has won
   * the game. If there are no wins, it calls another
   * function to check if there is a draw.
   */
  public void checkWin(Player p, int x, int y) {
    int id = p.getId();
    char type = p.getType();
    
    // check row
    boolean win = true;
    for (int i = 0; i < 3; i++) {
      if (boardState[x][i] != type) {
        win = false;
        break;
      }
    }
    if (win) {
      winner = id;
      return;
    }
    
    // check column
    win = true;
    for (int i = 0; i < 3; i++) {
      if (boardState[i][y] != type) {
        win = false;
        break;
      }
    }
    if (win) {
      winner = id;
      return;
    }

    // check first diagonal (\)
    win = true;
    for (int i = 0; i < 3; i++) {
      if (boardState[i][i] != type) {
        win = false;
        break;
      }
    }
    if (win) {
      winner = id;
      return;
    }
    
    // check second diagonal (/)
    win = true;
    for (int i = 0; i < 3; i++) {
      if (boardState[i][3 - i - 1] != type) {
        win = false;
        break;
      }
    }
    if (win) {
      winner = id;
      return;
    }
    
    // If no wins, check for a draw
    this.checkDraw();
  }

  /**
   * Checks if all cells are filled
   * when no players have won to determine
   * if the game is a draw.
   * If there is a draw, it changes the
   * isDraw variable to true.
   */
  public void checkDraw() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (boardState[i][j] == 0) {
          return;
        }
      }
    }
    isDraw = true;
  }

}
