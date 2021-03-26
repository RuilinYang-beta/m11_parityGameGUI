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
    private boolean solved = false;

    public void solve(Game pg){
//        init(pg);

        Result res = zielonka(pg);

        System.out.println(res);
        // parse result and write to gamestatus
        var winEven = res.getWin().getOrDefault(0, new HashSet<>());
        var winOdd = res.getWin().getOrDefault(1, new HashSet<>());
        var strEven = res.getStrategy().getOrDefault(0, new HashMap());
        var strOdd = res.getStrategy().getOrDefault(1, new HashMap<>());

        for (Vertex v : pg.getVertices()){
            HashMap<String, String> nodeStatus = new HashMap<>();
            nodeStatus.put("id", "" + v.getId());
            nodeStatus.put("winner", winEven.contains(v)? "0" : "1");

            if (strEven.containsKey(v)){
                String strategy = "" + strEven.get(v);
                nodeStatus.put("strategy", strategy);
            } else {
                String strategy = "" + strOdd.getOrDefault(v, null);
                nodeStatus.put("strategy", strategy);
            }


            gameStatus.put(v.getId(), nodeStatus);
        }

        solved = true;
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

        // TODO: how to see if two lists contains the same elements?
        if (B.containsAll(winOppo) && B.size() == winOppo.size()){
            winPlayer.addAll(A);
            strPlayer.putAll(Sa);
            // TODO: for every player's z, randomly choose an strategy so that stays in winning region
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
        return res;
    }

    public String getWinner(Vertex v) {
        assert(solved);
        return gameStatus.get(v.getId()).get("winner");
    }

    public String getStrategy(Vertex v){
        assert(solved);
        return gameStatus.get(v.getId()).get("strategy");
    }

    public Collection<Step> getSteps(){
        return null;
    }

    // get list of attributes related to this algorithm
    // temp method
    public Collection<Attribute> getAttributes() {
        return null;
    }

}
