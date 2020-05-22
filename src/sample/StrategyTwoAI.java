package sample;

import java.util.concurrent.ThreadLocalRandom;

public class StrategyTwoAI extends StrategyOneAI {
    private int[] arrayX = {1, 3, 5, 7, 9};
    private int[] arrayY = {0, 2, 4, 6, 8};

    public StrategyTwoAI() {
    }

    private void setCoordinates() {
        if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {
            int randomX = ThreadLocalRandom.current().nextInt(0, 5);
            setX(arrayX[randomX]);
            int randomY = ThreadLocalRandom.current().nextInt(0, 5);
            setY(arrayY[randomY]);
        } else {
            int randomX = ThreadLocalRandom.current().nextInt(0, 5);
            setX(arrayY[randomX]);
            int randomY = ThreadLocalRandom.current().nextInt(0, 5);
            setY(arrayX[randomY]);
        }
    }

    public void nextMove() {
        //If no boat found yet, pick random coordinate
        if (!isLastGuessHit() && getStackDirections().isEmpty()) {
            setCoordinates();
            return;
        }
        if (!isLastGuessHit() && !getStackDirections().isEmpty()) {
            setLastX(getStartSearchX());
            setX(getLastX());
            setLastY(getStartSearchY());
            setY(getLastY());
            tryToMove();
            return;
        }
        if (isLastGuessHit() && !getStackDirections().isEmpty()) {
            tryToMove();
            return;
        }
        //If hit, but stack is empty
        if (isLastGuessHit() && getStackDirections().isEmpty()) {
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
    }


    public void tryToMove() {
        int direction = (int) getStackDirections().pop();
        while (!move(direction)) {
            if (getStackDirections().isEmpty()) {
                setCoordinates();
                setLastDirection(direction);
                return;
            } else {
                direction = (int) getStackDirections().pop();
            }
        }
        setLastDirection(direction);
    }
}
