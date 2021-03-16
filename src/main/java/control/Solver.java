//package control;
//
//import algorithms.Algorithm;
////import algorithms.PriorityPromotion;
//import model.Game;
//import model.Step;
//import model.Vertex;
//import org.antlr.v4.runtime.CharStreams;
//import org.antlr.v4.runtime.CommonTokenStream;
//import parser.PGLexer;
//import parser.PGParser;
//import parser.PGParser.EdgeContext;
//import parser.PGParser.LineContext;
//import parser.PGParser.RootContext;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.*;
//import java.util.Map.Entry;
//
//public class Solver {
//
//    // TODO: set strategy in dominion:
//    // in dominion of even player, set the strategy of even node to go to nodes within dominion
//    // (player choose don't escape)
//
//    /**
//     * Pares a gameString to a Game object, feed it to an algorithm, and write the output
//     */
//    public static Collection<Step> process(String gameString, boolean isFilename) {
//        Game game;
//
//        // first parse the file (given on input)
//        try {
//            game = parse(gameString, isFilename);
//            if (game == null) return null; // in case of a parsing error
//        } catch (FileNotFoundException e) {
//            System.err.println("File " + gameString + " not found!");
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("I/O Exception while reading " + gameString + ": " + e.getMessage());
//            return null;
//        }
//
//        // now we have the vertices, solve the parity game with the freezing-fixed-point-iteration algorithm
//        Algorithm algorithm = new PriorityPromotion();
//        algorithm.solve(game);
//
//        // temp
//        System.out.println("winnerMap");
//        for (var entry : algorithm.getWinnerMap().entrySet()) {
//            System.out.println(entry);
//        }
//        System.out.println("strategyMap");
//        for (var entry : algorithm.getStrategyMap().entrySet()) {
//            System.out.println(entry);
//        }
//
//
//        return algorithm.getSteps();
//
////        try {
////            writeSolution(game, solver, gameString + "sol");
////        } catch (FileNotFoundException e) {
////            System.err.println("Cannot open " + gameString + " for writing!");
////        }
//    }
//
//
//    /**
//     * takes the string of .pg file, transform it into a Game.
//     */
//    public static Game parse(String gameString, boolean isFilename) throws IOException {
//
//        InputStream stream = null;
//
//        if (isFilename) {
//            Path filePath = Path.of(gameString);
//            String content = Files.readString(filePath);
//            System.out.println(content);
//            stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
//        } else {
//            stream = new ByteArrayInputStream(gameString.getBytes(StandardCharsets.UTF_8));
//        }
//
//        PGLexer l = new PGLexer(CharStreams.fromStream(stream));
//        PGParser p = new PGParser(new CommonTokenStream(l));
//
//        RootContext s = p.root();
//        int count = Integer.parseInt(s.header().count().getText());
//
//        // sometimes "count" is not actually the count, but the id of highest vertex :-/
//
//        Map<Integer, Vertex> verticesMap = new HashMap<>();
//        Map<Vertex, List<Integer>> edges = new HashMap<>();  // temp map for constructing outMap
//        Map<Vertex, List<Vertex>> outMap = new HashMap<>();
//
//
//        for (LineContext lineCtx : s.line()) {
//            int id = Integer.parseInt(lineCtx.id().getText());
//            int priority = Integer.parseInt(lineCtx.priority().getText());
//            int owner = Integer.parseInt(lineCtx.owner().getText());
//            String label = lineCtx.label().getText();
//
//            if (verticesMap.get(id) != null) {
//                System.err.println("vertex with id " + id + " appears twice!");
//                return null; // bye
//            }
//
//            Vertex v = new Vertex(id, priority, owner, label);
//            verticesMap.put(id, v);
//            edges.put(v, new ArrayList<>());
//            outMap.put(v, new ArrayList<>());
//
//            for (EdgeContext edgeCtx : lineCtx.edge()) {
//                int target = Integer.parseInt(edgeCtx.getText());
//                edges.get(v).add(target);
//            }
//        }
//
//        /*
//         * it's a directed graph with per source-target pair at most 1 directed edge
//         * so we can actually store the edges as a list of target vertices
//         * we also want to store the reverse direction, so all incoming edges source vertices
//         * because this is very useful for the algorithms
//         *
//         * we actually process the edge array after reading all vertices
//         */
//        for (Entry<Vertex, List<Integer>> edgeEntry : edges.entrySet()) {
//            Vertex source = edgeEntry.getKey();
//            for (int targetId : edgeEntry.getValue()) {
//                outMap.get(source).add(verticesMap.get(targetId));
//            }
//        }
//
//        return new Game(verticesMap.values(), outMap);
//    }
//
////    private static void writeSolution(Collection<Vertex> vertices, DFI solver, String filename) throws FileNotFoundException {
////        try (PrintWriter pw = new PrintWriter(filename)) {
////            pw.printf("paritysol %d\n", vertices.size());
//////            for (var v : vertices) {
////            for (Vertex v : vertices) {
////                int winner = solver.getWinner(v);
////                Vertex strategy = solver.getStrategy(v);
////                if (winner == v.getOwner()) {
////                    if (strategy == null) System.err.printf("Missing strategy for vertex %d!\n", v.getId());
////                    else if (solver.getWinner(strategy) != winner) System.err.printf("Wrong winner for strategy %d->%d!", v.getId(), strategy.getId());
////                } else {
////                    if (strategy != null) System.err.printf("Unexpected strategy for vertex %d!\n", v.getId());
////                }
////                if (strategy == null) pw.printf("%d %d\n", v.getId(), winner);
////                else pw.printf("%d %d %d\n", v.getId(), winner, strategy.getId());
////            }
////        }
////    }
//
//
//
//    public static void main(String[] args) {
////        // example from slides
////        String testGameString = "parity 9;\n" +
////                "0 0 0 1;\n" +
////                "1 2 1 0,5;\n" +
////                "2 7 0 1,6;\n" +
////                "3 1 1 2,4;\n" +
////                "4 5 0 3,8;\n" +
////                "5 8 0 6;\n" +
////                "6 6 0 7;\n" +
////                "7 2 0 3,8;\n" +
////                "8 3 0 4,7;";
////        process(testGameString, false);
//
//        File f = new File("src/games/test020.pg");
//        if (f.isDirectory()) {
////        	var files = f.listFiles((dir, name) -> name.endsWith(".pg"));
//            File[] files = f.listFiles((dir, name) -> name.endsWith(".pg"));
//
//
//            if (files != null) {
////            	for (var file : files) {
//                for (File file : files) {
//                    process(file.getPath(), true);
//                }
//            }
//        } else {
//            process(f.getPath(), true);
//        }
//    }
//}
