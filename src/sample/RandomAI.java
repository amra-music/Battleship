package sample;

import javafx.util.Pair;

import java.util.concurrent.ThreadLocalRandom;

public class RandomAI {
    private int x;
    private int y;

    public RandomAI() {
    }

    public Pair<Integer, Integer> nextMove(){
        this.x = ThreadLocalRandom.current().nextInt(0,10);
        this.y = ThreadLocalRandom.current().nextInt(0,10);
        return new Pair<>(x, y);
    }
}
