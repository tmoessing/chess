package server;

import handler.*;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        UserHandler userHandler = new UserHandler();
        GameHandler gameHandler = new GameHandler();
        DeleteHandler deleteHandler = new DeleteHandler();

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (userHandler::handleRequest));
        Spark.post("/session", (userHandler::handleLogin));
        Spark.delete("/session", (userHandler::handleLogout));
//        Spark.get("/game", gameHandler::handleListGames));
        Spark.post("/game", gameHandler::handleCreateGame);
        Spark.put("/game", gameHandler::handleJoinGame);
        Spark.delete("/db", (deleteHandler::handleDelete));

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
