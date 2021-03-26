package algorithms;

import modelGame.*;
import modelStep.Attribute;
import modelStep.Effect;
import modelStep.GameStatus;
import modelStep.Step;

import java.util.*;

public class DFI implements Algorithm{
    /**
     * Algorithm defines sets:
     * Z : distraction set
     * F : freezing sets 0..d
     * S : strategy (vertex -> vertex)
     */
    private final Set<Vertex> Z = new HashSet<>();
    private final Map<Vertex, Integer> F = new HashMap<>();
    private final Map<Vertex, Vertex> S = new HashMap<>();
    // for recording the current status of the whole game
    private final GameStatus gameStatus = new GameStatus();
    // for returning to the client
    private final List<Step> steps = new ArrayList<>();
    private boolean solved = false;

    /**
     * Given a parity game as a collection of vertices, solve the game.
     * After solving, use the getWinner() and getStrategy() methods for the solution
     */
    public void solve(Game pg) {
        init(pg);  // init gameStatus of the whole game
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

//                    triggerStep(vertices, updated);
//
//                    // important: break the loop so we can restart from 0 now
//                    somethingchanged = true;
//                    break;

                    somethingchanged = true;
                }

                if (updated.size() != 0) {
                    triggerStep(vertices, updated);
                }

                // important: break the loop so we can restart from 0 now
                if (somethingchanged) {
                    break;
                }

            }
        }
        solved = true;

        System.out.println("There are " + steps.size() + " steps.");
    }



    /**
     * Initialize <GameStatus> for all nodes.
     */
    private void init(Game pg) {
        Collection<Vertex> vertices = pg.getVertices();

        for (Vertex v : vertices) {
            // init <GameStatus> object, the attribute of nodeStatus differs per algorithm
            HashMap<String, String> nodeStatus = new HashMap<>();
            nodeStatus.put("id", "" + v.getId());
            // freeze: specific to DFI
            nodeStatus.put("freeze", null);
            // distract: specific to DFI, initially no node is distraction
            nodeStatus.put("distract", "0");
            // init color is its priority's color
            nodeStatus.put("color", (v.getPriority() % 2 == 0)? "even" : "odd");
            // init visual effect is highlight
            nodeStatus.put("effect", Effect.HIGHLIGHT.toString());
            nodeStatus.put("strategy", null);
            nodeStatus.put("winner", (v.getPriority() % 2 == 0)? "0" : "1");
            // node i is at index i of gameStatus, to accommodate id staring from any number
            gameStatus.put(v.getId(), nodeStatus);
        }
    }


    protected void triggerStep(List<Vertex> vertices, List<Vertex> updated) {
        /*
         * At this point, I would like to add a step to the algorithm; for now.
         * ideally I would like to have "minor" and "major" steps but I would say
         * having different "levels" of steps is a nice-to-have but probably best
         * to start with just steps.
         */

        /*
         * Make copies of the sets to put in the step
         */

        // based on the update vertices, modify gameStatus
        for (Vertex v : updated) {
            int id = v.getId();
            // freeze attribute
            int freeze = F.getOrDefault(v, -1);
            gameStatus.get(id).put("freeze", "" + ((freeze == -1)? null : freeze));
            // distract attribute
            gameStatus.get(id).put("distract", (Z.contains(v))? "1" : "0");
            // color attribute
            String winner = (v.getPriority() % 2 == 0)? "even" : "odd";
            String winnerReverse = (v.getPriority() % 2 == 0)? "odd" : "even";
            gameStatus.get(id).put("color", (Z.contains(v))? winnerReverse : winner);
            // effect attribute: if not frozen, hightlight; else, shade
            gameStatus.get(id).put("effect", (freeze == -1)? Effect.HIGHLIGHT.toString() : Effect.SHADE.toString());
            // strategy attribute
            gameStatus.get(id).put("strategy", "" + ((S.get(v) == null)? null : S.get(v).getId()));
            // winner attribute
            String winnerNum = (gameStatus.get(id).get("color").equals("even"))? "0" : "1";
            gameStatus.get(id).put("winner", winnerNum);
        }

        // visually freeze the first vertex
        Vertex first = updated.get(0);
        gameStatus.get(first.getId()).put("freeze", "" + first.getPriority());

        // deep copy gameStatus, get a subset of it as updateStatus
        GameStatus gameStatusCopy = gameStatus.getDeepCopy();
        GameStatus updateStatus = new GameStatus();
        for (Vertex v : updated) {
            int id = v.getId();
            // updateStatus share the same set of data with gameStatusCopy
            // they won't be changed later
            updateStatus.put(id, gameStatusCopy.get(id));
        }

        System.out.println(updateStatus);
        // construct a step and add it to steps
        steps.add(new Step(gameStatusCopy, updateStatus, ""));
    }

    /**
     * Retrieve the winner of a vertex: player 0 or player 1.
     */
//    public int getWinner(Vertex v) {
//        int r = v.getPriority() % 2;
//        return Z.contains(v) ? 1-r : r;
//    }
    public String getWinner(Vertex v) {
        assert(solved);
        return gameStatus.get(v.getId()).get("winner");
    }

    /**
     * If the vertex owner is winning then returns a single vertex, the strategy to win
     * If the vertex owner is losing then returns null, because there is no strategy to win
     */
//    public Vertex getStrategy(Vertex v) {
//        return S.get(v);
//    }
    public String getStrategy(Vertex v){
        assert(solved);
        return gameStatus.get(v.getId()).get("strategy");
    }

    public Collection<Step> getSteps() {
        assert(solved);
        return steps;
    }

    // TODO: not finished yet
    public Collection<Attribute> getAttributes() {
        return null;
    }
}
