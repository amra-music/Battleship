package sample;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class Board {
    List<Ship> ships = new ArrayList<>();

    public Board() {
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

    void addShip(Ship ship) {
        ships.add(ship);
    }

    void removeShip(Ship ship) {
        ships.remove(ship);
    }

    public List<Ship> getShips() {
        return ships;
    }
}
