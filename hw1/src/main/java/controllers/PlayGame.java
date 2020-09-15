package controllers;

import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;

class PlayGame {

  private static final int PORT_NUMBER = 8080;
  private static Javalin app;
  private static GameBoard game;
  
  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    //Create app
    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });

    // Redirect to tic tac toe board.
    app.get("/newgame", ctx -> {
      ctx.redirect("/tictactoe.html");
    });
    
    // Start game, creating player 1 and game board.
    app.post("/startgame", ctx -> {
      char type1 = ctx.req.getParameter("type").charAt(0);
      Player p1 = new Player(type1, 1);
      game = new GameBoard(p1);
      ctx.result(game.getBoard());
    });

    // Player 2 joins game and game begins.
    app.get("/joingame", ctx -> {
      char type2;
      if (game.getP1Type() == 'X') {
        type2 = 'O';
      } else {
        type2 = 'X';
      }
      Player p2 = new Player(type2, 2);
      game.addP2(p2);
      ctx.redirect("/tictactoe.html?p=2");
      game.startGame();
      sendGameBoardToAllPlayers(game.getBoard());
    });
    
    
    // Player 1 moves.
    app.post("/move/1", ctx -> {
      int x = Integer.parseInt(ctx.req.getParameter("x"));
      int y = Integer.parseInt(ctx.req.getParameter("y"));
      Move move = new Move(game.getP1(), x, y);
      Message message = game.move(move);
      ctx.result(message.getMessage());
      sendGameBoardToAllPlayers(game.getBoard());
      System.out.println("Sent board to all players");
    });
    
    // Player 2 moves.
    app.post("/move/2", ctx -> {
      int x = Integer.parseInt(ctx.req.getParameter("x"));
      int y = Integer.parseInt(ctx.req.getParameter("y"));
      Move move = new Move(game.getP2(), x, y);
      Message message = game.move(move);
      ctx.result(message.getMessage());
      sendGameBoardToAllPlayers(game.getBoard());
      System.out.println("Sent board to all players");
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }
  
  /**
   * Send message to all players.
   * 
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        System.out.println("Failed to send game board to all players.");
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
