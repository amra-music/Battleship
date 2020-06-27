package battleship.AI;

import java.util.Stack;

public abstract class SmartAI {
    private int x;
    private int y;
    private int startSearchX = 0;
    private int startSearchY = 0;
    private int lastX = 0;
    private int lastY = 0;
    private boolean lastGuessHit = false;
    private Stack<Integer> stackDirections = new Stack<>();
    private int lastDirection = 0;

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

    public Stack<Integer> getStackDirections() {
        return stackDirections;
    }

    public void setStackDirections(Stack<Integer> stackDirections) {
        this.stackDirections = stackDirections;
    }

    public int getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(int lastDirection) {
        this.lastDirection = lastDirection;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void reset() {
        startSearchX = 0;
        startSearchY = 0;
        lastX = 0;
        lastY = 0;
        setX(0);
        setY(0);
        lastGuessHit = false;
        stackDirections.empty();
        lastDirection = 0;
        nextMove(false);
    }

    public void feedback(boolean getHit, boolean getDestroy, int x, int y) {
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
        setLastX(getX());
        setLastY(getY());

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
        } else {
            lastGuessHit = false;
        }
    }

    public boolean move(int direction) {
        //Move North
        if (direction == 0) {
            if (getY() != 0) {
                setX(getLastX());
                setY(getLastY() - 1);
                return true;
            } else {
                return false;
            }
        }
        //Move East
        if (direction == 1) {
            if (getX() != 9) {
                setX(getLastX() + 1);
                setY(getLastY());
                return true;
            } else {
                return false;
            }
        }
        //Move South
        if (direction == 2) {
            if (getY() != 9) {
                setX(getLastX());
                setY(getLastY() + 1);
                return true;
            } else {
                return false;
            }
        }
        //direction == 3. Move West
        if (getX() != 0) {
            setX(getLastX() - 1);
            setY(getLastY());
            return true;
        } else {
            return false;
        }
    }

    abstract public void nextMove(boolean isShot);
}
