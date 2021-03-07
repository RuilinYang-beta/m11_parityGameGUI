package model;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private Collection<Vertex> vertices;
    private Map<Vertex, List<Vertex>> outMap;
    private  Map<Vertex, List<Vertex>> inMap;

    public Game(Collection<Vertex> vs, Map<Vertex, List<Vertex>> out) {
        this.vertices = vs;
        this.outMap = out;
        this.inMap = computeInMap(out);
    }

    private Map<Vertex, List<Vertex>> computeInMap(Map<Vertex, List<Vertex>> out){
        Map<Vertex, List<Vertex>> inMap = new HashMap<>();

        for (Map.Entry<Vertex, List<Vertex>> entry : out.entrySet()) {
            Vertex source = entry.getKey();
            List<Vertex> targets = entry.getValue();
            for (Vertex target : targets) {
                if (!inMap.containsKey(target)) {
                    inMap.put(target, new ArrayList<>(Arrays.asList(source)));
                } else {
                    inMap.get(target).add(source);
                }
            }
        }
        return inMap;
    }


    public Collection<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(Collection<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Map<Vertex, List<Vertex>> getOutMap() {
        return outMap;
    }

    public void setOutMap(Map<Vertex, List<Vertex>> outMap) {
        this.outMap = outMap;
    }

    public Map<Vertex, List<Vertex>> getInMap() {
        return inMap;
    }

    public void setInMap(Map<Vertex, List<Vertex>> inMap) {
        this.inMap = inMap;
    }

    public String toString(){
        List<Integer> vs = vertices.stream().map(e -> e.getId()).collect(Collectors.toList());
        String outString = "";
        for (Map.Entry<Vertex, List<Vertex>> e : outMap.entrySet()){
            outString += e.getKey();
            outString += " -> ";
            outString += e.getValue().toString();
            outString += "\n";
        }
        return "Parity game: \n" + vs.toString() + "\n" + outString;
    }

}
