package handler;

import service.AuthService;
import service.GameService;
import service.UserService;

public class Handler {
    protected UserService userService;
    protected GameService gameService;
    protected AuthService authService;

    public Handler() {
        this.userService = new UserService();
        this.gameService = new GameService();
        this.authService = new AuthService();
    }
}
