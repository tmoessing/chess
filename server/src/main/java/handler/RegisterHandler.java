package handler;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.RegisterRequest;
import model.RegisterResult;
import service.UserService;
import spark.*;
import com.google.gson.Gson;

public class RegisterHandler extends Handler {

    public RegisterHandler() {
       super();
    }

    public Object handleRequest(Request req, Response res) {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult registerResult;

        try {
            if (registerRequest.username().isEmpty() || registerRequest.password().isEmpty() || registerRequest.email().isEmpty() ){
                registerResult = new RegisterResult("Error: bad request", null);
                res.status(400);
            } else {
                registerResult = userService.register(registerRequest);
                res.status(200);
            }
        } catch (DataAccessException e) {
            registerResult = new RegisterResult(e.getMessage(), null);
            res.status(403);
        }

        return new Gson().toJson(registerResult);
    }
}
