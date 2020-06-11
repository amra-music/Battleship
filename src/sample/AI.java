package sample;

public abstract class AI {
    private int x;
    private int y;
    abstract void nextMove(boolean isHit);


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

    public abstract void feedback(boolean b, boolean destroyed);
}
