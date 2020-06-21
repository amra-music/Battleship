package battleship;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Field {
    private Rectangle rectangle;
    private double positionX;
    private double positionY;
    private boolean shot = false;
    private boolean occupied = false;
    private Ship ship = new Ship();


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

    public boolean isShot() {
        return shot;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
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

    public Paint getColor() { return rectangle.getFill(); }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void reset(){
        shot = false;
        occupied = false;
        setColor(Color.DODGERBLUE);
        rectangle.setDisable(false);
        ship = new Ship();
    }

    @Override
    public String toString() {
        return "{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", hit=" + shot +
                ", occupied=" + occupied +
                '}';
    }
}
