package algorithms;

import model.Game;
import model.Step;
import model.Vertex;

import java.util.Collection;
import java.util.Map;

public interface Algorithm {

    void solve(Game pg);

    int getWinner(Vertex v);

    Vertex getStrategy(Vertex v);

    // temp method
    Map<Vertex, Integer> getWinnerMap();

    // temp method
    Collection<Step> getSteps();
}
