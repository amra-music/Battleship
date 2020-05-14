package sample;

public class Field {
    private double positionX;
    private double positionY;
    private boolean hit = false;

    public Field(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    @Override
    public String toString() {
        return "{" +
                "positionX= " + positionX +
                ", positionY= " + positionY +
                ", hit= " + hit +
                '}';
    }
}
