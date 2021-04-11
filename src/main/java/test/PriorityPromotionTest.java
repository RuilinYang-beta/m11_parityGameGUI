package test;

import algorithms.Algorithm;
import algorithms.PriorityPromotion;
import control.Solver;
import modelGame.Game;
import modelGame.Vertex;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.junit.Assert.*;

public class PriorityPromotionTest {

    private final String BASE = "src/games";
    private Algorithm algo = new PriorityPromotion();
    private Algorithm bm = new Benchmark();

    @BeforeEach
    public void init(){
        algo = new PriorityPromotion();
        bm = new Benchmark();
    }

    @Test
    public void test0() throws IOException {
        String game = "/test001.pg";
        test(game);
    }

    @Test
    public void test1() throws IOException {
        String game = "/test011.pg";
        test(game);
    }

    @Test
    public void test2() throws IOException {
        String game = "/test021.pg";
        test(game);
    }

    @Test
    public void test3() throws IOException {
        String game = "/test031.pg";
        test(game);
    }

    @Test
    public void test4() throws IOException {
        String game = "/test041.pg";
        test(game);
    }

    @Test
    public void test5() throws IOException {
        String game = "/test051.pg";
        test(game);
    }

    @Test
    public void test6() throws IOException {
        String game = "/test061.pg";
        test(game);
    }

    @Test
    public void test7() throws IOException {
        String game = "/test071.pg";
        test(game);
    }

    @Test
    public void test8() throws IOException {
        String game = "/test081.pg";
        test(game);
    }

    @Test
    public void test9() throws IOException {
        String game = "/test091.pg";
        test(game);
    }

    private void test(String game) throws IOException {
        Game pg = Solver.parse(BASE + game, true);
        algo.solve(pg);
        bm.solve(pg);

        for (Vertex v : pg.getVertices()){
            assertEquals(algo.getWinner(v), bm.getWinner(v));
            assertEquals(algo.getStrategy(v), bm.getStrategy(v));
        }
    }
}