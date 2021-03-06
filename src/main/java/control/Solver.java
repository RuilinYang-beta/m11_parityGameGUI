package control;

import algorithms.Algorithm;
import algorithms.*;
import modelGame.Game;
import modelStep.Step;
import modelGame.Vertex;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.PGLexer;
import parser.PGParser;
import parser.PGParser.EdgeContext;
import parser.PGParser.LineContext;
import parser.PGParser.RootContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;

public class Solver {

    /**
     * Pares a gameString to a Game object, and feed it to the given algorithm.
     */
    public static Collection<Step> process(String gameString, Algorithm algorithm, boolean isFilename) {
        Game game;

        // first parse the file (given on input)
        try {
            game = parse(gameString, isFilename);
            if (game == null) return null; // in case of a parsing error
        } catch (FileNotFoundException e) {
            System.err.println("File " + gameString + " not found!");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("I/O Exception while reading " + gameString + ": " + e.getMessage());
            return null;
        }

        // now we have the vertices, solve the parity game with the given algorithm
        algorithm.solve(game);

        // to print the solution in the console to have a quick look
        System.out.println("winnerMap");
        for (Vertex v : game.getVertices()){
            System.out.println("" + v + " won by " + algorithm.getWinner(v));
        }
        System.out.println("strategyMap");
        for (Vertex v : game.getVertices()) {
            System.out.println("" + v + " -> " + algorithm.getStrategy(v));
        }

        return algorithm.getSteps();
    }


    /**
     * takes the string of .pg file, transform it into a Game object.
     */
    public static Game parse(String gameString, boolean isFilename) throws IOException {

        InputStream stream = null;

        if (isFilename) {
            // when gameString is the path to a file, use for local testing
            Path filePath = Path.of(gameString);
            String content = Files.readString(filePath);
            System.out.println(content);
            stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        } else {
            // when gameString is the string content of a .pg file
            stream = new ByteArrayInputStream(gameString.getBytes(StandardCharsets.UTF_8));
        }

        PGLexer l = new PGLexer(CharStreams.fromStream(stream));
        PGParser p = new PGParser(new CommonTokenStream(l));

        RootContext s = p.root();
        int count = Integer.parseInt(s.header().count().getText());

        // sometimes "count" is not actually the count, but the id of highest vertex :-/

        Map<Integer, Vertex> verticesMap = new HashMap<>();
        Map<Vertex, List<Integer>> edges = new HashMap<>();  // temp map for constructing outMap
        Map<Vertex, List<Vertex>> outMap = new HashMap<>();


        for (LineContext lineCtx : s.line()) {
            int id = Integer.parseInt(lineCtx.id().getText());
            int priority = Integer.parseInt(lineCtx.priority().getText());
            int owner = Integer.parseInt(lineCtx.owner().getText());
            String label = lineCtx.label().getText();

            if (verticesMap.get(id) != null) {
                System.err.println("vertex with id " + id + " appears twice!");
                return null; // bye
            }

            Vertex v = new Vertex(id, priority, owner, label);
            verticesMap.put(id, v);
            edges.put(v, new ArrayList<>());
            outMap.put(v, new ArrayList<>());

            for (EdgeContext edgeCtx : lineCtx.edge()) {
                int target = Integer.parseInt(edgeCtx.getText());
                edges.get(v).add(target);
            }
        }

        /*
         * it's a directed graph with per source-target pair at most 1 directed edge
         * so we can actually store the edges as a list of target vertices
         * we also want to store the reverse direction, so all incoming edges source vertices
         * because this is very useful for the algorithms
         *
         * we actually process the edge array after reading all vertices
         */
        for (Entry<Vertex, List<Integer>> edgeEntry : edges.entrySet()) {
            Vertex source = edgeEntry.getKey();
            for (int targetId : edgeEntry.getValue()) {
                outMap.get(source).add(verticesMap.get(targetId));
            }
        }

        return new Game(verticesMap.values(), outMap);
    }


    /**
     * To have a quick test on parsing and solving a game.
     */
    public static void main(String[] args) {
//        // ===== option 1: test using a gameString =====
//        String testGameString = "parity 5;\n" +
//                "38 0 1 38,39;\n" +
//                "39 4 1 42,38;\n" +
//                "40 4 1 41;\n" +
//                "41 5 0 38,40;\n" +
//                "42 6 0 41;";
//
//        process(testGameString, new DFI(), false);

        // ===== option 2: test using a file path =====
        File f = new File("src/games/test001.pg");
        if (f.isDirectory()) {
            File[] files = f.listFiles((dir, name) -> name.endsWith(".pg"));

            if (files != null) {
                for (File file : files) {
                    process(file.getPath(), new DFI(), true);
                }
            }
        } else {
            process(f.getPath(), new DFI(),true);
        }

    }
}
