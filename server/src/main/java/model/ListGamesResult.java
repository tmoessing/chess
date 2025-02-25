package model;

import java.util.ArrayList;
import java.util.Dictionary;
//{ "games": [{"gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}

public record ListGamesResult(ArrayList<Dictionary<String, String>> gamesList) {
}
