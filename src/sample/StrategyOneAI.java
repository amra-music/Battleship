package sample;

import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class StrategyOneAI extends AI {

    private int startSearchX = 0;
    private int startSearchY = 0;
    private int lastX = 0;
    private int lastY = 0;
    private boolean lastGuessHit = false;
    private Stack stackDirections = new Stack();
    private int lastDirection = 0;

    public StrategyOneAI() {}

    public int getStartSearchX() {
        return startSearchX;
    }

    public void setStartSearchX(int startSearchX) {
        this.startSearchX = startSearchX;
    }

    public int getStartSearchY() {
        return startSearchY;
    }

    public void setStartSearchY(int startSearchY) {
        this.startSearchY = startSearchY;
    }

    public int getLastX() {
        return lastX;
    }

    public void setLastX(int lastX) {
        this.lastX = lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public void setLastY(int lastY) {
        this.lastY = lastY;
    }

    public boolean isLastGuessHit() {
        return lastGuessHit;
    }

    public void setLastGuessHit(boolean lastGuessHit) {
        this.lastGuessHit = lastGuessHit;
    }

    public Stack getStackDirections() {
        return stackDirections;
    }

    public void setStackDirections(Stack stackDirections) {
        this.stackDirections = stackDirections;
    }

    public int getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(int lastDirection) {
        this.lastDirection = lastDirection;
    }

    public int nextX() {
        if (getX() == lastX && getY() == lastY) {
            System.out.println("Generise X ");
            this.nextMove();
        }
        System.out.println("Ne generise X");
        return getX();
    }

    public int nextY() {
        if (getX() == lastX && getY() == lastY) {
            System.out.println("Generise Y ");
            this.nextMove();
        }
        System.out.println("Ne generise Y");
        return getY();
    }

    public void nextMove() {
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

    public boolean move(int direction) {
        //Move North
        if (direction == 0) {
            if (getY() != 0) {
                setX(lastX);
                setY(lastY - 1);
                return true;
            } else {
                return false;
            }
        }
        //Move East
        if (direction == 1) {
            if (getX() != 9) {
                setX(lastX + 1);
                setY(lastY);
                return true;
            } else {
                return false;
            }
        }
        //Move South
        if (direction == 2) {
            if (getY() != 9) {
                setX(lastX);
                setY(lastY + 1);
                return true;
            } else {
                return false;
            }
        }
        //direction == 3. Move West
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
        nextMove();
    }

    void feedback(boolean getHit, boolean getDestroy) {
            if (getDestroy) {
                stackDirections.clear();
                lastX = 0;
                lastY = 0;
                setY(0);
                setX(0);
                lastDirection = 0;
                lastGuessHit = false;
                return;
            }

        lastX = getX();
        lastY = getY();

        //If hit and we can keep going in the direction we just came from, keep searching in that direction
        if (getHit) {
            lastGuessHit = true;
            if (!stackDirections.isEmpty()) {
                int direction = lastDirection;
                if (direction == 0 && lastY == 0) {
                    return;
                }
                if (direction == 1 && lastX == 9) {
                    return;
                }
                if (direction == 2 && lastY == 9) {
                    return;
                }
                if (direction == 3 && lastX == 0) {
                    return;
                }
                //Add direction to keep searching in current direction
                stackDirections.push(direction);
            }
            return;
        } else {
            lastGuessHit = false;
        }
    }
}
