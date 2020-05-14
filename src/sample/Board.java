package sample;

import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class Board {
    private List<List<Field>> fields = new ArrayList<>();
    private List<Ship> ships = new ArrayList<>();

    public Board() {
    }

    public Board(List<Rectangle> fields) {
        setFields(fields);
    }

    void addShip(Ship ship) {
        ships.add(ship);
    }

    void removeShip(Ship ship) {
        ships.remove(ship);
    }

    public List<Ship> getShips() {
        return ships;
    }

    public List<List<Field>> getFields() {
        return fields;
    }

    public void setFields(List<Rectangle> fields) {
        List<Field> row = new ArrayList<>();
        for (int column = 0; column < 10; column++) {
            for (int i = 0; i < 10; i++) {
                row.add(new Field(fields.get(i).getLayoutX(), fields.remove(i).getLayoutY()));
            }
            this.fields.add(row);
            row.clear();
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
        String string = "Board = ";
        for (Ship ship : ships) {
            string += ship + "\n";
        }
        return string;
    }
}
