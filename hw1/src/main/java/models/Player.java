package models;

public class Player {

  private char type;
  private int id;
  
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
