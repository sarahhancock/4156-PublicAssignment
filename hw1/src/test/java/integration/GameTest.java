package integration;

import com.google.gson.Gson;
import controllers.PlayGame;
import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(OrderAnnotation.class) 

public class GameTest {
	
  /**
  * Runs only once before the testing starts.
  */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(null);
    System.out.println("Before All");
  }
	
  /**
  * This method starts a new game before every test run. It will run every time before a test.
  */
  @BeforeEach
  public void startNewGame() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method.
    //HttpResponse<?> response = Unirest.get("http://localhost:8080/").asString();
    //int restStatus = response.getStatus();
    //System.out.println("Before Each");
  }
	
  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(1)
  public void newGameTest() {
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
    
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }
    
  /**
  * This is a test case to evaluate the startgame endpoint.
  */
  @Test
  @Order(2)
  public void startGameTest() {
    // Create a POST request to startgame endpoint and get the body
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = response.getBody();    
    System.out.println("Start Game Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('X', player1.getType());
    
    System.out.println("Test Start Game");
  }
  
  /**
  * This is a test case to evaluate the startgame endpoint with p1 choosing "O" instead
  * of "X".
  */
  @Test
  @Order(3)
  public void startGameChooseOTest() {
    // Create a POST request to startgame endpoint and get the body
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    String responseBody = response.getBody();    
    System.out.println("Start Game Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('O', player1.getType());
    
    System.out.println("Test Start Game");
  }
  
  /**
  * This is a test case to evaluate the joingame endpoint.
  */
  @Test
  @Order(4)
  public void joinGameTest() {
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    int status = response.getStatus();
    assertEquals(status, 200);
    System.out.println("Test Join Game");
  }
 
  /**
  * This is a test case to evaluate the move p1 endpoint.
  */
  @Test
  @Order(3)
  public void moveP1Test() {
    // Create a POST request to move endpoint and get the body
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    String responseBody = response.getBody();
    Message m = new Message(true, 100, "");
    assertEquals(m.getMessage(), responseBody);
    System.out.println("Test P2 Move");
  }
  
  @Test
  @Order(5)
  public void moveP2TestInvalid() {
    // Test if P2 can move in spot P1 already moved in
	Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    String responseBody = response.getBody();
    System.out.println(responseBody);
    Message m = new Message(false, 400, "Invalid move!");
    assertEquals(m.getMessage(), responseBody);
    System.out.println("Test Invalid P2 Move");
  }
  
  @Test
  @Order(6)
  public void moveP1TestInvalid() {
    // Test if P1 can move twice
	Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    String responseBody = response.getBody();
    System.out.println(responseBody);
    Message m = new Message(false, 400, "It's Player 2's turn!");
    assertEquals(m.getMessage(), responseBody);
    System.out.println("Test if P1 can move twice");
  }

  @Test
  @Order(7)
  public void moveP2Test() {
    // Create a POST request to move endpoint and get the body
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    String responseBody = response.getBody();
    System.out.println(responseBody);
    Message m = new Message(true, 100, "");
    assertEquals(m.getMessage(), responseBody);
    System.out.println("Test Valid P2 move");
  }
  
  
    
  /**
  * This will run every time after a test has finished.
  */
  @AfterEach
  public void finishGame() {
    System.out.println("After Each");
  }
    
  /**
   * This method runs only once after all the test cases have been executed.
   */
  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
    System.out.println("After All");
  }
}