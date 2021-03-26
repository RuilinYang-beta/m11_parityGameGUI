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
    // for returning to the client
	private final List<Step> steps = new ArrayList<>();
    // for recording the current status of the whole game
    private final GameStatus gameStatus = new GameStatus();

    private boolean solved = false;

    // attributes of the algorithm
	private Collection<Attribute> attributes = new ArrayList<>();



	public void solve(Game pg){
		init(pg);

		List<Vertex> allNodes = new ArrayList<>(pg.getVertices());
		Game currentSubgame = pg;

		while (!allNodes.isEmpty()) {
			// find a dominion, and extend it with its attractors
			Map<Integer, Collection<Vertex>> dominionMap = findDominion(currentSubgame);
			Map.Entry<Integer, Collection<Vertex>> entry = dominionMap.entrySet().iterator().next(); // the first and only entry
			int player = entry.getKey();
			Collection<Vertex> dominion = entry.getValue();
			Collection<Vertex> dominionAndItsAttractor = getAttractor(currentSubgame, dominion, currentSubgame.getVertices(), player);

			// set winner and strategy of each node
			for (Vertex v : dominionAndItsAttractor) {
				int id = v.getId();
			    // record winners for  each node
				gameStatus.get(id).put("winner", "" + player);

				// set strategy for nodes within dominionAndItsAttractor if node owner and winner is the same
				// the strategy is to move within dominionAndItsAttractor
				if (v.getOwner() == player) {
					for (Vertex out : pg.getOutMap().get(v)) {
						if (dominionAndItsAttractor.contains(out)) {
							gameStatus.get(id).put("strategy", "" + out.getId());
							break;
						}
					}
				}
			}

            triggerStep(dominionAndItsAttractor, Effect.HIGHLIGHT.toString(), player, "dominion and its attractor, with strategy");

			// update subgame by removing all the node and relevant edges of dominionAndItsAttractor from currentSubgame
			allNodes.removeAll(dominionAndItsAttractor);
			currentSubgame = computeSubgame(currentSubgame, dominionAndItsAttractor);

			triggerStep(dominionAndItsAttractor, Effect.SHADE.toString(), player, "");
		}
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
			// init regional priority is node priority
			nodeStatus.put("color", "" + v.getPriority());
			nodeStatus.put("region", "" + v.getPriority());
			// init color is its priority's color
			nodeStatus.put("color", (v.getPriority() % 2 == 0)? "even" : "odd");
			// init visual effect is neutral, when neutral, color & region attribute doesn't matter
			nodeStatus.put("effect", Effect.NEUTRAL.toString());
			// the below two attr is wait to be set
			nodeStatus.put("strategy", null);
			nodeStatus.put("winner", null);
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

			triggerStep(base, Effect.HIGHLIGHT.toString(),  p, "base");

			// within current subgame,
			// for player's node:   if it can go to base?  add
			// for opponent's node: if it must go to base? add
			Collection<Vertex> attractor = getAttractor(currentSubgame, base, available, player);

			triggerStep(attractor, Effect.HIGHLIGHT.toString(), p, "attractor");

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

				triggerStep(attractor, Effect.SHADE.toString(), p,"locally open, set regional priority of attractor");

				// update currentSubgame and p, continue with the next highest priority
				currentSubgame = computeSubgame(currentSubgame, attractor);
				p = getHighestPriority(currentSubgame);

			} else if (!opponentEscapeSet.isEmpty()) {
				// the attractor is closed in current subgame, but open in original game (locally closed, globally open)
				// promote attractor, reset region priority for some nodes
				p = getLowestRegionalPriority(new ArrayList<>(opponentEscapeSet));
				// promote the attractor to the lowest region that it can go to
				for (Vertex node : attractor) {
					regionMap.put(node, p);
				}
				regionMapInv.get(p).addAll(attractor);

				triggerStep(regionMapInv.get(p), Effect.HIGHLIGHT.toString(), p, "locally closed & globally open, promote the attractor");

				// reset the region priority of the  nodes with region < p
				Collection<Vertex> toNeutralize = new ArrayList<>();
				for (Vertex node : pg.getVertices()) {
					if (regionMap.get(node) < p) {
						int regionToReset = regionMap.get(node);
						// reset region
						if (regionMapInv.containsKey(regionToReset)) {
							toNeutralize.addAll(regionMapInv.get(regionToReset));
							regionMapInv.remove(regionToReset);
						}
						regionMap.put(node, node.getPriority());
					}
				}

				if (toNeutralize.size() != 0) {
					triggerStep(toNeutralize, Effect.NEUTRAL.toString(), -1, "reset nodes with region < " + p);
				}

				// update current subgame for next iter
				currentSubgame = computeSubGame(pg, p);
			} else {
				// the attractor is closed in the original game (globally closed)
				Map<Integer, Collection<Vertex>> dominion = new HashMap<>();
				dominion.put(player, attractor);

				triggerStep(attractor, Effect.SHADE.toString(), player, "globally closed, dominion found!");

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
	private void triggerStep(Collection<Vertex> focus, String effect, Integer priority, String msg){
		System.out.println("In trigger step!");
		System.out.println("" + focus + "; " + effect + "; " + priority + "; " + msg);

		// modify gameStatus
		for (Vertex v : focus) {
		    int id = v.getId();
			gameStatus.get(id).put("region", "" + priority);
			gameStatus.get(id).put("color", (priority % 2 == 0)? "even" : "odd");
			gameStatus.get(id).put("effect", effect);
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

	private int getLowestRegionalPriority(List<Vertex> nodes) {
		int p = regionMap.get(nodes.get(0));
		for (int i = 1; i < nodes.size(); i++) {
			int region = regionMap.get(nodes.get(i));
			if (region < p) {
				p = region;
			}
		}
		return p;
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


	// todo: to be generalized
	public Collection<Attribute> getAttributes() {
		List<String> colorValues = new ArrayList<>();
		colorValues.add("even");
		colorValues.add("odd");

		Attribute color = new Attribute("color", Attribute.AttributeType.color, colorValues);
		this.attributes.add(color);

		return this.attributes;
	}
}
