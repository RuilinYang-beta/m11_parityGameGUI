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

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getAlgorithms() {
        List<String> algorithmList = new ArrayList<>();

        algorithmList.add("Priority Promotion");
        algorithmList.add("DFI");
        algorithmList.add("Zielonka");
        // todo: names of customized algorithm(s) to be added

        return algorithmList;
    }
}
