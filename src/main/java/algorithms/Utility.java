package algorithms;

import modelGame.Game;
import modelGame.Vertex;

import java.util.*;
import java.util.stream.Collectors;

// TODO: move some methods in PriorityPromotion.java to here
/**
 * A class for common methods across different algorithms.
 */
public class Utility {

    /**
     * Compute the highest priority and vertices of this priority of a game.
     */
    public static Map<Integer, List<Vertex>> getHighestPriority(Game pg) {
        int p = -1;
        Map<Integer, List<Vertex>> result = new HashMap<>();
        for (Vertex v : pg.getVertices()) {
            if (v.getPriority() == p) {
                result.get(p).add(v);
            }
            if (v.getPriority() > p) {
                result.clear();
                p = v.getPriority();
                result.put(p, new ArrayList<>(Arrays.asList(v)));
            }
        }
        System.out.println(result);
        return result;
    }

    /**
     * Compute attractor nodes and strategy for the player in the game.
     * @param G: current sub game
     * @param Z: base for computing attractor
     * @param R: available nodes (the nodes in the current sub game)
     * @param player: the player
     * @param s: strategy of player
     */
	public static AS attract(Game G, Collection<Vertex> Z, Collection<Vertex> R, int player, Map<Vertex, Vertex> s){

		if (Z.size() == R.size()) {
			return new AS(Z, s);
		}

		Map<Vertex, List<Vertex>> inMap = G.getInMap();

		List<Vertex> attractor = new ArrayList<>(Z);      // shallow copy
		List<Vertex> queue = new ArrayList<>(Z);   	     // shallow copy

		while (!queue.isEmpty()) {
			Vertex v = queue.remove(0);

			if (inMap.containsKey(v)) {

				for (Vertex u: inMap.get(v)) {

					// the node should be in current subgame, and not in current attractorList
					if (R.contains(u) && !attractor.contains(u)) {

						if (u.getOwner() == player) {
							// node belongs to player, can strategically move to v
							attractor.add(u);
							queue.add(u);

						} else {
							// node belongs to opponent,
							// if all of its successor (in current game) is contained in attractorList,
							// then the node have no choice but enter the attractor area, add it to attractor
							List<Vertex> whereCanOpponentGo = G.getOutMap().get(u)
																			.stream()
															 				.filter(R::contains)   // in current subgame
																			.collect(Collectors.toList());

							if (attractor.containsAll(whereCanOpponentGo)) {
								attractor.add(u);
								queue.add(u);
							}
						}

						if (attractor.contains(u) && u.getOwner() == player && !s.containsKey(u)) {
						    s.put(u, v);
                        }
					}
				}
			}
		}
		return new AS(attractor, s);
	}

    /**
     * A shorthand for Attractor and Strategy.
     */
	public static class AS {
	    Collection<Vertex> attractor;
	    Map<Vertex, Vertex> strategy;

	    AS(Collection<Vertex> v, Map<Vertex, Vertex> s) {
	        this.attractor = v;
	        this.strategy = s;
        }

        public Collection<Vertex> getAttractor() {
            return attractor;
        }

        public Map<Vertex, Vertex> getStrategy() { return strategy; }
    }


    /**
     * Compute the sub game after removing a collection of nodes.
     * This includes removing all the edges associated with the removed nodes.
     */
    public static Game getSubgame(Game pg, Collection<Vertex> toRemove) {
        if (pg.getVertices().size() == toRemove.size()){
            return new Game(new ArrayList<>(), new HashMap<>(), new HashMap<>());
        }

        Collection<Vertex> nodes = new ArrayList<>(pg.getVertices());
        nodes.removeAll(toRemove);

        Map<Vertex, List<Vertex>> newOutMap = new HashMap<>(pg.getOutMap());

        Iterator<Map.Entry<Vertex, List<Vertex>>> iter = newOutMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Vertex, List<Vertex>> entry = iter.next();

            if (toRemove.contains(entry.getKey())) {
                // if the fromNode is in toRemove, remove the entry
                iter.remove();
            } else {
                // should keep the entry, but probably update the value
                List<Vertex> targets = new ArrayList<>(entry.getValue());
                targets.removeAll(toRemove);

                if (targets.size() == 0) {
                    iter.remove();
                } else {
                    // update entry value
                    entry.setValue(targets);
                }
            }
        }
        return new Game(nodes, newOutMap);
    }



}
