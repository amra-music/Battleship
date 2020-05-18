package sample;

import javafx.geometry.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Board {
    private List<List<Field>> fields = new ArrayList<>();
    private List<Ship> ships = new ArrayList<>();
    private int health;

    public Board() {
    }

    public Board(List<Rectangle> fields) {
        setFields(fields);
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public void removeShip(Ship ship) {
        ships.remove(ship);
    }

    public void removeShips() {
        ships.clear();
    }

    public List<Ship> getShips() {
        return ships;
    }

    public List<List<Field>> getFields() {
        return fields;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void resetBoard() {
        //postavi na plavo, da nije occupied, nije hit
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Field field = fields.get(i).get(j);
                field.reset();
            }
        }
        removeShips();
        health = 0;
    }

    public void setOccupiedFields() {
        ships.forEach(ship -> {
            health += ship.getSize();
            if (ship.getOrientation() == Orientation.HORIZONTAL) {
                int rowNumber = (int) ship.getStartY() / 50;
                for (int row = rowNumber; row <= rowNumber; row++) {
                    for (int column = (int) ship.getStartX() / 50; column <= ship.getEndX() / 50; column++) {
                        fields.get(row).get(column).setOccupied(true);
                    }
                }
            } else {
                int columnNumber = (int) ship.getStartX() / 50;
                for (int column = columnNumber; column <= columnNumber; column++) {
                    for (int row = (int) ship.getStartY() / 50; row <= ship.getEndY() / 50; row++) {
                        fields.get(row).get(column).setOccupied(true);
                    }
                }
            }
        });
    }

    public boolean setOccupiedFields(Ship ship) {
        if (ship.getOrientation() == Orientation.HORIZONTAL) {
            int rowNumber = (int) ship.getStartY() / 50;
            for (int row = rowNumber; row <= rowNumber; row++) {
                for (int column = (int) ship.getStartX() / 50; column <= ship.getEndX() / 50; column++) {
                    if (fields.get(row).get(column).isOccupied()) return false;
                }
            }

            for (int row = rowNumber; row <= rowNumber; row++) {
                for (int column = (int) ship.getStartX() / 50; column <= ship.getEndX() / 50; column++) {
                    fields.get(row).get(column).setOccupied(true);
                }
            }
        } else {
            int columnNumber = (int) ship.getStartX() / 50;
            for (int column = columnNumber; column <= columnNumber; column++) {
                for (int row = (int) ship.getStartY() / 50; row <= ship.getEndY() / 50; row++) {
                    if (fields.get(row).get(column).isOccupied()) return false;
                }
            }

            for (int column = columnNumber; column <= columnNumber; column++) {
                for (int row = (int) ship.getStartY() / 50; row <= ship.getEndY() / 50; row++) {
                    fields.get(row).get(column).setOccupied(true);
                }
            }
        }
        health += ship.getSize();
        return true;
    }


    public void setFields(List<Rectangle> fields) {
        List<Field> row = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            row.add(new Field(fields.get(i - 1)));//.getLayoutX(), fields.get(i - 1).getLayoutY()));
            if (i % 10 == 0) {
                this.fields.add(row);
                row = new ArrayList<>();
            }
        }
    }

    //daj polje za rectangle
    /*public Field getField(Rectangle rectangle) {
        for (int row = 0; row < 10; row++) {
            for (int element = 0; element < 10; element++) {
                Field field = fields.get(row).get(element);
                if (field.getPositionX() == rectangle.getLayoutX() && field.getPositionY() == rectangle.getLayoutY())
                    return field;
            }
        }
        return null;
    }*/
    public Field getField(double columnPosition, double rowPosition) {
        return fields.get((int) (rowPosition / 50)).get((int) (columnPosition / 50));
    }

    public void setRandomShips() {
        int[] sizes = {5, 4, 3, 3, 2};
        removeShips();
        for (int i = 0; i < 5; i++) {
            Ship ship = new Ship(sizes[i]);
            Orientation randomOrientation = ThreadLocalRandom.current().nextInt(0, 2) == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            ship.setOrientation(randomOrientation);
            int randomPositionOne = ThreadLocalRandom.current().nextInt(0, 10);
            int randomPositionTwo = ThreadLocalRandom.current().nextInt(0, 10 - sizes[i] + 1);
            if (randomOrientation == Orientation.HORIZONTAL) {
                ship.setStartX(randomPositionTwo * 50);
                ship.setStartY(randomPositionOne * 50);
            } else {
                ship.setStartX(randomPositionOne * 50);
                ship.setStartY(randomPositionTwo * 50);
            }
            if (setOccupiedFields((ship))) {
                addShip(ship);
                continue;
            }
            i--;
        }

    }

    @Override
    public String toString() {
        ships.sort(new Comparator<Ship>() {
            @Override
            public int compare(Ship o1, Ship o2) {
                if (o1.getSize() > o2.getSize()) return -1;
                else if (o1.getSize() == o2.getSize())
                    return 0;
                return 1;
            }
        });
        String string = "Ships = ";
        for (Ship ship : ships) {
            string += ship + "\n";
        }
        string += "***Fields*** \n";
        for (List<Field> row : fields)
            string += row.toString() + "\n";
        return string;
    }
}
