package test;

import algorithms.Algorithm;
import algorithms.Zielonka;
import control.Solver;
import modelGame.Game;
import modelGame.Vertex;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.junit.Assert.*;

public class ZielonkaTest {

    private final String BASE = "src/games";
    private Algorithm algo;
    private Algorithm bm;

    @BeforeEach
    public void init(){
        algo = new Zielonka();
        bm = new Benchmark();
    }

    @Test
    public void test0() throws IOException {
        init();
        String game = "/test001.pg";
        test(game);
    }

    @Test
    public void test1() throws IOException {
        init();
        String game = "/test011.pg";
        test(game);
    }

    @Test
    public void test2() throws IOException {
        init();
        String game = "/test021.pg";
        test(game);
    }

    @Test
    public void test3() throws IOException {
        init();
        String game = "/test031.pg";
        test(game);
    }

    @Test
    public void test4() throws IOException {
        init();
        String game = "/test041.pg";
        test(game);
    }

    @Test
    public void test5() throws IOException {
        init();
        String game = "/test051.pg";
        test(game);
    }

    @Test
    public void test6() throws IOException {
        init();
        String game = "/test061.pg";
        test(game);
    }

    @Test
    public void test7() throws IOException {
        init();
        String game = "/test071.pg";
        test(game);
    }

    @Test
    public void test8() throws IOException {
        init();
        String game = "/test081.pg";
        test(game);
    }

    @Test
    public void test9() throws IOException {
        init();
        String game = "/test091.pg";
        test(game);
    }

    private void test(String game) throws IOException {
        Game pg = Solver.parse(BASE + game, true);
        algo.solve(pg);
        bm.solve(pg);

        for (Vertex v : pg.getVertices()){
            assertEquals(algo.getWinner(v), bm.getWinner(v));
            assertEquals("" + algo.getStrategy(v), "" + bm.getStrategy(v));
        }
    }

}