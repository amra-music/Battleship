package battleship;

import javafx.geometry.Orientation;

public class Ship {
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double firstPositionX;
    private double firstPositionY;
    private int size;
    private Orientation orientation;
    private int health;


    public Ship(double width, double firstPositionX, double firstPositionY) {
        //50  is width of board field
        this.size = (int) (width / 50);
        this.firstPositionX = firstPositionX;
        this.firstPositionY = firstPositionY;
        this.health = size;
    }

    public Ship(int size) {
        this.size = size;
        this.health = size;
    }

    public Ship() {
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
        if (orientation == Orientation.HORIZONTAL)
            this.endX = startX + (this.size - 1) * 50;
        else
            this.endX = startX;

    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
        if (orientation == Orientation.HORIZONTAL)
            this.endY = startY;
        else
            this.endY = startY + (this.size - 1) * 50;
    }

    public int getSize() {
        return size;
    }

    public void setSize(double width) {
        this.size = (int) (width / 50);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        if (orientation == 0)
            this.orientation = Orientation.HORIZONTAL;
        else
            this.orientation = Orientation.VERTICAL;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public double getFirstPositionX() {
        return firstPositionX;
    }

    public double getFirstPositionY() {
        return firstPositionY;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isDestroyed() {
        return health == 0;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "startX=" + startX +
                ",\n endX=" + endX +
                ",\n startY=" + startY +
                ",\n endY=" + endY +
                ",\n size=" + size +
                ",\n orientation=" + orientation +
                '}';
    }
}
