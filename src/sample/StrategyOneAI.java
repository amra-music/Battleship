package sample;

import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class StrategyOneAI extends AI {

    private int startSearchX = 0;
    private int startSearchY = 0;
    private int lastX = 0;
    private int lastY = 0;
    private boolean lastGuessHit = false;
    private Stack<Integer> stackDirections = new Stack<>();
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

    public int nextX() {
        if (getX() == lastX && getY() == lastY) {
            System.out.println("Generise X ");
            this.nextMove(false);
        }
        System.out.println("Ne generise X");
        return getX();
    }

    public int nextY() {
        if (getX() == lastX && getY() == lastY) {
            System.out.println("Generise Y ");
            this.nextMove(false);
        }
        System.out.println("Ne generise Y");
        return getY();
    }
    @Override
    public void nextMove(boolean isHit) {
        //If no boat found yet, pick random coordinate
        if (!lastGuessHit && stackDirections.isEmpty()) {
            setX(ThreadLocalRandom.current().nextInt(0, 10));
            setY(ThreadLocalRandom.current().nextInt(0, 10));
            System.out.println("Nije brod nađen, radnom X:"+getX()+" Y:"+getY());
            return;
        }
        if (!lastGuessHit || isHit) {
            System.out.println("Pogođen je brod, ali je fula prilikom unistavanja");
            setLastX(startSearchX);
            setX(startSearchX);
            setLastY(startSearchY);
            setY(startSearchY);
            tryToMove();
            return;
        }
        if (!stackDirections.isEmpty()) {
            tryToMove();
            return;
        }
        //If hit, but stack is empty
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
        System.out.println("Pogodjen brod, smjerovi za unistenje su: "+stackDirections);
        tryToMove();
    }

    public void tryToMove() {
        System.out.println("Pokusaj da se pomjeris");
        if (stackDirections.isEmpty()) {
            setLastGuessHit(false);
            return;
        }
        int direction = stackDirections.pop();
        while (!move(direction)) {
            // nije na ploci
            /*
            setLastX(startSearchX);
            setX(startSearchX);
            setLastY(startSearchY);
            setY(startSearchY);
             */
            if (stackDirections.isEmpty()) {
                setX(ThreadLocalRandom.current().nextInt(0, 10));
                setY(ThreadLocalRandom.current().nextInt(0, 10));
                lastDirection = direction;
                return;
            } else {
                direction = stackDirections.pop();
            }
        }
        lastDirection = direction;
    }

    public boolean move(int direction) {
        //Move North
        if (direction == 0) {
            System.out.println("Sjever0 X "+lastX + "Y: " + lastY + " Trenutni X "+ getX()+ " Y "+ getY());
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
            System.out.println("Istok1 X "+lastX + "Y: " + lastY + " Trenutni X "+ getX()+ " Y "+ getY());
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
            System.out.println("Jug2 X "+lastX + "Y: " + lastY+ " Trenutni X "+ getX()+ " Y "+ getY());
            if (getY() != 9) {
                setX(lastX);
                setY(lastY + 1);
                return true;
            } else {
                return false;
            }
        }
        //direction == 3. Move West
        System.out.println("Zapad3 X "+lastX + "Y: " + lastY + " Trenutni X "+ getX()+ " Y "+ getY());
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
        nextMove(false);
    }
    @Override
    public void feedback(boolean getHit, boolean getDestroy) {
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
            return;
        } else {
            lastGuessHit = false;
        }
    }
}
