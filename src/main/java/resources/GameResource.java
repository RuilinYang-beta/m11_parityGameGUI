package resources;

import control.Solver;
import model.GameStatus;
import model.Step;
import model.Vertex;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/vertex")
public class GameResource {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)   // the computed steps
    public Collection<Step> parseGame(String gameString){

        return Solver.process(gameString, false);
    }


    private static HashMap<String, String> makeMap(List<String> labelList, List<String> dataList){
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < labelList.size(); i++){
            result.put(labelList.get(i), dataList.get(i));
        }
        return result;
    }

}
