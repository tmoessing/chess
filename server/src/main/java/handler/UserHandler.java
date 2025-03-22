package handler;

import dataaccess.DataAccessException;
import model.*;
import spark.*;
import com.google.gson.Gson;

public class UserHandler extends Handler {

    public UserHandler() {
       super();
    }

    public Object handleReqister(Request req, Response res) {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            res.status(400);
            return new Gson().toJson(new FailureRecord("Error: bad request"));
        }

        try {
            if (registerRequest.username().isEmpty() || registerRequest.password().isEmpty() || registerRequest.email().isEmpty()) {
                res.status(400);
                return new Gson().toJson(new FailureRecord("Error: bad request"));
            } else {
                res.status(200);
                RegisterResult registerResult = userService.register(registerRequest);
                return new Gson().toJson(registerResult);
            }
        } catch (DataAccessException e) {
            res.status(403);
            return new Gson().toJson(new FailureRecord(e.getMessage()));
        }
    }

    public Object handleLogin(Request req, Response res) {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);

        try {
            res.status(200);
            LoginResult loginResult = userService.login(loginRequest);
            return new Gson().toJson(loginResult);
        } catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new FailureRecord(e.getMessage()));
        }

    }

    public  Object handleLogout(Request req, Response res) {
        String authToken = req.headers("authorization");

        try {
            authService.logout(authToken);
            res.status(200);
            return new Gson().toJson(new SuccessResult());
        } catch (DataAccessException e) {
            res.status(401);
            return new Gson().toJson(new FailureRecord(e.getMessage()));
        }
    }
}
