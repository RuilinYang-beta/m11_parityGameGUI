package resources;

import modelStep.Label;

import algorithms.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;


@Path("/algorithm")
public class AlgorithmResource {

    /**
     * Invoked when the user choose an algorithm in the frontend. It returns the
     * labels of the chosen algorithm.
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Label> getLabels(String algorithmName){
        return AlgorithmFactory.getLabels(algorithmName);
    }

    /**
     * Invoked when the webapp is started. This method return all the available algorithms' names
     * to the frontend, to populate the "Select Algorithm" section in the collapsible sidebar.
     */
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
