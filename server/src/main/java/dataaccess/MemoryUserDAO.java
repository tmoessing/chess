package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    private static final ArrayList<UserData> USER_LIST = new ArrayList<>();

    public boolean isUsernameTaken(String username) {
        for (UserData userData : USER_LIST) {
            if (userData.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void addUserData(String username, String password, String email) {
        UserData userData = new UserData(username, password, email);
        USER_LIST.add(userData);
    }

    public UserData pullUserData(String username) {
        for (UserData userData : USER_LIST) {
            if (userData.username().equals(username)) {
                return userData;
            }
        }
        return null;
    }

    public void clearAllUsers() {
        USER_LIST.clear();
    }
}
