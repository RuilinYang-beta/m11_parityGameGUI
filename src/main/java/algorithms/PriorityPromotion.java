package algorithms;

import modelGame.*;
import modelStep.Attribute;
import modelStep.Effect;
import modelStep.GameStatus;
import modelStep.Step;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Algorithm priority promotion.
 * This class contains some methods that might be specific to priority promotion,
 * for example, **reset the priority of all nodes / reset priority of a subset of nodes**,
 * and **get the successor of a set of node**,
 * Currently not sure if they are specific only to priority promotion,
 * if other algorithms also need these functionalities, consider moving them to class Game.
 */
public class PriorityPromotion implements Algorithm {

	// current priority, have to put it here to use lambda expression
	private int p;
	// a map from vertex to its regional priority
	private final Map<Vertex, Integer> regionMap = new HashMap<>();
	private final Map<Vertex, Vertex> S = new HashMap<>();
    // for returning to the client
	private final List<Step> steps = new ArrayList<>();
    // for recording the current status of the whole game
	// used in returning steps to the frontend and store winner/strategy when the game ends
    private final GameStatus gameStatus = new GameStatus();

	public void solve(Game pg){
		init(pg);

		List<Vertex> allNodes = new ArrayList<>(pg.getVertices());
		Game currentSubgame = pg;

		while (!allNodes.isEmpty()) {
			// find a dominion, and extend it with its attractors
			Map<Integer, Collection<Vertex>> dominionMap = findDominion(currentSubgame);
			// the first and only entry
			Map.Entry<Integer, Collection<Vertex>> entry = dominionMap.entrySet().iterator().next();
			int player = entry.getKey();
			Collection<Vertex> dominion = entry.getValue();
			// reset nodes other than the dominion nodes
			for (Vertex v : currentSubgame.getVertices()) {
				if (!dominion.contains(v)){
					regionMap.put(v, v.getPriority());
					S.remove(v);
				}
			}
			Collection<Vertex> toReset = currentSubgame.getVertices().stream().filter(e -> !dominion.contains(e)).collect(Collectors.toList());
			triggerStep(toReset, Effect.NEUTRAL.toString(), player, "reset nodes that are not in dominion", false, true);

			Collection<Vertex> dominionAndItsAttractor = getAttractor(currentSubgame, dominion, currentSubgame.getVertices(), player);

			// set winner and strategy of each node
			for (Vertex v : dominionAndItsAttractor) {
				int id = v.getId();
			    // record winners for  each node
				gameStatus.get(id).put("winner", "" + player);
			}

            triggerStep(dominionAndItsAttractor, Effect.HIGHLIGHT.toString(), player, "dominion and its attractor found", false, false);

			// update subgame by removing all the node and relevant edges of dominionAndItsAttractor from currentSubgame
			allNodes.removeAll(dominionAndItsAttractor);
			currentSubgame = computeSubgame(currentSubgame, dominionAndItsAttractor);

			triggerStep(dominionAndItsAttractor, Effect.SHADE.toString(), player, "remove dominion from subgame", false, false);
		}
	}


	/**
	 * Initialize <GameStatus> for all nodes, initialize regionMap.
	 */
	private void init(Game pg) {
		Collection<Vertex> vertices = pg.getVertices();

		for (Vertex v : vertices) {
			// init <GameStatus> object, the attribute of nodeStatus differs per algorithm
			HashMap<String, String> nodeStatus = new HashMap<>();
			// regardless of algorithm, all nodes have these 3 attributes
			nodeStatus.put("id", "" + v.getId());
			nodeStatus.put("strategy", null);
			nodeStatus.put("winner", null);

			// init regional priority is node priority
			nodeStatus.put("region", "" + v.getPriority());

			// init color is its priority's color
			nodeStatus.put("color", (v.getPriority() % 2 == 0)? "even" : "odd");
			// init visual effect is neutral, when neutral, color & region attribute doesn't matter
			nodeStatus.put("effect", Effect.NEUTRAL.toString());

			// node i is at index i of gameStatus
			gameStatus.put(v.getId(), nodeStatus);

			// init vertex-region map
			regionMap.put(v, v.getPriority());
		}
	}

	/**
	 * Given a parity game, return a pair, where the key is the player, the value is the list of nodes
	 * starting from which the player can make sure to win.
	 */
	public Map<Integer, Collection<Vertex>> findDominion(Game pg){
		resetRegionPriority(pg);
		// region to vertices map
		Map<Integer, Collection<Vertex>> regionMapInv = new HashMap<>();

		p = getHighestPriority(pg);
		Game currentSubgame = pg;

		while (true) {
			int player = p % 2;
			// nodes of the current subgame
			Collection<Vertex> available = currentSubgame.getVertices();
			// the base for computing attractor, that is
			// nodes within current subgame that has nodal priority / regional priority equal to p
			Collection<Vertex> base =  available.stream()
					 					  .filter(e -> e.getPriority() == p || regionMap.get(e) == p)
										  .collect(Collectors.toList());

			triggerStep(base, Effect.HIGHLIGHT.toString(),  p, "base", false, false);

			// within current subgame,
			// for player's node:   if it can go to base?  add
			// for opponent's node: if it must go to base? add
			// strategy is updated at the same time
			Collection<Vertex> attractor = getAttractor(currentSubgame, base, available, player);

			triggerStep(attractor, Effect.HIGHLIGHT.toString(), p, "attractor", false, false);

			// among player's node within attractor, those who must escape attractor
			Collection<Vertex> playerOpen = attractor.stream()
													.filter(e -> e.getOwner() == player &&
															noIntersection(pg.getOutMap().get(e), attractor))
													.collect(Collectors.toList());

			// among (successor of (opponent's node in attractor)), those who are outside attractor
			// (ie. where can opponent escape to, in original game)
			Collection<Vertex> opponentNodes = attractor.stream()
														.filter(e -> e.getOwner() != player)
														.collect(Collectors.toList());

			Collection<Vertex> opponentEscapeSet = getSuccessorOfNodes(opponentNodes, pg);
			opponentEscapeSet.removeAll(attractor);

			// where can opponent escape to, in current subgame
			Collection<Vertex> opponentCurrentEscapSet = getSuccessorOfNodes(opponentNodes, currentSubgame);
			opponentCurrentEscapSet.removeAll(attractor);

			if (!playerOpen.isEmpty() || !opponentCurrentEscapSet.isEmpty()) {
				// if the attractorList is open in current subgame (locally open)
				// set region of attractorList to be p
				for (Vertex node : attractor) {
					regionMap.put(node, p);
				}
				regionMapInv.put(p, attractor);

				triggerStep(attractor, Effect.SHADE.toString(), p,"locally closed & globally open, set regional priority and remove from game", true, false);

				// update currentSubgame and p, continue with the next highest priority
				currentSubgame = computeSubgame(currentSubgame, attractor);
				p = getHighestPriority(currentSubgame);

			} else if (!opponentEscapeSet.isEmpty()) {
				// the attractor is closed in current subgame, but open in original game (locally closed, globally open)
				// promote attractor, reset region priority for some nodes
				p = Collections.min(opponentEscapeSet.stream()
													 .map(e -> regionMap.get(e))
													 .collect(Collectors.toList()));
				// promote the attractor to the lowest region that it can go to
				for (Vertex node : attractor) {
					regionMap.put(node, p);
				}
				regionMapInv.get(p).addAll(attractor);

				triggerStep(regionMapInv.get(p), Effect.HIGHLIGHT.toString(), p, "locally closed & globally open, promote the attractor to the lowest higher region", true, false);

				// reset the region priority & strategy of the nodes with region < p
				Collection<Vertex> toNeutralize = new ArrayList<>();
				for (Vertex node : pg.getVertices()) {
					if (regionMap.get(node) < p) {
						int regionToReset = regionMap.get(node);
						// reset region
						if (regionMapInv.containsKey(regionToReset)) {
							toNeutralize.addAll(regionMapInv.get(regionToReset));
							// regionMapInv don't keep this record
							regionMapInv.remove(regionToReset);
						}
						// regionMap reset priority to its nodal priority
						regionMap.put(node, node.getPriority());

						// reset strategy
						S.remove(node);
					}
				}

				if (toNeutralize.size() != 0) {
					triggerStep(toNeutralize, Effect.NEUTRAL.toString(), -1, "reset nodes with region < " + p, false, true);
				}

				// update current subgame for next iter
				currentSubgame = computeSubGame(pg, p);
			} else {
				// the attractor is closed in the original game (globally closed)
				Map<Integer, Collection<Vertex>> dominion = new HashMap<>();
				dominion.put(player, attractor);

				triggerStep(attractor, Effect.SHADE.toString(), player, "globally closed, dominion found!", false, false);

				return dominion;
			}
		}
	}

	/**
	 * Given the currentSubgame, a base for attraction, and a list of node that is available (present in current subgame), and a player,
	 * return the list of attractor nodes.
	 */
	public Collection<Vertex> getAttractor(Game currentSubgame, Collection<Vertex> base, Collection<Vertex> available, int player){

		if (base.size() == available.size()) {
			return base;
		}

		Map<Vertex, List<Vertex>> inMap = currentSubgame.getInMap();

		List<Vertex> attractor = new ArrayList<>(base);      // shallow copy
		List<Vertex> queue = new ArrayList<>(base);   	     // shallow copy

		while (!queue.isEmpty()) {
			Vertex v = queue.remove(0);

			if (inMap.containsKey(v)) {

				for (Vertex u: inMap.get(v)) {

					// the node should be in current subgame, and not in current attractorList
					if (available.contains(u) && !attractor.contains(u)) {

						if (u.getOwner() == player) {
							// node belongs to player, can strategically move to v
							attractor.add(u);
							queue.add(u);

						} else {
							// node belongs to opponent,
							// if all of its successor (in current game) is contained in attractorList,
							// then the node have no choice but enter the attractor area, add it to attractor
							List<Vertex> whereCanOpponentGo = currentSubgame.getOutMap().get(u)
																			.stream()
															 				.filter(available::contains)   // in current subgame
																			.collect(Collectors.toList());

							if (attractor.containsAll(whereCanOpponentGo)) {
								attractor.add(u);
								queue.add(u);
							}
						}
						// set the strategy of the attracted node if it's player's node
						if (attractor.contains(u) && u.getOwner() == player && !S.containsKey(u)) {
							S.put(u, v);
						}
					}
				}
			}

		}
		// if player's node within attractor still don't have an strategy,
		// randomly pick a successor to move *within attractor*
		for (Vertex v : attractor){
			if (v.getOwner() == player && S.get(v) == null) {
				for (Vertex succ : currentSubgame.getOutMap().get(v)) {
					if (attractor.contains(succ)) {
						S.put(v, succ);
						break;
					}
				}
			}
		}
		return attractor;
	}

	/**
	 * Given the nodes that changed from last step, construct a step and add it to steps for later returning to client.
	 * @param focus: nodes that have gone through change from last step
	 * @param effect: the status that *focus* should change into
	 * @param priority: the priority of the focus nodes
	 * @param msg: the message of this step
	 */
	private void triggerStep(Collection<Vertex> focus, String effect, Integer priority, String msg, boolean setRegion, boolean resetRegion){

		// modify gameStatus
		for (Vertex v : focus) {
		    int id = v.getId();
		    if (setRegion) {
				gameStatus.get(id).put("region", "" + priority);
			} else if (resetRegion){
				// to reset region to nodal priority
				gameStatus.get(id).put("region", "" + v.getPriority());
			} else {
		    	// keep the region as it is
			}
			gameStatus.get(id).put("color", (priority % 2 == 0)? "even" : "odd");
			gameStatus.get(id).put("effect", effect);
			String strategy = (S.get(v) == null)? null : "" + S.get(v).getId();
			gameStatus.get(id).put("strategy", strategy);
		}
		GameStatus gameStatusCopy = gameStatus.getDeepCopy();

		// construct updateStatus
        GameStatus updateStatus = new GameStatus();
        for (Vertex v : focus) {
            int id = v.getId();
            // updateStatus share the same set of data with gameStatusCopy
            // they won't be changed later
            updateStatus.put(id, gameStatusCopy.get(id));
        }
		// construct Step object
		steps.add(new Step(gameStatusCopy, updateStatus, msg));
	}


	/**
	 * Compute the subgame after removing nodes with regional priority **larger** than p.
	 */
	public Game computeSubGame(Game pg, int p) {
		List<Vertex> toRemove = pg.getVertices().stream()
													 .filter(e -> regionMap.get(e) > p)
													 .collect(Collectors.toList());
		return computeSubgame(pg, toRemove);
	}

	/**
	 * Compute the subgame after removing a collection of nodes.
	 * This includes removing all the edges associated with the removed nodes.
	 */
	public Game computeSubgame(Game pg, Collection<Vertex> toRemove) {
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

	/**
	 * return the union of the successors for each node in nodes
	 */
	private Collection<Vertex> getSuccessorOfNodes(Collection<Vertex> nodes, Game pg){
		Collection<Vertex> result = new HashSet<>();
		for (Vertex node : nodes) {
			List<Vertex> succ = pg.getOutMap().get(node);
			result.addAll(succ);
		}
		return result;
	}


	private boolean noIntersection(Collection<Vertex> list1, Collection<Vertex> list2) {
		Collection<Vertex> intersection = list1.stream()
												 .filter(list2::contains)
												 .collect(Collectors.toList());
		return intersection.isEmpty();
	}

	/**
	 * Set the region priority of each node to the node's own priority
	 */
	private void resetRegionPriority(Game pg) {
		for (Vertex node: pg.getVertices()) {
			regionMap.put(node, node.getPriority());
		}
	}

	private int getHighestPriority(Game pg) {
		int p = -1;
		for (Vertex v : pg.getVertices()) {
			if (v.getPriority() > p) {
				p = v.getPriority();
			}
		}
		return p;
	}

	public String getWinner(Vertex v) {
	    return gameStatus.get(v.getId()).get("winner");
	}

	public String getStrategy(Vertex v){
		return gameStatus.get(v.getId()).get("strategy");
	}

	public Collection<Step> getSteps() {
		return steps;
	}


	public static Collection<Attribute> getAttributes() {
		Collection<Attribute> attributes = new ArrayList<>();
		// color
		Attribute color = new Attribute("color", Attribute.AttributeType.color, new ArrayList<>(Arrays.asList("even", "odd")));
		attributes.add(color);
		// region
		Attribute region = new Attribute("region", Attribute.AttributeType.text);
		attributes.add(region);
		return attributes;
	}
}
