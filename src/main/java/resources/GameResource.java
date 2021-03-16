package resources;

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

//        return Solver.process(gameString, false);

        Collection<Step> steps = new ArrayList<>();

        // there are 6 dimensions:
        // id: the id
        // dim1: a binary dimension
        // dim2: a numeric dimension
        // dim3: a string dimension
        // strategy: the id of the node that this node should go to
        // winner: the winner of this node

        // dim1, dim2, dim3 will be different per algorithm

        // the first step
        List<String> labels = new ArrayList<>(Arrays.asList("id", "dim1", "dim2", "dim3", "strategy", "winner"));
        List<String> data0 = new ArrayList<>(Arrays.asList("0", "0", "9", "s0", null, null));
        List<String> data1 = new ArrayList<>(Arrays.asList("1", "0", "8", "s1", null, null));
        List<String> data2 = new ArrayList<>(Arrays.asList("2", "1", "7", "s2", null, null));

        GameStatus gameStatus0 = new GameStatus();
        gameStatus0.add(makeMap(labels, data0));
        gameStatus0.add(makeMap(labels, data1));
        gameStatus0.add(makeMap(labels, data2));
        steps.add(new Step(gameStatus0, "init status"));

        // second step
        data0.set(1, "1");   // set dim1 of node 0
        data0.set(4, "1");   // set strategy of node 0
        GameStatus gameStatus1 = new GameStatus();
        gameStatus1.add(makeMap(labels, data0));
        gameStatus1.add(makeMap(labels, data1));
        gameStatus1.add(makeMap(labels, data2));

        GameStatus update1 = new GameStatus(makeMap(labels, data0));
        steps.add(new Step(gameStatus1, update1, "node0: change dim1, set strategy"));


        // third step
        data0.set(2, "2");   // set dim2 of node 0
        data0.set(4, null);  // unset strategy of node 0
        data1.set(2, "2");   // set dim2 of node 1
        data1.set(4, "0");   // set strategy of node 1
        GameStatus gameStatus2 = new GameStatus();
        gameStatus2.add(makeMap(labels, data0));
        gameStatus2.add(makeMap(labels, data1));
        gameStatus2.add(makeMap(labels, data2));

        GameStatus update2 = new GameStatus();
        update2.add(makeMap(labels, data0));
        update2.add(makeMap(labels, data1));

        steps.add(new Step(gameStatus2, update2, "node0: change dim2, unset strategy; node1: change dim2, set strategy"));



        return steps;
    }


    private static HashMap<String, String> makeMap(List<String> labelList, List<String> dataList){
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < labelList.size(); i++){
            result.put(labelList.get(i), dataList.get(i));
        }
        return result;
    }

}
