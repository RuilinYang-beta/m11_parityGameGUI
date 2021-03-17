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
//        return null;
    }
}
