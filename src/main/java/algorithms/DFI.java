package algorithms;

import modelGame.*;
import modelStep.Attribute;
import modelStep.Effect;
import modelStep.GameStatus;
import modelStep.Step;

import java.util.*;
import java.util.stream.Collectors;

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

                int updatedSize = updated.size();
                if (updatedSize != 0) {
                    triggerStep(vertices, updated, "strategy found");
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
                    if (updated.size() != updatedSize) {
                        triggerStep(vertices, updated, "distractor found; freeze nodes with same winner; thaw & reset nodes with diff winner");
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
     * Initialize <GameStatus> for all nodes.
     */
    private void init(Game pg) {
        Collection<Vertex> vertices = pg.getVertices();

        for (Vertex v : vertices) {
            // init <GameStatus> object, the attribute of nodeStatus differs per algorithm
            HashMap<String, String> nodeStatus = new HashMap<>();
            // regardless of algorithm, all nodes have these 3 attributes
            nodeStatus.put("id", "" + v.getId());
            nodeStatus.put("strategy", null);
            nodeStatus.put("winner", (v.getPriority() % 2 == 0)? "0" : "1");

            // freeze: specific to DFI
            nodeStatus.put("freeze", null);
            // distract: specific to DFI, initially no node is distraction
            nodeStatus.put("distract", "0");

            // init color is its priority's color
            nodeStatus.put("color", (v.getPriority() % 2 == 0)? "even" : "odd");
            // init visual effect is highlight
            nodeStatus.put("effect", Effect.HIGHLIGHT.toString());

            // node i is at index i of gameStatus, to accommodate id staring from any number
            gameStatus.put(v.getId(), nodeStatus);
        }

        GameStatus gameStatusCopy = gameStatus.getDeepCopy();
        steps.add(new Step(gameStatusCopy, gameStatusCopy, "init: assume node won by the player of its priority's parity"));

    }


    protected void triggerStep(List<Vertex> vertices, List<Vertex> updated, String message) {
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
            // effect attribute: if not frozen and not distract, hightlight; else, shade
            gameStatus.get(id).put("effect", (freeze == -1 && !Z.contains(v))? Effect.HIGHLIGHT.toString() : Effect.SHADE.toString());
            // strategy attribute
            gameStatus.get(id).put("strategy", "" + ((S.get(v) == null)? null : S.get(v).getId()));
            // winner attribute
            String winnerNum = (gameStatus.get(id).get("color").equals("even"))? "0" : "1";
            gameStatus.get(id).put("winner", winnerNum);
        }

        // visually freeze the first vertex (why would I do this???)
//        Vertex first = updated.get(0);
//        gameStatus.get(first.getId()).put("freeze", "" + first.getPriority());

        // deep copy gameStatus, get a subset of it as updateStatus
        GameStatus gameStatusCopy = gameStatus.getDeepCopy();
        GameStatus updateStatus = new GameStatus();
        for (Vertex v : updated) {
            int id = v.getId();
            // updateStatus share the same set of data with gameStatusCopy
            // they won't be changed later
            updateStatus.put(id, gameStatusCopy.get(id));
        }
        // construct a step and add it to steps
        steps.add(new Step(gameStatusCopy, updateStatus, message));
    }

    /**
     * Retrieve the winner of a vertex: player 0 or player 1.
     */
    public String getWinner(Vertex v) {
        int r = v.getPriority() % 2;
        return "" + (Z.contains(v) ? 1-r : r);
    }
//    public String getWinner(Vertex v) {
//        return gameStatus.get(v.getId()).get("winner");
//    }

    /**
     * If the vertex owner is winning then returns a single vertex, the strategy to win
     * If the vertex owner is losing then returns null, because there is no strategy to win
     */
    public String getStrategy(Vertex v) {
        return (S.containsKey(v) && S.get(v) != null)? "" + S.get(v).getId() : "null";
    }
//    public String getStrategy(Vertex v){
//        return gameStatus.get(v.getId()).get("strategy");
//    }

    public Collection<Step> getSteps() {
        return steps;
    }

    public static Collection<Attribute> getAttributes() {
        Collection<Attribute> attributes = new ArrayList<>();
        // color
        Attribute color = new Attribute("color", Attribute.AttributeType.color, new ArrayList<>(Arrays.asList("even", "odd")));
        attributes.add(color);
        // freeze, don't care about distinct value because it will be displayed as text
        Attribute freeze = new Attribute("freeze", Attribute.AttributeType.text);
        attributes.add(freeze);
        // distract, here display as color
        Attribute distract = new Attribute("distract", Attribute.AttributeType.text);
        attributes.add(distract);

        return attributes;
    }
}
