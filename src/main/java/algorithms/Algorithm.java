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

    // get list of attributes related to this algorithm
    static Collection<Label> getAttributes(){return null;}

}
