package sample;

import java.util.concurrent.ThreadLocalRandom;

public class RandomAI extends AI {

    public RandomAI() {
    }

    public void nextMove(){
        setX(ThreadLocalRandom.current().nextInt(0,10));
        setY(ThreadLocalRandom.current().nextInt(0,10));
    }

    @Override
    public void feedback(boolean b, boolean destroyed) {
    }
}
