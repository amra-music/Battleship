package battleship.AI;

import java.util.concurrent.ThreadLocalRandom;

public class StrategyOneAI extends SmartAI {

    public void nextMove(boolean isShot) {
        //If no boat found yet, pick random coordinate
        if (!isLastGuessHit() && getStackDirections().isEmpty()) {
            setX(ThreadLocalRandom.current().nextInt(0, 10));
            setY(ThreadLocalRandom.current().nextInt(0, 10));
            return;
        }
        if (!isLastGuessHit() || isShot) {
            setLastX(getStartSearchX());
            setX(getStartSearchX());
            setLastY(getStartSearchY());
            setY(getStartSearchY());
            tryToMove();
            return;
        }
        if (!getStackDirections().isEmpty()) {
            tryToMove();
            return;
        }
        //If hit, but stack is empty
        //Pick random direction
        int direction = ThreadLocalRandom.current().nextInt(0, 4);
        setStartSearchX(getLastX());
        setStartSearchY(getLastY());
        //Add all dirs to stack in random order
        int i = 4;
        while (i > 0) {
            getStackDirections().push(direction);
            direction++;
            if (direction == 4)
                direction = 0;
            i--;
        }
        tryToMove();
    }

    public void tryToMove() {
        if (getStackDirections().isEmpty()) {
            setLastGuessHit(false);
            return;
        }
        int direction = getStackDirections().pop();
        while (!move(direction)) {
            if (getStackDirections().isEmpty()) {
                setX(ThreadLocalRandom.current().nextInt(0, 10));
                setY(ThreadLocalRandom.current().nextInt(0, 10));
                setLastDirection(direction);
                return;
            } else {
                direction = getStackDirections().pop();
            }
        }
        setLastDirection(direction);
    }
}
