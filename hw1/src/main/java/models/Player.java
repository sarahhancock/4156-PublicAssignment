package models;

public class Player {

  private char type;
  private int id;
  
  /**
   * Creates an instance of a player with
   * type t and id i.
   */
  public Player(char t, int i) {
    type = t;
    id = i;
  }
  
  public char getType() {
    return type;
  }
  
  public int getId() {
    return id;
  }
}
