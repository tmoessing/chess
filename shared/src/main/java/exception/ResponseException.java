package exception;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {
    final private int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", statusCode));
    }

//    public static ResponseException fromJson(InputStream stream) {
//        var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
//        var status = ((Double)map.get("status")).intValue();
//        String message = map.get("message").toString();
//        return new ResponseException(status, message);
//    }

    public static ResponseException fromJson(InputStream stream) {
        Gson gson = new Gson();

        // Preserve type information
        Map<String, Object> map = gson.fromJson(
                new InputStreamReader(stream),
                new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        // Handle potential missing fields
        Object statusObj = map.get("status");
        int status = (statusObj instanceof Number) ? ((Number) statusObj).intValue() : -1;

        String message = map.getOrDefault("message", "Unknown error").toString();

        return new ResponseException(status, message);
    }


    public int StatusCode() {
        return statusCode;
    }
}