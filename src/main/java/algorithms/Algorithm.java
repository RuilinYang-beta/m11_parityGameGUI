package algorithms;

import modelStep.Label;
import modelGame.Game;
import modelStep.Step;
import modelGame.Vertex;

import java.util.Collection;

public interface Algorithm {

    void solve(Game pg);

    String getWinner(Vertex v);

    String getStrategy(Vertex v);

    Collection<Step> getSteps();

    /**
     * Get list of Labels related to this algorithm.
     * This method will be invoked when the user selects an algorithm
     * in the frontend.
     */
    static Collection<Label> getLabels(){return null;}

}
