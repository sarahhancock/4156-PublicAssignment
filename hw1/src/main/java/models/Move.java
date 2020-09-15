package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;
  
  /**
   * Constructs an instance of a "move".
   * @param p the player who made the move
   * @param x the x position of the move
   * @param y the y position of the move
   */
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
