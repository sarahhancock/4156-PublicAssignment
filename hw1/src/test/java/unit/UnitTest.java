package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.junit.jupiter.api.Test;

public class UnitTest {

  @Test
  public void testStartGame() {
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.addP2(p2);
    
    // Check start game
    String board = game.getBoard();
    JSONObject jsonObject = new JSONObject(board);
    String gameStarted = jsonObject.get("gameStarted").toString();
    assertEquals("false", gameStarted);
    game.startGame();
    board = game.getBoard();
    jsonObject = new JSONObject(board);
    gameStarted = jsonObject.get("gameStarted").toString();
    assertEquals("true", gameStarted);
  }
  
  @Test
  public void testfirstMove() {
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.addP2(p2);
    game.startGame();

    // Check if player 2 can make the first move
    Move move = new Move(p2, 2, 1);
    game.move(move);
    String board = game.getBoard();
    JSONObject jsonObject = new JSONObject(board);
    board = jsonObject.get("boardState").toString();
    String expected = "[[\"\\u0000\",\"\\u0000\",\"\\u0000\"],"
            + "[\"\\u0000\",\"\\u0000\",\"\\u0000\"],[\"\\u0000\",\"\\u0000\",\"\\u0000\"]]";
    assertEquals(expected, board);
    
    // Check if player 1 can make the first move
    move = new Move(p1, 2, 1);
    game.move(move);
    board = game.getBoard();
    jsonObject = new JSONObject(board);
    board = jsonObject.get("boardState").toString();
    expected = "[[\"\\u0000\",\"\\u0000\",\"\\u0000\"],"
            + "[\"\\u0000\",\"\\u0000\",\"\\u0000\"],[\"\\u0000\",\"X\",\"\\u0000\"]]";
    assertEquals(expected, board);
  }
    
  @Test
  public void testTwoMoves() {
    // Check if player can move twice
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.addP2(p2);    
    game.startGame();
    Move move = new Move(p1, 2, 1);
    game.move(move);
    String board = game.getBoard();
    JSONObject jsonObject = new JSONObject(board);
    board = jsonObject.get("boardState").toString();
    String expected = "[[\"\\u0000\",\"\\u0000\",\"\\u0000\"],"
            + "[\"\\u0000\",\"\\u0000\",\"\\u0000\"],[\"\\u0000\",\"X\",\"\\u0000\"]]";
    assertEquals(expected, board);
    move = new Move(p1, 1, 0);
    game.move(move);
    board = game.getBoard();
    jsonObject = new JSONObject(board);
    board = jsonObject.get("boardState").toString();
    assertEquals(expected, board);
  }
    
  @Test
  public void testInvalidMove() {
    // Check if player move in the same spot
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.addP2(p2);
    game.startGame();
    Move move = new Move(p1, 1, 0);
    game.move(move);
    move = new Move(p2, 1, 0);
    game.move(move);
    String board = game.getBoard();
    JSONObject jsonObject = new JSONObject(board);
    board = jsonObject.get("boardState").toString();
    String expected = "[[\"\\u0000\",\"\\u0000\",\"\\u0000\"],"
            + "[\"X\",\"\\u0000\",\"\\u0000\"],[\"\\u0000\",\"\\u0000\",\"\\u0000\"]]";
    assertEquals(expected, board);
  }    
  
  @Test
  public void testMessage() {
    // Check if player can win
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.startGame();
    game.addP2(p2);
    Move move = new Move(p1, 0, 0);
    Message msg = game.move(move);
    assertEquals(msg.getMoveValidity(), true);
    assertEquals(msg.getCode(), 100);
    assertEquals(msg.getMessageBody(), "");
  }
    
  @Test
  public void testWinVertical() {
    // Check if player can win
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.startGame();
    game.addP2(p2);
    Move move = new Move(p1, 0, 0);
    game.move(move);
    move = new Move(p2, 0, 1);
    game.move(move);
    move = new Move(p1, 1, 0);
    game.move(move);
    move = new Move(p2, 0, 2);
    game.move(move);
    String board = game.getBoard();
    JSONObject jsonObject = new JSONObject(board);
    String winner = jsonObject.get("winner").toString();
    assertEquals("0", winner);
    move = new Move(p1, 2, 0);
    game.move(move);
    board = game.getBoard();
    jsonObject = new JSONObject(board);
    winner = jsonObject.get("winner").toString();
    assertEquals("1", winner);
  }
  
  @Test
  public void testWinHorizontal() {
    // Check if player can win
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.addP2(p2);
    game.startGame();
    Move move = new Move(p1, 0, 0);
    game.move(move);
    move = new Move(p2, 1, 1);
    game.move(move);
    move = new Move(p1, 0, 1);
    game.move(move);
    move = new Move(p2, 2, 2);
    game.move(move);
    String board = game.getBoard();
    JSONObject jsonObject = new JSONObject(board);
    String winner = jsonObject.get("winner").toString();
    assertEquals("0", winner);
    move = new Move(p1, 0, 2);
    game.move(move);
   
    board = game.getBoard();
    jsonObject = new JSONObject(board);
    winner = jsonObject.get("winner").toString();
    assertEquals("1", winner);
  }
  
  @Test
  public void testWinDiagonalForward() {
    // Check if player can win
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.addP2(p2);
    game.startGame();
    Move move = new Move(p1, 0, 0);
    game.move(move);
    move = new Move(p2, 0, 1);
    game.move(move);
    move = new Move(p1, 1, 1);
    game.move(move);
    move = new Move(p2, 0, 2);
    game.move(move);
    String board = game.getBoard();
    JSONObject jsonObject = new JSONObject(board);
    String winner = jsonObject.get("winner").toString();
    assertEquals("0", winner);
    move = new Move(p1, 2, 2);
    game.move(move);
   
    board = game.getBoard();
    jsonObject = new JSONObject(board);
    winner = jsonObject.get("winner").toString();
    assertEquals("1", winner);
  }

  @Test
  public void testWinDiagonalBackward() {
    // Check if player can win
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.addP2(p2);
    game.startGame();
    Move move = new Move(p1, 0, 2);
    game.move(move);
    move = new Move(p2, 0, 1);
    game.move(move);
    move = new Move(p1, 1, 1);
    game.move(move);
    move = new Move(p2, 0, 0);
    game.move(move);
    String board = game.getBoard();
    JSONObject jsonObject = new JSONObject(board);
    String winner = jsonObject.get("winner").toString();
    assertEquals("0", winner);
    move = new Move(p1, 2, 0);
    game.move(move);
   
    board = game.getBoard();
    jsonObject = new JSONObject(board);
    winner = jsonObject.get("winner").toString();
    assertEquals("1", winner);
  }
  
  @Test
  public void testDraw() {
    // Check if player can win
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.addP2(p2);
    game.startGame();
    Move move = new Move(p1, 0, 0);
    game.move(move);
    move = new Move(p2, 0, 2);
    game.move(move);
    move = new Move(p1, 0, 1);
    game.move(move);
    move = new Move(p2, 1, 0);
    game.move(move);
    move = new Move(p1, 1, 1);
    game.move(move);
    move = new Move(p2, 2, 1);
    game.move(move);
    move = new Move(p1, 1, 2);
    game.move(move);
    move = new Move(p2, 2, 2);
    game.move(move);
    String board = game.getBoard();
    JSONObject jsonObject = new JSONObject(board);
    String draw = jsonObject.get("isDraw").toString();
    assertEquals("false", draw);
    move = new Move(p1, 2, 0);
    game.move(move);
   
    board = game.getBoard();
    jsonObject = new JSONObject(board);
    draw = jsonObject.get("isDraw").toString();
    assertEquals("true", draw);
  }
  
  @Test
  public void testGameBoardGettersSetters() {
    Player p1 = new Player('X', 1);
    GameBoard game = new GameBoard(p1);
    Player p2 = new Player('O', 2);
    game.addP2(p2);
    assertEquals(p2, game.getP2());
    assertEquals(p1, game.getP1());
    assertEquals('X', game.getP1Type());
  }
    
  @Test
  public void testGetMessage() {
    // Check if GSON message conversion works
    Message message = new Message(true, 100, "");
    Gson gson = new Gson();
    String json = gson.toJson(message);
    assertEquals(message.getMessage(), json);
  }
    

}
 
