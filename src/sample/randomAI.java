package sample;

import javafx.util.Pair;

import java.util.concurrent.ThreadLocalRandom;

public class randomAI {
    private int x;
    private int y;

    public randomAI() {
    }

    public Pair<Integer, Integer> nextMove(){
        this.x = ThreadLocalRandom.current().nextInt(0,10);
        this.y = ThreadLocalRandom.current().nextInt(0,10);
        return new Pair<>(x, y);

    }
}