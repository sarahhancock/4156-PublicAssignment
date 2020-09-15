package models;

import com.google.gson.Gson;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;
  
  public Message(boolean validity, int c, String m) {
	  moveValidity = validity;
	  code = c;
	  message = m;
  }
  
  public String getMessage() {
	  Gson gson = new Gson();
	  String json = gson.toJson(this);
	  return json;
  }

}
