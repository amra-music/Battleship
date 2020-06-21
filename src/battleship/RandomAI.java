package battleship;

import java.util.concurrent.ThreadLocalRandom;

public class RandomAI extends DummyAI {

    public RandomAI() {}

    public void nextMove(){
        setX(ThreadLocalRandom.current().nextInt(0,10));
        setY(ThreadLocalRandom.current().nextInt(0,10));
    }
}
