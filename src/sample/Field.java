package sample;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Field {
    private Rectangle rectangle;
    private double positionX;
    private double positionY;
    private boolean hit = false;
    private boolean occupied = false;

    public Field(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Field(Rectangle rectangle){
        this.rectangle = rectangle;
        this.positionX = rectangle.getLayoutX();
        this.positionY = rectangle.getLayoutY();
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

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void setColor(Color color) {
        rectangle.setFill(color);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public String toString() {
        return "{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", hit=" + hit +
                ", occupied=" + occupied +
                '}';
    }
}
