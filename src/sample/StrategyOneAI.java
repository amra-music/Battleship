package sample;

import javafx.util.Pair;

import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class StrategyOneAI extends MojaAI {

    private int startSearchX = 0;
    private int startSearchY = 0;
    private int lastX = 0;
    private int lastY = 0;
    private boolean lastGuessHit = false;
    private Stack stackDirections = new Stack();
    private int lastDirection = 0;

    public StrategyOneAI() {
    }
    public int nextX() {
        if (getX() == lastX && getY() == lastY) {
            this.generate();
        }
        return getX();
    }

    public int nextY() {
        if (getX() == lastX && getY() == lastY) {
            this.generate();
        }
        return getY();
    }

    public void generate() {
        //If no boat found yet, pick random coordinate
        if (!lastGuessHit && stackDirections.isEmpty()) {
            setX(ThreadLocalRandom.current().nextInt(0, 10));
            setY(ThreadLocalRandom.current().nextInt(0, 10));
            return;
        }
        if (!lastGuessHit && !stackDirections.isEmpty()) {
            lastX = startSearchX;
            setX(lastX);
            lastY = startSearchY;
            setY(lastY);
            tryToMove();
            return;
        }
        if (lastGuessHit && !stackDirections.isEmpty()) {
            tryToMove();
            return;
        }
        //If hit, but stack is empty
        if (lastGuessHit && stackDirections.isEmpty()) {
            //Pick random direction
            int direction = ThreadLocalRandom.current().nextInt(0, 4);
            startSearchX = lastX;
            startSearchY = lastY;
            //Add all dirs to stack in random order
            int i = 4;
            while (i > 0) {
                stackDirections.push(direction);
                direction++;
                if (direction == 4)
                    direction = 0;
                i--;
            }
            tryToMove();
            return;
        }
    }

    public void tryToMove() {
        int direction = (int) stackDirections.pop();
        while (!move(direction)) {
            if (stackDirections.isEmpty()) {
                setX(ThreadLocalRandom.current().nextInt(0, 10));
                setY(ThreadLocalRandom.current().nextInt(0, 10));
                lastDirection = direction;
                return;
            } else {
                direction = (int) stackDirections.pop();
            }
        }
        lastDirection = direction;
    }

    public boolean move(int dir) {
        //Move North
        if (dir == 0) {
            if (getY() != 0) {
                setX(lastX);
                setY(lastY - 1);
                return true;
            } else {
                return false;
            }
        }
        //Move East
        if (dir == 1) {
            if (getX() != 9) {
                setX(lastX + 1);
                setY(lastY);
                return true;
            } else {
                return false;
            }
        }
        //Move South
        if (dir == 2) {
            if (getY() != 9) {
                setX(lastX);
                setY(lastY + 1);
                return true;
            } else {
                return false;
            }
        }
        //dir == 3. Move West
        if (getX() != 0) {
            setX(lastX - 1);
            setY(lastY);
            return true;
        } else {
            return false;
        }
    }
    void reset() {
        startSearchX = 0;
        startSearchY = 0;
        lastX = 0;
        lastY = 0;
        setX(0);
        setY(0);
        lastGuessHit = false;
        stackDirections.empty();
        lastDirection = 0;
        generate();
    }


    @Override
    Pair<Integer, Integer> nextMove() {
        return null;
    }
}
