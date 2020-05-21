package sample;

import javafx.util.Pair;

import java.util.concurrent.ThreadLocalRandom;

public class RandomAI extends MojaAI {

    public RandomAI() {
    }

    public Pair<Integer, Integer> nextMove(){
        setX(ThreadLocalRandom.current().nextInt(0,10));
        setY(ThreadLocalRandom.current().nextInt(0,10));
        return new Pair<>(getX(), getY());
    }
}
