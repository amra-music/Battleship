package sample;

import com.sun.deploy.net.MessageHeader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
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
import sun.nio.cs.ext.PCK;

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
    public GridPane PCBoard;

    private Image boatFiveImage = new Image("sample/boatFive.png");
    private Image boatFourImage = new Image("sample/boatFour.png");
    private Image boatThree1Image = new Image("sample/boatThree1.png");
    private Image boatThree2Image = new Image("sample/boatThree2.png");
    private Image boatTwoImage = new Image("sample/boatTwo.png");

    public Pane scene2;
    //polja jedne i druge ploce
    private List<Rectangle> playerBoardFields = new ArrayList();
    private List<Rectangle> PCBoardFields = new ArrayList();

    private Bounds playerBoardBounds;
    private Board player = new Board();
    private List<Rectangle> ships = new ArrayList();
    private boolean firstTime = true;


    @FXML
    public void initialize() {

        //radi prevlacenja brodica na player plocu
        for (Node currentNode : playerBoard.getChildren()) {
            if (currentNode instanceof Rectangle) {
                playerBoardFields.add((Rectangle) currentNode);
            }
        }

        //dodavanje u listu PC polja
        for (Node currentNode : PCBoard.getChildren()) {
            if (currentNode instanceof Rectangle) {
                PCBoardFields.add((Rectangle) currentNode);
            }
        }


        boatCarrier.setFill(new ImagePattern(boatFiveImage));
        boatBattleship.setFill(new ImagePattern(boatFourImage));
        boatCruiser.setFill(new ImagePattern(boatThree2Image));
        boatSubmarine.setFill(new ImagePattern(boatThree1Image));
        boatDestroyer.setFill(new ImagePattern(boatTwoImage));

        ships.add(boatCarrier);
        ships.add(boatBattleship);
        ships.add(boatCruiser);
        ships.add(boatSubmarine);
        ships.add(boatDestroyer);

        ships.forEach(this::setDragListeners);

        playerBoardBounds = playerBoard.localToScene(playerBoard.getBoundsInParent());

        //pracenje da li je start dugme iskljuceno, ako jeste moze se hover preko PC polja
        startButton.disabledProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    PCBoard.setDisable(false);
                    PCBoardFields.forEach(boardField -> {
                        EventHandler<MouseEvent> mouseEntered = event -> boardField.setFill(Color.GREEN);
                        EventHandler<MouseEvent> mouseExited = event -> boardField.setFill(Color.DODGERBLUE);
                        EventHandler<MouseEvent> mouseClicked = event -> textArea.setText(boardField.getLayoutX()+"  "+boardField.getLayoutY());

                        boardField.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEntered);
                        // Removing the Green when exit
                        boardField.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExited);
                        //kada kliknemo da se sacuva pozicija kliknutog polja
                        boardField.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked) ;
                    });
                } else if (oldValue) {
                    PCBoard.setDisable(true);
                }
            }
        });
    }

    public void setDragListeners(final Rectangle shipRectangle) {
        final Delta dragDelta = new Delta();
        Ship ship = new Ship(shipRectangle.getWidth(), shipRectangle.getLayoutX(), shipRectangle.getLayoutY());
        shipRectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                shipRectangle.toFront();
                shipRectangle.setCursor(Cursor.CLOSED_HAND);

                //zapamti pocetnu poziciju
                if (dragDelta.getFirstLayoutX() == 0) {
                    dragDelta.firstLayoutX = shipRectangle.getLayoutX();
                    dragDelta.firstLayoutY = shipRectangle.getLayoutY();
                }
                if (isBoatOnBoard(shipRectangle, playerBoardBounds)) {
                    player.removeShip(ship);
                }
                dragDelta.x = shipRectangle.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = shipRectangle.getLayoutY() - mouseEvent.getSceneY();

                //na desni klik, rotacija
                if (mouseEvent.isSecondaryButtonDown()) {
                    setRotation(shipRectangle);
                }
            }
        });
        shipRectangle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                shipRectangle.setCursor(Cursor.OPEN_HAND);

                if (!isBoatOnBoard(shipRectangle, playerBoardBounds)) {
                    shipRectangle.setLayoutX(dragDelta.firstLayoutX);
                    shipRectangle.setLayoutY(dragDelta.firstLayoutY);
                    player.removeShip(ship);
                } else {
                    for (Shape static_bloc : playerBoardFields) {
                        if (static_bloc.getFill() == Color.GREEN) {
                            ship.setOrientation(static_bloc.getRotate());
                            ship.setStartY(static_bloc.getLayoutY());
                            ship.setStartX(static_bloc.getLayoutX());
                            player.addShip(ship);
                            //ovaj if-else je za pravilno snapovanje
                            if (shipRectangle.getRotate() == 0) {
                                shipRectangle.setLayoutY(static_bloc.getLayoutY() + 5);
                                shipRectangle.setLayoutX(static_bloc.getLayoutX());
                                break;
                            } else {
                                int deviation = outBoard(shipRectangle);
                                shipRectangle.setLayoutX(static_bloc.getLayoutX() - deviation + 5);
                                shipRectangle.setLayoutY(static_bloc.getLayoutY() + deviation);
                                break;
                            }
                        }
                    }
                }
                for (Shape static_bloc : playerBoardFields) {
                    if (static_bloc.getFill() == Color.GREEN || static_bloc.getFill() == Color.CORAL)
                        static_bloc.setFill(Color.BLUE);
                }
            }
        });
        shipRectangle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                shipRectangle.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                shipRectangle.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                System.out.println(shipRectangle.getLayoutX());
                System.out.println(shipRectangle.getLayoutY());

                checkShapeIntersection(shipRectangle);
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
        for (Rectangle static_bloc : playerBoardFields) {
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
                } else {
                    firstTime = true;
                    return 164;
                }
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
            ship.setRotate(0);

            //playerShips.get(0) -> getFirst()

            ship.setLayoutX(550);
            ship.setLayoutY(layoutYByWidth(ship.getWidth()));
        });

        player.getShips().clear();
    }


}
