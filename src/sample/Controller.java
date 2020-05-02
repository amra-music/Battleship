package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;


public class Controller {
    public Rectangle boatSubmarine;
    public Rectangle boatCarrier;
    public Rectangle boatCruiser;
    public Rectangle boatDestroyer;
    public Rectangle boatBattleship;
    public GridPane playerBoard;
    public Rectangle field;
    public TextField text;
    public Rectangle field1;
    public Rectangle field2;
    public Rectangle field3;
    public Rectangle field4;
    public Rectangle field5;
    private Image boatFiveImage = new Image("sample/boatFive.png");
    private Image boatFourImage = new Image("sample/boatFour.png");
    private Image boatThree1Image = new Image("sample/boatThree1.png");
    private Image boatThree2Image = new Image("sample/boatThree2.png");
    private Image boatTwoImage = new Image("sample/boatTwo.png");
    private Image ocean = new Image("sample/ocean.jpg");
    public Pane scene2;
    private List<Shape> nodes = new ArrayList();

    private Bounds ploca;


    @FXML
    public void initialize() {


        for (Node currentNode : playerBoard.getChildren()) {
            if (currentNode instanceof Rectangle) {
                nodes.add((Shape) currentNode);
            }
        }

        boatCarrier.setFill(new ImagePattern(boatFiveImage));
        boatBattleship.setFill(new ImagePattern(boatFourImage));
        boatCruiser.setFill(new ImagePattern(boatThree2Image));
        boatSubmarine.setFill(new ImagePattern(boatThree1Image));
        boatDestroyer.setFill(new ImagePattern(boatTwoImage));

        setDragListeners(boatCarrier);
        setDragListeners(boatBattleship);
        setDragListeners(boatCruiser);
        setDragListeners(boatDestroyer);
        setDragListeners(boatSubmarine);

        ploca = playerBoard.localToScene(playerBoard.getBoundsInParent());
    }

    public void setDragListeners(final Rectangle ship) {
        final Delta dragDelta = new Delta();
        ship.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ship.toFront();
                ship.setCursor(Cursor.CLOSED_HAND);

                //zapamti pocetnu poziciju
                if (dragDelta.getLayoutX() == 0) {
                    dragDelta.layoutX = ship.getLayoutX();
                    dragDelta.layoutY = ship.getLayoutY();
                }

                dragDelta.x = ship.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = ship.getLayoutY() - mouseEvent.getSceneY();

                //na desni klik, rotacija
                if (mouseEvent.isSecondaryButtonDown()) {
                    setRotation(ship);
                }
            }
        });
        ship.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ship.setCursor(Cursor.OPEN_HAND);

                if (!isBoatOnBoard(ship, ploca)) {
                    ship.setLayoutX(dragDelta.layoutX);
                    ship.setLayoutY(dragDelta.layoutY);
                } else {
                    for (Shape static_bloc : nodes) {
                        if (static_bloc.getFill() == Color.GREEN) {
                            if (ship.getRotate() == 0) {
                                ship.setLayoutY(static_bloc.getLayoutY() + 5);
                                break;
                            } else {
                                int deviation = outBoard(ship);
                                ship.setLayoutX(static_bloc.getLayoutX() - deviation + 5);
                            }
                        }
                    }
                }
                for (Shape static_bloc : nodes) {
                    if (static_bloc.getFill() == Color.GREEN || static_bloc.getFill() == Color.RED)
                        static_bloc.setFill(Color.BLUE);
                }
            }
        });
        ship.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                ship.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                ship.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                System.out.println(ship.getLayoutX());
                System.out.println(ship.getLayoutY());

                checkShapeIntersection(ship);
            }
        });
    }

    private void setRotation(Rectangle ship) {
        if (ship.getRotate() == 0)
            ship.setRotate(270);
        else
            ship.setRotate(0);
    }

    private boolean isBoatOnBoard(Rectangle ship, Bounds board) {
        if (ship.getRotate() == 0) {
            return ship.getLayoutX() >= 0 && ship.getLayoutX() < (board.getMaxX() - ship.getWidth()) &&
                    ship.getLayoutY() >= 0 && ship.getLayoutY() < board.getMaxY() - ship.getHeight();
        } else {
            int outBoard = outBoard(ship);
            return ship.getLayoutX() >= -outBoard && ship.getLayoutX() < (board.getMaxX() - ship.getWidth() + outBoard) &&
                    ship.getLayoutY() >= outBoard && ship.getLayoutY() < board.getMaxY() - ship.getWidth() + outBoard;
        }
    }

    private int outBoard(Rectangle ship) {
        switch ((int) ship.getWidth()) {
            case 240:
                return 100;
            case 190:
                return 75;
            case 140:
                return 50;
            case 80:
                return 20;
        }
        return 0;
    }

    private boolean occupiesOneRow(Shape ship, Shape field) {
        return ship.getLayoutY() <= field.getLayoutY() + 30 && ship.getLayoutY() > field.getLayoutY() - 50 + 30;
    }

    private boolean occupiesOneColumn(Shape ship, Shape field) {
        int deviation = outBoard((Rectangle) ship);
        return ship.getLayoutX() <= field.getLayoutX() - deviation + 30 && ship.getLayoutX() > field.getLayoutX() - deviation - 50 + 30;
    }

    private void checkShapeIntersection(Shape block) {
        for (Shape static_bloc : nodes) {
            Shape intersect = Shape.intersect(block, static_bloc);
            if (intersect.getBoundsInLocal().getWidth() != -1) {

                //50 je visina i sirina bloka
                if (block.getRotate() == 0) {
                    if (occupiesOneRow(block, static_bloc) && isBoatOnBoard((Rectangle) block, ploca))
                        static_bloc.setFill(Color.GREEN);
                    else if (occupiesOneRow(block, static_bloc) && !isBoatOnBoard((Rectangle) block, ploca))
                        static_bloc.setFill(Color.RED);
                    else
                        static_bloc.setFill(Color.BLUE);
                } else {
                    if (occupiesOneColumn(block, static_bloc) && isBoatOnBoard((Rectangle) block, ploca))
                        static_bloc.setFill(Color.GREEN);
                    else if
                    (occupiesOneColumn(block, static_bloc) && !isBoatOnBoard((Rectangle) block, ploca))
                        static_bloc.setFill(Color.RED);
                    else
                        static_bloc.setFill(Color.BLUE);
                }
            } else
                static_bloc.setFill(Color.BLUE);
        }
    }
   /*private void checkShapeIntersection(Shape block) {
       for (Shape static_bloc : nodes) {
           Shape intersect = Shape.intersect(block, static_bloc);
           if (intersect.getBoundsInLocal().getWidth() != -1) {
           staviti u neku listu, provjeriti minship.layout-block.layout
               static_bloc.setFill(Color.GREEN);
           }
           else
               static_bloc.setFill(Color.BLUE);
       }
   }*/
}


