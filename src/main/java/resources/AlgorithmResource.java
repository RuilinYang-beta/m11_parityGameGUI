package resources;

import modelStep.Attribute;

import algorithms.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/algorithm")
public class AlgorithmResource {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Attribute> getAttributes(String algorithmName){
        return AlgorithmFactory.getAttribute(algorithmName);
    }
}
