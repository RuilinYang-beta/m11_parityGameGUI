package resources;

import algorithms.Algorithm;
import algorithms.AlgorithmFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.Solver;
import modelStep.Step;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/vertex")
public class GameResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   // the computed steps
    public Collection<Step> parseGame(String jsonString){
        JSONObject jo = new JSONObject(jsonString);
        String algorithm = jo.getString("algorithm");
        String gameString = jo.getString("game");
        Algorithm algorithmObj = AlgorithmFactory.getAlgorithm(algorithm);

        return Solver.process(gameString, algorithmObj, false);
    }
}