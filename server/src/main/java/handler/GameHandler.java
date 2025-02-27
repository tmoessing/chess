package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class GameHandler extends Handler{
    public GameHandler() {super();}

    public Object handleCreateGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);

        try {
            if (createGameRequest.gameName().isEmpty()) {
                res.status(400);
                return new Gson().toJson(new FailureRecord("Error: bad request"));
            }
            res.status(200);
            CreateGameResult createGameResult = gameService.createGame(createGameRequest, authToken);
            return new Gson().toJson(createGameResult);
        } catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new FailureRecord(e.getMessage()));
        }
    }

    public Object handleJoinGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);

        try {
            if (joinGameRequest.playerColor().isEmpty()) {
                res.status(400);
                return new Gson().toJson(new FailureRecord("Error: bad request"));
            }
            res.status(200);
            gameService.joinGame(joinGameRequest, authToken);
            return new Gson().toJson(new SuccessResult());
        } catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: bad request")){
                res.status(400);
            } else if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
                res.status(401);
            } else {
                res.status(403);
            }
            return new Gson().toJson(new FailureRecord(e.getMessage()));
        }
    }

//    public Object handleListGames(Request req, Response res) {
//        String authToken = req.headers("authorization");
//
//        try {
////            ListGamesResult listGamesResult = gameService.listGames(authToken);
//            res.status(200);
//            return new Gson().toJson(listGamesResult);
//        } catch (DataAccessException e) {
//            res.status(401);
//            return new Gson().toJson(new FailureRecord(e.getMessage()));
//        }
//    }
}
