package client;

import java.io.*;
import java.net.*;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

public class ServerFacade {

    private static String serverUrl;
    private static String authToken;

    public static String getAuthToken() {
        return authToken;
    }

    public ServerFacade(String url) {
        serverUrl = url;
    }

    private <T> T makeRequest(String method, String path,
                              Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setRequestProperty("authorization", authToken);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public void register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        RegisterResult registerResult = this.makeRequest("POST", path, request, RegisterResult.class);
        authToken = registerResult.authToken();
    }

    public void login(LoginRequest request) throws ResponseException {
        var path = "/session";
        LoginResult loginResult = this.makeRequest("POST", path, request, LoginResult.class);
        authToken = loginResult.authToken();
    }

    public void logout() throws ResponseException {
        var path = "/session";
        SuccessResult successResult = this.makeRequest("DELETE", path, null, SuccessResult.class);
        authToken = null;
    }

    public void clear() throws ResponseException {
        var path = "/db";
        SuccessResult successResult = this.makeRequest("DELETE", path, null, SuccessResult.class);
    }

    public ListGamesResult listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResult.class);
    }

    public void createGame(CreateGameRequest request) throws ResponseException {
        var path = "/game";
        CreateGameResult createGameResult = this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public void joinGame(JoinGameRequest request) throws ResponseException {
        var path = "/game";
        SuccessResult successResult = this.makeRequest("PUT", path, request, SuccessResult.class);
    }

    public ChessGame getGameBoard(GetGameBoardRequest request) throws ResponseException {
        var path = "/gameBoard";
        GetGameBoardResult getGameBoardResult = this.makeRequest("POST", path, request, GetGameBoardResult.class);
        return getGameBoardResult.chessGame();
    }

}
