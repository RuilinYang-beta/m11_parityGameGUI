package algorithms;

import modelGame.Game;
import modelGame.Vertex;
import modelStep.Attribute;
import modelStep.Effect;
import modelStep.GameStatus;
import modelStep.Step;

import java.util.*;


public class Zielonka implements Algorithm {

    // for recording the current status of the whole game
    private final GameStatus gameStatus = new GameStatus();
    // for returning to the client
    private final List<Step> steps = new ArrayList<>();
    private boolean solved = false;

    public void solve(Game pg) {
        init(pg);
        zielonka(pg);
        solved = true;
    }


    /**
     * Initialize <GameStatus> for all nodes, initialize regionMap.
     */
    private void init(Game pg) {
        Collection<Vertex> vertices = pg.getVertices();

        for (Vertex v : vertices) {
            // init <GameStatus> object, the attribute of nodeStatus differs per algorithm
            HashMap<String, String> nodeStatus = new HashMap<>();
            nodeStatus.put("id", "" + v.getId());
            // init color is its priority's color
            nodeStatus.put("color", (v.getPriority() % 2 == 0)? "even" : "odd");
            // init visual effect is neutral, when neutral, color & region attribute doesn't matter
            nodeStatus.put("effect", Effect.NEUTRAL.toString());
            // the below two attr is wait to be set
            nodeStatus.put("strategy", null);
            nodeStatus.put("winner", null);
            // node i is at index i of gameStatus
            gameStatus.put(v.getId(), nodeStatus);
        }
    }


    /**
     * W: Winner, a hashmap of 2 elements, 0:winning_v_of_even, 1:winning_v_of_odd.
     */
    class W extends HashMap<Integer, HashSet<Vertex>> {
        W() {super();}
    }

    /**
     * S: Strategy, an arraylist of 2 elements: strategy_of_even_player, strategy_of_odd_player.
     */
    class S extends ArrayList<HashMap<Vertex, Vertex>> {
        S() {super();}

        HashMap<Vertex, Vertex> getOrDefault(int index, HashMap<Vertex, Vertex> defaultValue) {
            if (index < 0) {
                throw new IllegalArgumentException("index is less than 0: " + index);
            }
            return index <= this.size() - 1 ? this.get(index) : defaultValue;
        }
    }

    /**
     * A handy format of the return value of zielonka.
     */
    private class Result {
        private final W win;
        private final S strategy;

        public Result(W w, S s) {
            this.win = w;
            this.strategy = s;
        }

        public W getWin() {
            return win;
        }

        public S getStrategy() {
            return strategy;
        }

        public String toString(){
            return "win: " + this.win + "; str: " + this.strategy;
        }
    }

    private Result zielonka(Game G) {
        if (G.getVertices().size() == 0){
            return new Result(new W(), new S());
        }

        var entry = Utility.getHighestPriority(G).entrySet().iterator().next();
        int p = entry.getKey();
        int player = p % 2;
        Collection<Vertex> Z = entry.getValue();  // the base for compute attractor

        Utility.AS attrA = Utility.attract(G, Z, G.getVertices(), player, new HashMap<>());
        Collection<Vertex> A = attrA.getAttractor();
        Map<Vertex, Vertex> Sa = attrA.getStrategy();

        // step: shade A
        triggerStep(A, Sa, p, Effect.SHADE);

        // recursion
        Result resZielonka = zielonka(Utility.getSubgame(G, A));
        int oppo = 1 - player;
        // parse resZielonka
        HashSet<Vertex> winPlayer = resZielonka.getWin().getOrDefault(player, new HashSet<>());
        HashSet<Vertex> winOppo = resZielonka.getWin().getOrDefault(oppo, new HashSet<>());
        HashMap<Vertex, Vertex> strPlayer = resZielonka.getStrategy().getOrDefault(player, new HashMap());
        HashMap<Vertex, Vertex> strOppo = resZielonka.getStrategy().getOrDefault(oppo, new HashMap<>());

        Utility.AS attrB = Utility.attract(G,
                                           winOppo,  // base: winning nodes of opponent
                                           G.getVertices(),
                                           oppo,
                                           strOppo);
        Collection<Vertex> B = attrB.getAttractor();
        Map<Vertex, Vertex> Sb = attrB.getStrategy();

        if (B.containsAll(winOppo) && B.size() == winOppo.size()){
            winPlayer.addAll(A);
            strPlayer.putAll(Sa);
            // for every player's z, randomly choose an strategy so that stays in winning region
            for (Vertex v : Z){
                if (v.getOwner() == player) {
                    for (Vertex u : G.getOutMap().get(v)) {
                        if (winPlayer.contains(u)) {
                            strPlayer.put(v, u);
                            break;
                        }
                    }
                }
            }

        } else {
            resZielonka = zielonka(Utility.getSubgame(G, B));
            // overwrite parsing result
            winPlayer = resZielonka.getWin().getOrDefault(player, new HashSet<>());
            winOppo = resZielonka.getWin().getOrDefault(oppo, new HashSet<>());
            strPlayer = resZielonka.getStrategy().getOrDefault(player, new HashMap());
            strOppo = resZielonka.getStrategy().getOrDefault(oppo, new HashMap<>());

            winOppo.addAll(B);
            strOppo.putAll(Sb);
        }
        W win = new W();
        win.put(player, winPlayer);
        win.put(oppo, winOppo);
        S str = new S();
        if (player == 0) {
            str.add(strPlayer); str.add(strOppo);
        } else {
            str.add(strOppo); str.add(strPlayer);
        }

        Result res = new Result(win, str);
        // step: highlight those with strategy / winner
        triggerStep(G, res);
        return res;
    }

    /**
     * This step is triggered when recursively computing attractors.
     */
    private void triggerStep(Collection<Vertex> A, Map<Vertex, Vertex> Sa, int p, Effect effect) {
        // based on the update vertices, modify gameStatus
        for (Vertex v : A) {
            int id = v.getId();
            gameStatus.get(id).put("color", (p % 2 == 0)? "even" : "odd");
            gameStatus.get(id).put("effect", effect.toString());
            if (Sa.containsKey(v)) {
                gameStatus.get(id).put("strategy", Sa.get(v).toString());
            }
        }
        // deep copy gameStatus, get a subset of it as updateStatus
        GameStatus gameStatusCopy = gameStatus.getDeepCopy();
        GameStatus updateStatus = new GameStatus();
        for (Vertex v : A) {
            int id = v.getId();
            // updateStatus share the same set of data with gameStatusCopy
            // they won't be changed later
            updateStatus.put(id, gameStatusCopy.get(id));
        }
        // construct a step and add it to steps
        steps.add(new Step(gameStatusCopy, updateStatus, "Attractor at priority " + p + " removed"));
    }

    /**
     * This step is triggered when winning region and strategy is computed.
     */
    private void triggerStep(Game G, Result res) {
        // should overwrite the entire gameStatus
        init(G);
        // modify gameStatus,
        HashSet<Vertex> updated = new HashSet<>();
        var winEven = res.getWin().getOrDefault(0, new HashSet<>());
        var winOdd = res.getWin().getOrDefault(1, new HashSet<>());
        HashMap<Vertex, Vertex> strEven = res.getStrategy().getOrDefault(0, new HashMap());
        HashMap<Vertex, Vertex> strOdd = res.getStrategy().getOrDefault(1, new HashMap<>());
        for (Vertex v : winEven) {
            int id = v.getId();
            gameStatus.get(id).put("color", "even");
            gameStatus.get(id).put("effect", Effect.HIGHLIGHT.toString());
            gameStatus.get(id).put("winner", "0");
            updated.add(v);
        }
        for (Vertex v : winOdd) {
            int id = v.getId();
            gameStatus.get(id).put("color", "odd");
            gameStatus.get(id).put("effect", Effect.HIGHLIGHT.toString());
            gameStatus.get(id).put("winner", "1");
            updated.add(v);
        }
        for (Vertex v : strEven.keySet()){
            gameStatus.get(v.getId()).put("strategy", strEven.get(v).toString());
            updated.add(v);
        }
        for (Vertex v : strOdd.keySet()){
            gameStatus.get(v.getId()).put("strategy", strOdd.get(v).toString());
            updated.add(v);
        }
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
        steps.add(new Step(gameStatusCopy, updateStatus, "updated winning region & strategy"));
    }


    public String getWinner(Vertex v) {
        assert(solved);
        return gameStatus.get(v.getId()).get("winner");
    }

    public String getStrategy(Vertex v){
        assert(solved);
        return gameStatus.get(v.getId()).get("strategy");
    }

    public Collection<Step> getSteps() {
        assert(solved);
        return steps;
    }

    // get list of attributes related to this algorithm
    public static Collection<Attribute> getAttributes() {
        Collection<Attribute> attributes = new ArrayList<>();
        // color
        Collection<String> colorValues = new ArrayList<>(Arrays.asList("even", "odd"));
        Attribute color = new Attribute("color", Attribute.AttributeType.color, colorValues);
        attributes.add(color);

        return attributes;
    }

}
