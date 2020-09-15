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
  
  public String getBoard() {
    Gson gson = new Gson();
    String json = gson.toJson(this);
    return json;
  }
  
  public void startGame() {
    gameStarted = true;
  }
  
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

// check forward diagonal
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
	this.checkDraw();
}

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
