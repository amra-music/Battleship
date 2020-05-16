package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.util.ArrayList;
import java.util.List;

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
    private Board PC = new Board();
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
        player.setFields(playerBoardFields);

        //dodavanje u listu PC polja
        for (Node currentNode : PCBoard.getChildren()) {
            if (currentNode instanceof Rectangle) {
                PCBoardFields.add((Rectangle) currentNode);
            }
        }
        PC.setFields(PCBoardFields);

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

        //Liseneri na polja ploca
        setBoardFieldsListeners(PCBoardFields);
        setBoardFieldsListeners(playerBoardFields);
        playerBoard.setDisable(true);
        PCBoard.setDisable(true);

        // TODO : razmisliti o enumu koji ce cuvati tipove brodova i koji bi se nalazio u klasi Ship
        // TODO : pokusati napraviti elegantnijim vracanje brodica na poziciju kada se klikne play again
        // TODO : ne trebaju mi liseneri na player ploci jer ce racunar samo pogoditini neku poziciju, ono sto mi
        //  treba jeste da se ova ploca PC blokira kada je red na igraca
    }

    public void setBoardFieldsListeners(List<Rectangle> boardFields) {
        boardFields.forEach(boardField -> {
            EventHandler<MouseEvent> mouseEntered = event -> {
                if (boardField.getFill() == Color.DODGERBLUE)
                    boardField.setFill(Color.GREEN);
            };
            EventHandler<MouseEvent> mouseExited = event -> {
                if (boardField.getFill() == Color.GREEN)
                    boardField.setFill(Color.DODGERBLUE);
            };
            EventHandler<MouseEvent> mouseClicked = event -> {
                //igrac je na potezu
                if (playerBoard.isDisable()) {
                    Field field = PC.getField(boardField.getLayoutX(), boardField.getLayoutY());
                    field.setHit(true);
                    textArea.setText(field.toString());
                    boardField.setFill(Color.CORAL);
                    playerBoard.setDisable(false);
                    PCBoard.setDisable(true);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Red je na PC");
                    alert.showAndWait();
                }
                //PC je na potezu
                else if(PCBoard.isDisable()) {
                    Field field = player.getField(boardField.getLayoutX(), boardField.getLayoutY());
                    field.setHit(true);
                    textArea.setText(field.toString());
                    boardField.setFill(Color.CORAL);
                    playerBoard.setDisable(true);
                    PCBoard.setDisable(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Red je na igraca");
                    alert.showAndWait();
                }
            };

            boardField.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEntered);
            // Removing the Green when exit
            boardField.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExited);
            //kada kliknemo da se sacuva pozicija kliknutog polja
            boardField.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked);
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
                    dragDelta.setFirstLayoutX(shipRectangle.getLayoutX());
                    dragDelta.setFirstLayoutY(shipRectangle.getLayoutY());
                }
                if (isBoatOnBoard(shipRectangle, playerBoardBounds)) {
                    player.removeShip(ship);
                }
                dragDelta.setX(shipRectangle.getLayoutX() - mouseEvent.getSceneX());
                dragDelta.setY(shipRectangle.getLayoutY() - mouseEvent.getSceneY());

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
                    shipRectangle.setLayoutX(dragDelta.getFirstLayoutX());
                    shipRectangle.setLayoutY(dragDelta.getFirstLayoutY());
                    player.removeShip(ship);
                } else {
                    for (Shape field : playerBoardFields) {
                        if (field.getFill() == Color.GREEN) {
                            ship.setOrientation(shipRectangle.getRotate());
                            ship.setStartY(field.getLayoutY());
                            ship.setStartX(field.getLayoutX());
                            player.addShip(ship);
                            //ovaj if-else je za pravilno snapovanje
                            if (shipRectangle.getRotate() == 0) {
                                shipRectangle.setLayoutY(field.getLayoutY() + 5);
                                shipRectangle.setLayoutX(field.getLayoutX());
                                break;
                            } else {
                                int deviation = outBoard(shipRectangle);
                                shipRectangle.setLayoutX(field.getLayoutX() - deviation + 5);
                                shipRectangle.setLayoutY(field.getLayoutY() + deviation);
                                break;
                            }
                        }
                    }
                }
                for (Shape field : playerBoardFields) {
                    if (field.getFill() == Color.GREEN || field.getFill() == Color.CORAL)
                        field.setFill(Color.DODGERBLUE);
                }
            }
        });
        shipRectangle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                shipRectangle.setLayoutX(mouseEvent.getSceneX() + dragDelta.getX());
                shipRectangle.setLayoutY(mouseEvent.getSceneY() + dragDelta.getY());
                System.out.println(shipRectangle.getLayoutX());
                System.out.println(shipRectangle.getLayoutY());

                checkShapeIntersection(shipRectangle);
            }
        });
    }

    private void setDodgerblueColor(List<Rectangle> board){
        for (Rectangle field : board) {
            if (field.getFill() == Color.CORAL)
                field.setFill(Color.DODGERBLUE);
        }
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
        return ((int) ship.getWidth() / 2) - 20;
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
        for (Rectangle field : playerBoardFields) {
            Shape intersect = Shape.intersect(block, field);
            if (intersect.getBoundsInLocal().getWidth() != -1) {

                if (block.getRotate() == 0) {
                    if (occupiesOneRow(block, field) && occupiesRightWidth(block, field) &&
                            occupiesRightWidth2(block, field) && isBoatOnBoard(block, playerBoardBounds)) {
                        field.setFill(Color.GREEN);
                    } else if (occupiesOneRow(block, field) && occupiesRightWidth(block, field) &&
                            occupiesRightWidth2(block, field) && !isBoatOnBoard(block, playerBoardBounds))
                        field.setFill(Color.CORAL);
                    else
                        field.setFill(Color.DODGERBLUE);
                } else {
                    if (occupiesOneColumn(block, field) && occupiesRightHight(block, field)
                            && occupiesRightHight2(block, field) && isBoatOnBoard(block, playerBoardBounds))
                        field.setFill(Color.GREEN);
                    else if
                    (occupiesOneColumn(block, field) && occupiesRightHight(block, field)
                                    && occupiesRightHight2(block, field) && !isBoatOnBoard(block, playerBoardBounds))
                        field.setFill(Color.CORAL);
                    else
                        field.setFill(Color.DODGERBLUE);
                }
            } else
                field.setFill(Color.DODGERBLUE);
        }
    }

    public void start(MouseEvent mouseEvent) {
        playerBoard.setDisable(true);
        PCBoard.setDisable(false);
        Alert alert = new Alert(Alert.AlertType.ERROR,"Nisu postavljeni svi brodici na polje!" );
        if (player.getShips().size() != 5)
            alert.showAndWait();
        else {
            ships.forEach(ship -> ship.setDisable(true));
            player.setUpShips();
            textArea.setText(player.toString() + "\n " + player.getShips().size());
            startButton.setDisable(true);
        }
    }

    public void playAgain(MouseEvent mouseEvent) {
        startButton.setDisable(false);
        textArea.setText("");
        for (int i = 0; i < ships.size(); i++) {
            Rectangle ship = ships.get(i);
            ship.setDisable(false);
            ship.setRotate(0);
            ship.setLayoutX(10);
            ship.setLayoutY(50 * i + 600);
        }
        PCBoard.setDisable(true);
        playerBoard.setDisable(true);
        setDodgerblueColor(PCBoardFields);
        setDodgerblueColor(playerBoardFields);
        player.getShips().clear();

    }


}
