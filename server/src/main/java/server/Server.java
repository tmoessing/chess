package server;

import handler.*;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", ((req, res) -> new RegisterHandler().handleRequest(req, res)));

        Spark.delete("/db", ((req, res) -> new DeleteHandler().handleDelete(req, res)));

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        return new Gson().toJson(pet);
}
