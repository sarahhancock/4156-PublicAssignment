package models;

import com.google.gson.Gson;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;
  
  /**
   * Creates an instance of a message.
   * @param validity whether the move is valid
   * @param c the code associated with the move
   * @param m the message to display with the move
   */
  public Message(boolean validity, int c, String m) {
    moveValidity = validity;
    code = c;
    message = m;
  }
  
  /**
   * Returns moveValidity, code, and message as a 
   * json formatted String.
   */
  public String getMessage() {
    Gson gson = new Gson();
    String json = gson.toJson(this);
    return json;
  }

}
