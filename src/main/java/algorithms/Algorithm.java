package algorithms;

import model.Game;
import model.Step;
import model.Vertex;

import java.util.Collection;
import java.util.Map;

public interface Algorithm {

    void solve(Game pg);

    String getWinner(Vertex v);

    String getStrategy(Vertex v);

    // temp method
    Collection<Step> getSteps();

    // get list of attributes related to this algorithm
    // temp method
    Collection<String> getAttributes();

}
