package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class Controller {
    public Rectangle boatSubmarine;
    public Rectangle boatCarrier;
    public Rectangle boatCruiser;
    public Rectangle boatDestroyer;
    public Rectangle boatBattleship;
    public GridPane playerBoard;
    public TextArea textArea;
    public Button startButton;

    private Image boatFiveImage = new Image("sample/boatFive.png");
    private Image boatFourImage = new Image("sample/boatFour.png");
    private Image boatThree1Image = new Image("sample/boatThree1.png");
    private Image boatThree2Image = new Image("sample/boatThree2.png");
    private Image boatTwoImage = new Image("sample/boatTwo.png");
    private Image ocean = new Image("sample/ocean.jpg");
    public Pane scene2;
    private List<Rectangle> nodes = new ArrayList();

    private Bounds playerBoardBounds;
    private Board player = new Board();
    private List<Rectangle> ships = new ArrayList();
    private boolean firstTime = true;


    @FXML
    public void initialize() {


        for (Node currentNode : playerBoard.getChildren()) {
            if (currentNode instanceof Rectangle) {
                nodes.add((Rectangle) currentNode);
            }
        }

        boatCarrier.setFill(new ImagePattern(boatFiveImage));
        boatBattleship.setFill(new ImagePattern(boatFourImage));
        boatCruiser.setFill(new ImagePattern(boatThree2Image));
        boatSubmarine.setFill(new ImagePattern(boatThree1Image));
        boatDestroyer.setFill(new ImagePattern(boatTwoImage));

        ships.add(boatCarrier);
        ships.add(boatBattleship);
        ships.add(boatDestroyer);
        ships.add(boatSubmarine);
        ships.add(boatCruiser);

        ships.forEach(this::setDragListeners);

        playerBoardBounds = playerBoard.localToScene(playerBoard.getBoundsInParent());
    }

    public void setDragListeners(final Rectangle ship) {
        final Delta dragDelta = new Delta();
        Ship brod = new Ship(ship.getWidth());
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
                if (isBoatOnBoard(ship, playerBoardBounds)) {
                    player.removeShip(brod);
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

                if (!isBoatOnBoard(ship, playerBoardBounds)) {
                    ship.setLayoutX(dragDelta.layoutX);
                    ship.setLayoutY(dragDelta.layoutY);
                    player.removeShip(brod);
                } else {
                    for (Shape static_bloc : nodes) {
                        if (static_bloc.getFill() == Color.GREEN) {
                            brod.setOrientation(static_bloc.getRotate());
                            brod.setStartY(static_bloc.getLayoutY());
                            brod.setStartX(static_bloc.getLayoutX());
                            player.addShip(brod);
                            //ovaj if-else je za pravilno snapovanje
                            if (ship.getRotate() == 0) {
                                ship.setLayoutY(static_bloc.getLayoutY() + 5);
                                ship.setLayoutX(static_bloc.getLayoutX());
                                break;
                            } else {
                                int deviation = outBoard(ship);
                                ship.setLayoutX(static_bloc.getLayoutX() - deviation + 5);
                                ship.setLayoutY(static_bloc.getLayoutY() + deviation);
                                break;
                            }
                        }
                    }
                }
                for (Shape static_bloc : nodes) {
                    if (static_bloc.getFill() == Color.GREEN || static_bloc.getFill() == Color.CORAL)
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
            return ship.getLayoutX() >= 0 && ship.getLayoutX() <= (board.getMaxX() + 5 - ship.getWidth()) &&
                    ship.getLayoutY() >= 0 && ship.getLayoutY() <= board.getMaxY() + 5 - ship.getHeight();
        } else {
            int outBoard = outBoard(ship);
            //+5 na velicinu ploce da se ne detekltuje odmah cim se dotakne okvir da je izvan ploce brodic
            return ship.getLayoutX() >= -outBoard && ship.getLayoutX() <= (board.getMaxX() + 5 - ship.getHeight() + outBoard) &&
                    ship.getLayoutY() >= outBoard && ship.getLayoutY() <= board.getMaxY() + 5 - ship.getWidth() + outBoard;
        }
    }

    private int outBoard(Rectangle ship) {
        switch ((int) ship.getWidth()) {
            case 250:
                return 105;
            case 200:
                return 80;
            case 150:
                return 55;
            case 100:
                return 30;
        }
        return 0;
    }

    // da se ne boje dva reda/kolone ako se presijecaju polja vec samo ako je presjeceno vise od pola polja
    private boolean occupiesOneRow(Rectangle ship, Rectangle field) {
        return ship.getLayoutY() <= field.getLayoutY() + 30 && ship.getLayoutY() > field.getLayoutY() - field.getHeight() + 30;
    }

    private boolean occupiesRightWidth(Rectangle ship, Rectangle field) {
        return (ship.getLayoutX() <= field.getLayoutX() + field.getWidth() / 2);
    }

    private boolean occupiesRightWidth2(Rectangle ship, Rectangle field) {
        return ship.getLayoutX() + ship.getWidth() > field.getLayoutX() + field.getWidth() / 2;
    }

    private boolean occupiesRightHight(Rectangle ship, Rectangle field) {
        int deviation = outBoard(ship);
        return (ship.getLayoutY() - deviation <= field.getLayoutY() + field.getWidth() / 2);
    }

    private boolean occupiesRightHight2(Rectangle ship, Rectangle field) {
        int deviation = outBoard(ship);
        return ship.getLayoutY() - deviation + ship.getWidth() > field.getLayoutY() + field.getWidth() / 2;
    }

    private boolean occupiesOneColumn(Rectangle ship, Rectangle field) {
        int deviation = outBoard(ship);
        return ship.getLayoutX() <= field.getLayoutX() - deviation + 30 && ship.getLayoutX() > field.getLayoutX() - deviation - field.getWidth() + 30;
    }

    private void checkShapeIntersection(Rectangle block) {
        for (Rectangle static_bloc : nodes) {
            Shape intersect = Shape.intersect(block, static_bloc);
            if (intersect.getBoundsInLocal().getWidth() != -1) {

                if (block.getRotate() == 0) {
                    if (occupiesOneRow(block, static_bloc) && occupiesRightWidth(block, static_bloc) &&
                            occupiesRightWidth2(block, static_bloc) && isBoatOnBoard(block, playerBoardBounds)) {
                        static_bloc.setFill(Color.GREEN);
                    } else if (occupiesOneRow(block, static_bloc) && occupiesRightWidth(block, static_bloc) &&
                            occupiesRightWidth2(block, static_bloc) && !isBoatOnBoard(block, playerBoardBounds))
                        static_bloc.setFill(Color.CORAL);
                    else
                        static_bloc.setFill(Color.BLUE);
                } else {
                    if (occupiesOneColumn(block, static_bloc) && occupiesRightHight(block, static_bloc)
                            && occupiesRightHight2(block, static_bloc) && isBoatOnBoard(block, playerBoardBounds))
                        static_bloc.setFill(Color.GREEN);
                    else if
                    (occupiesOneColumn(block, static_bloc) && occupiesRightHight(block, static_bloc)
                                    && occupiesRightHight2(block, static_bloc) && !isBoatOnBoard(block, playerBoardBounds))
                        static_bloc.setFill(Color.CORAL);
                    else
                        static_bloc.setFill(Color.BLUE);
                }
            } else
                static_bloc.setFill(Color.BLUE);
        }
    }

    public void start(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Nisu postavljeni svi brodici na polje!");
        if (player.getShips().size() != 5)
            alert.showAndWait();
        else {
            textArea.setText(player.toString() + "\n " + player.getShips().size());
            ships.forEach(ship -> ship.setDisable(true));
            startButton.setDisable(true);
        }
    }
    private double layoutYByWidth(double width) {
        switch ((int) width) {
            case 250:
                return 18;
            case 200:
                return 64;
            case 150:
                if (firstTime) {
                    firstTime = false;
                    return 114;
                } else
                return 164;
            case 100:
                return 214;
        }
        return 0;
    }
    public void playAgain(MouseEvent mouseEvent) {
        startButton.setDisable(false);
        textArea.setText("");
        ships.forEach(ship -> {
            ship.setDisable(false);
            ship.setLayoutX(550);
            ship.setRotate(0);
            ship.setLayoutY(layoutYByWidth(ship.getWidth()));
        });
        player.getShips().clear();
    }
}


