package resources;

import algorithms.Attribute;
import control.Solver;
import model.GameStatus;
import model.Step;
import model.Vertex;

import algorithms.PriorityPromotion;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/algorithm")
public class AlgorithmResource {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)   // the computed steps
    public Collection<Attribute> getAttributes(String algorithm){
        // temp use for priority promotion
        // todo: to be generalized
        return new PriorityPromotion().getAttributes();
    }
}
