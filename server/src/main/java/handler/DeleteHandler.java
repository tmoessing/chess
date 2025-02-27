package handler;

import model.SuccessResult;
import spark.*;
import com.google.gson.Gson;

public class DeleteHandler extends Handler {
    public DeleteHandler() {
        super();
    }

    public Object handleDelete(Request req, Response res) {
        userService.clearUsers();
        gameService.clearGames();
        authService.clearAuthData();

        res.status(200);
        return new Gson().toJson(new SuccessResult());
    }
}
