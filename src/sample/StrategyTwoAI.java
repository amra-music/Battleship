package sample;

import java.util.concurrent.ThreadLocalRandom;

public class StrategyTwoAI extends SmartAI {
    private int[] arrayX = {1, 3, 5, 7, 9};
    private int[] arrayY = {0, 2, 4, 6, 8};
    private int availableCoordinates = 50;

    public StrategyTwoAI() {
    }

    public int getAvailableCoordinates() {
        return availableCoordinates;
    }

    public void setAvailableCoordinates(int availableCoordinates) {
        this.availableCoordinates = availableCoordinates;
    }

    public boolean isAvailableCoordinates(int x, int y) {
        return ((y % 2 == 0 && x % 2 == 1) || (x % 2 == 0 && y % 2 == 1));
    }

    private void setCoordinates() {
        if (availableCoordinates != 0) {
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
        } else {
            if(ThreadLocalRandom.current().nextInt(0, 2) == 0) {
                int randomX = ThreadLocalRandom.current().nextInt(0, 5);
                setX(arrayX[randomX]);
                int randomY = ThreadLocalRandom.current().nextInt(0, 5);
                setY(arrayX[randomY]);
            } else {
                int randomX = ThreadLocalRandom.current().nextInt(0, 5);
                setX(arrayY[randomX]);
                int randomY = ThreadLocalRandom.current().nextInt(0, 5);
                setY(arrayY[randomY]);
            }
        }
    }

    @Override
    public void nextMove(boolean isShot) {
        //If no boat found yet, pick random coordinate
        if (!isLastGuessHit() && getStackDirections().isEmpty()) {
            setCoordinates();
            return;
        }
        if (!isLastGuessHit() || isShot) {
            setLastX(getStartSearchX());
            setX(getLastX());
            setLastY(getStartSearchY());
            setY(getLastY());
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
                setCoordinates();
                setLastDirection(direction);
                return;
            } else {
                direction = getStackDirections().pop();
            }
        }
        setLastDirection(direction);
    }

    @Override
    public void feedback(boolean getHit, boolean getDestroy, int x, int y) {
        if(this.isAvailableCoordinates(x,y)) this.setAvailableCoordinates(this.getAvailableCoordinates()-1);
        super.feedback(getHit, getDestroy, x, y);
    }
}
