package algorithms;

import modelStep.Attribute;
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
    // temp method
    Collection<Attribute> getAttributes();

}
