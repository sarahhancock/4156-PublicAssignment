package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;
  
  public Move(Player p, int x, int y) {
	  player = p;
	  moveX = x;
	  moveY = y;
  }
  
  public int getMoveX() {
	  return moveX;
  }
  
  public int getMoveY() {
	  return moveY;
  }
  
  public Player getPlayer() {
	  return player;
  }

}
