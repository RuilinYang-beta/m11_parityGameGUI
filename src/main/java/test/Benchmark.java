package test;

import algorithms.Algorithm;
import modelGame.Game;
import modelGame.Vertex;
import modelStep.Attribute;
import modelStep.Effect;
import modelStep.GameStatus;
import modelStep.Step;

import java.util.*;

/**
 * This class is based on the given DFI, only changes are in data structures,
 * so it's correctness is assured.
 */
public class Benchmark implements Algorithm {
    private final Set<Vertex> Z = new HashSet<>();
    private final Map<Vertex, Integer> F = new HashMap<>();
    private final Map<Vertex, Vertex> S = new HashMap<>();
    // for recording the current status of the whole game
    private final GameStatus gameStatus = new GameStatus();
    // for returning to the client
    private final List<Step> steps = new ArrayList<>();


    public void solve(Game pg) {
        List<Vertex> vertices = new ArrayList<>(pg.getVertices()); // make a local copy that we can sort
        vertices.sort(Comparator.comparingInt(Vertex::getPriority)); // sort vertices from low to high

        boolean somethingchanged = true;
        while (somethingchanged) {
            somethingchanged = false;

            // start checking from low to high priority
            for (Vertex vertex : vertices) {
                // don't check if frozen
                if (F.get(vertex) != null) continue;
                // don't check if already in Z (we know there won't be a change then)
                if (Z.contains(vertex)) continue;

                List<Vertex> updated = new ArrayList<>();

                // check one step ahead
                int winner = 1-vertex.getOwner();
                for (Vertex target : pg.getOutMap().get(vertex)) {
                    int targetWinner = Integer.parseInt(getWinner(target));
                    if (targetWinner != winner) {
                        // ok found a winning edge for the vertex owner, so set the strategy
                        Vertex oldStrategy = S.get(vertex);

                        if (!target.equals(oldStrategy)) {
                            // update the strategy
                            S.put(vertex, target);
                            updated.add(vertex);
                        }
                        winner = targetWinner;
                        break;
                    }
                }
                int pr = vertex.getPriority();

                // check if the vertex should actually be in Z
                if (winner != (pr % 2)) {
                    Z.add(vertex);

                    if (!updated.contains(vertex)) {
                        updated.add(vertex);
                    }

                    // freeze/thaw/reset all lower vertices
                    for (Vertex v2 : vertices) {
                        if (v2.getPriority() >= pr) break; // only check lower vertices
                        if (F.getOrDefault(v2, -1) >= pr) continue; // skip if frozen at >= priority
                        if (getWinner(v2).equals("" + winner)) {
                            // same winner so freeze it
                            F.put(v2, pr);
                        } else {
                            // different winner so thaw & reset
                            F.remove(v2);
                            Z.remove(v2);
                            S.remove(v2);
                        }
                        updated.add(v2);
                    }
                    somethingchanged = true;
                }

                // important: break the loop so we can restart from 0 now
                if (somethingchanged) {
                    break;
                }
            }
        }
    }

    /**
     * Retrieve the winner of a vertex: player 0 or player 1.
     */
    public String getWinner(Vertex v) {
        int r = v.getPriority() % 2;
        return "" + (Z.contains(v) ? 1-r : r);
    }

    /**
     * If the vertex owner is winning then returns a single vertex, the strategy to win
     * If the vertex owner is losing then returns null, because there is no strategy to win
     */
    public String getStrategy(Vertex v) {
        Vertex s = S.getOrDefault(v, null);
        return (s == null)? "null" : "" + s.getId();
    }

    public Collection<Step> getSteps() {
        return null;
    }
}
