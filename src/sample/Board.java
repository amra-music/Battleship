package sample;

import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public void setUpShips(){
        ships.forEach(ship->{
            //System.out.println(ship.getStartY()/50 +" "+ ship.getEndY()/50);
            for(int row = (int) ship.getStartY()/50; row <= ship.getEndY()/50; row++){
                for(int column = (int) ship.getStartX()/50; column <= ship.getEndX()/50; column++){
                    fields.get(row).get(column).setOccupied(true);
                }
            }
        });
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
    public Field getField(double columnPosition, double rowPosition){
        return fields.get((int) (rowPosition/50)).get((int) (columnPosition/50));
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
