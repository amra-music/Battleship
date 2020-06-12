package sample;

public abstract class DummyAI {
    private int x;
    private int y;

    public DummyAI() {
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

    abstract public void nextMove();
}
