package battleship.controllers;

import battleship.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PlaySceneController {
    public Rectangle boatSubmarine;
    public Rectangle boatCarrier;
    public Rectangle boatCruiser;
    public Rectangle boatDestroyer;
    public Rectangle boatBattleship;
    public GridPane playerBoard;
    public Button startButton;
    public GridPane PCBoard;
    public Button randomButton;
    public ProgressBar PCHealth;
    public ProgressBar playerHealth;
    public BorderPane primaryScene;
    public AnchorPane title;
    public Button soundButton;
    public RadioButton randomRButton;
    public RadioButton sequenceRButton;
    public RadioButton strategyOneRButton;
    public RadioButton strategyTwoRButton;
    public Spinner<Integer> iterationsSpinner;
    public ProgressBar loadingBar;

    private ToggleGroup strategys = new ToggleGroup();
    private Image boatFiveImage = new Image("/img/boatFive.png");
    private Image boatFourImage = new Image("/img/boatFour.png");
    private Image boatThree1Image = new Image("/img/boatThree1.png");
    private Image boatThree2Image = new Image("/img/boatThree2.png");
    private Image boatTwoImage = new Image("/img/boatTwo.png");
    private Image soundOnButtonImage = new Image("/img/soundOn1.png", 65, 25, true, true);
    private Image soundOffButtonImage = new Image("/img/soundOff1.png", 65, 25, true, true);

    public Pane scene2;
    //polja jedne i druge ploce
    private List<Rectangle> playerBoardFields = new ArrayList<>();
    private List<Rectangle> PCBoardFields = new ArrayList<>();

    private Bounds playerBoardBounds;
    private Board player = new Board();
    private Board PC = new Board();
    private List<Rectangle> ships = new ArrayList<>();
    private DummyAI dummyAI = null;
    private SmartAI smartAI = null;
    private RandomAI randomAI = new RandomAI();
    private SequenceAI sequenceAI = new SequenceAI();
    private StrategyOneAI strategyOneAI = new StrategyOneAI();
    private StrategyTwoAI strategyTwoAI = new StrategyTwoAI();

    private <T> void commitEditorText(Spinner<T> spinner) {
        if (!spinner.isEditable()) return;
        String text = spinner.getEditor().getText();
        SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
        if (valueFactory != null) {
            StringConverter<T> converter = valueFactory.getConverter();
            if (converter != null) {
                T value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }

    @FXML
    public void initialize() {
        iterationsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000));
        iterationsSpinner.focusedProperty().addListener((s, ov, nv) -> {
            if (nv) return;
            commitEditorText(iterationsSpinner);
        });

        randomRButton.setToggleGroup(strategys);
        sequenceRButton.setToggleGroup(strategys);
        strategyOneRButton.setToggleGroup(strategys);
        strategyTwoRButton.setToggleGroup(strategys);
        strategys.selectToggle(strategyTwoRButton);

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
        soundButton.setGraphic(new ImageView(soundOnButtonImage));

        ships.add(boatCarrier);
        ships.add(boatBattleship);
        ships.add(boatCruiser);
        ships.add(boatSubmarine);
        ships.add(boatDestroyer);

        ships.forEach(this::setDragListeners);

        playerBoardBounds = playerBoard.localToScene(playerBoard.getBoundsInParent());

        //Liseneri na polja ploca
        setBoardFieldsListeners(PC.getFields());
        playerBoard.setDisable(true);
        PCBoard.setDisable(true);


        // TODO : options da se odabrere AI i zvuk da se moze iskljuciti
        // TODO : ako je polje occupied onda kada se presijece sa tim poljem ne moze se tu postaviti
    }

    public void setBoardFieldsListeners(List<List<Field>> boardFields) {
        boardFields.forEach(rows -> rows.forEach(field -> {
            EventHandler<MouseEvent> mouseEntered = event -> {
                if (field.getColor() == Color.DODGERBLUE)
                    field.setColor(Color.GREEN);
            };
            EventHandler<MouseEvent> mouseExited = event -> {
                if (field.getColor() == Color.GREEN)
                    field.setColor(Color.DODGERBLUE);
            };
            EventHandler<MouseEvent> mouseClicked = event -> {
                PC.playerTurn(field);
                PCBoard.setDisable(true);
                PCHealth.setProgress(PC.getHealth() / 17.);

                if (smartAI != null) player.enemyTurn(smartAI);
                else player.enemyTurn(dummyAI);


                PCBoard.setDisable(false);
                playerHealth.setProgress(player.getHealth() / 17.);
            };

            field.getRectangle().addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEntered);
            // Removing the Green when exit
            field.getRectangle().addEventHandler(MouseEvent.MOUSE_EXITED, mouseExited);
            //kada kliknemo da se sacuva pozicija kliknutog polja
            field.getRectangle().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked);
        }));
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
                    int deviation = shipRectangle.getRotate() == 270 ? outBoard(shipRectangle) : 0;
                    int column = (int) (shipRectangle.getLayoutX() + deviation) / 50;
                    int row = (int) (shipRectangle.getLayoutY() - deviation) / 50;

                    for (int i = 0; i < ship.getSize(); i++) {
                        if (shipRectangle.getRotate() == 270)
                            playerBoardFields.get((row + i) * 10 + column).setDisable(false);
                        else
                            playerBoardFields.get(row * 10 + column + i).setDisable(false);
                    }
                }
                dragDelta.setX(shipRectangle.getLayoutX() - mouseEvent.getSceneX());
                dragDelta.setY(shipRectangle.getLayoutY() - mouseEvent.getSceneY());

                //na desni klik, rotacija
                if (mouseEvent.isSecondaryButtonDown()) {
                    setRotation(shipRectangle);
                    checkShapeIntersection(shipRectangle);
                }
            }
        });
        shipRectangle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                shipRectangle.setCursor(Cursor.OPEN_HAND);

                if (!isBoatOnBoard(shipRectangle, playerBoardBounds)) {
                    resetShip(shipRectangle, dragDelta, ship);
                } else {
                    Sound.INSTANCE.shipPlaced();
                    for (int i = 0; i < playerBoardFields.size(); i++) {
                        Shape field = playerBoardFields.get(i);
                        if (field.getFill() == Color.GREEN) {
                            ship.setOrientation(shipRectangle.getRotate());
                            ship.setStartY(field.getLayoutY());
                            ship.setStartX(field.getLayoutX());
                            //ovaj if-else je za pravilno snapovanje
                            if (shipRectangle.getRotate() == 0) {
                                int brojac = 0;
                                for (int j = 0; j < ship.getSize(); j++) {
                                    if (playerBoardFields.get(i + j).getFill() == Color.GREEN)
                                        brojac++;
                                }
                                if (brojac != ship.getSize()) {
                                    resetShip(shipRectangle, dragDelta, ship);
                                    break;
                                }
                                for (int j = 0; j < ship.getSize(); j++) {
                                    playerBoardFields.get(i + j).setDisable(true);
                                }
                                shipRectangle.setLayoutY(field.getLayoutY() + 5);
                                shipRectangle.setLayoutX(field.getLayoutX());
                                player.addShip(ship);
                                break;
                            } else {
                                int brojac = 0;
                                for (int j = 0; j < ship.getSize(); j++) {
                                    if (playerBoardFields.get(i + j * 10).getFill() == Color.GREEN)
                                        brojac++;
                                }
                                if (brojac != ship.getSize()) {
                                    resetShip(shipRectangle, dragDelta, ship);
                                    break;
                                }
                                for (int j = 0; j < ship.getSize(); j++) {
                                    playerBoardFields.get(i + j * 10).setDisable(true);
                                }
                                int deviation = outBoard(shipRectangle);
                                shipRectangle.setLayoutX(field.getLayoutX() - deviation + 5);
                                shipRectangle.setLayoutY(field.getLayoutY() + deviation);
                                player.addShip(ship);
                                break;
                            }
                        } else if (field.getFill() == Color.CORAL) {
                            resetShip(shipRectangle, dragDelta, ship);
                            break;
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
                checkShapeIntersection(shipRectangle);
            }
        });
    }

    private void resetShip(Rectangle shipRectangle, Delta dragDelta, Ship ship) {
        shipRectangle.setLayoutX(dragDelta.getFirstLayoutX());
        shipRectangle.setLayoutY(dragDelta.getFirstLayoutY());
        shipRectangle.setRotate(0);
        player.removeShip(ship);
        Sound.INSTANCE.error();
    }

    private void setDodgerblueColor(List<Rectangle> board) {
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
                    if (occupiesOneRow(block, field) && occupiesRightWidth(block, field) && !field.isDisable() &&
                            occupiesRightWidth2(block, field) && isBoatOnBoard(block, playerBoardBounds)) {
                        field.setFill(Color.GREEN);
                    } else if ((field.isDisable() && occupiesRightWidth(block, field) &&
                            occupiesRightWidth2(block, field) && occupiesOneRow(block, field)) || (occupiesOneRow(block, field) && occupiesRightWidth(block, field) &&
                            occupiesRightWidth2(block, field) && !isBoatOnBoard(block, playerBoardBounds)))
                        field.setFill(Color.CORAL);
                    else
                        field.setFill(Color.DODGERBLUE);
                } else {
                    if (occupiesOneColumn(block, field) && occupiesRightHight(block, field) && !field.isDisable()
                            && occupiesRightHight2(block, field) && isBoatOnBoard(block, playerBoardBounds))
                        field.setFill(Color.GREEN);
                    else if
                    ((field.isDisable() && occupiesRightHight(block, field)
                                    && occupiesRightHight2(block, field) && occupiesOneColumn(block, field)) || (occupiesOneColumn(block, field) && occupiesRightHight(block, field)
                                    && occupiesRightHight2(block, field) && !isBoatOnBoard(block, playerBoardBounds)))
                        field.setFill(Color.CORAL);
                    else
                        field.setFill(Color.DODGERBLUE);
                }
            } else
                field.setFill(Color.DODGERBLUE);
        }
    }

    private void finalPreparations() {
        playerBoard.setDisable(true);
        PCBoard.setDisable(false);
        ships.forEach(ship -> ship.setDisable(true));
        startButton.setDisable(true);
        randomButton.setDisable(true);
        randomRButton.setDisable(true);
        sequenceRButton.setDisable(true);
        strategyOneRButton.setDisable(true);
        strategyTwoRButton.setDisable(true);
        PC.setRandomShips();
        //besmisleno ako je random kliknuto
        player.setOccupiedFields();

        player.setInitialHealth();
        PC.setInitialHealth();
        playerBoardFields.forEach(field -> field.setDisable(false));
    }

    private void reset() {
        startButton.setDisable(false);
        randomButton.setDisable(false);
        randomRButton.setDisable(false);
        sequenceRButton.setDisable(false);
        strategyOneRButton.setDisable(false);
        strategyTwoRButton.setDisable(false);
        dummyAI = null;
        smartAI = null;
        double size = 10;
        for (int i = 0; i < ships.size(); i++) {
            Rectangle ship = ships.get(i);
            ship.setDisable(false);
            ship.setRotate(0);
            if (i != 0)
                size = size + ships.get(i - 1).getWidth() + 10;
            ship.setLayoutX(size);
            ship.setLayoutY(620);

        }
        PCBoard.setDisable(true);
        player.resetBoard();
        PC.resetBoard();
        //ovisno koji je AI ukljucen
        strategyOneAI.reset();
        sequenceAI.reset();
        strategyTwoAI.reset();
        playerHealth.setProgress(1);
        PCHealth.setProgress(1);
    }

    public void start(MouseEvent mouseEvent) {
        if (player.getShips().size() != 5) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/warning.fxml"));
                Parent root = loader.load();
                Stage wariningStage = new Stage();
                wariningStage.initStyle(StageStyle.UNDECORATED);
                wariningStage.initModality(Modality.APPLICATION_MODAL);
                wariningStage.setResizable(false);
                wariningStage.setScene(new Scene(root));
                wariningStage.show();
                Sound.INSTANCE.error();
            } catch (IOException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Problem " + error.getMessage());
                alert.show();
            }
        } else {
            if (smartAI == null && dummyAI == null) {
                if (strategys.getSelectedToggle() == strategyTwoRButton)
                    smartAI = strategyTwoAI;
                else if (strategys.getSelectedToggle() == strategyOneRButton)
                    smartAI = strategyOneAI;
                else if (strategys.getSelectedToggle() == sequenceRButton)
                    dummyAI = sequenceAI;
                else dummyAI = randomAI;
            }
            finalPreparations();
        }
    }

    public void playAgain(MouseEvent mouseEvent) {
        reset();
    }

    public void placeShipsRandom(MouseEvent mouseEvent) {
        player.removeOccupied();
        player.setRandomShips();
        Sound.INSTANCE.shipPlaced();
        ships.forEach(ship -> ship.setDisable(true));
        for (int i = 0; i < 5; i++) {
            Ship ship = player.getShips().get(i);
            Rectangle rectangle = ships.get(i);
            if (ship.getOrientation() == Orientation.HORIZONTAL) {
                rectangle.setRotate(0);
                rectangle.setLayoutY(ship.getStartY() + 5);
                rectangle.setLayoutX(ship.getStartX());
            } else {
                rectangle.setRotate(270);
                int deviation = outBoard(rectangle);
                rectangle.setLayoutX(ship.getStartX() - deviation + 5);
                rectangle.setLayoutY(ship.getStartY() + deviation);
            }
        }
    }

    private int dummyTestAI(DummyAI ai) {
        int hits = 0;
        Board boardTest = new Board(PCBoardFields);
        boardTest.setRandomShips();
        boardTest.setHealth(17);
        while (boardTest.getHealth() != 0) {
            hits++;
            boardTest.dummyAITest(ai);
        }
        return hits;
    }

    private int smartTestAI(SmartAI ai) {
        int hits = 0;
        Board boardTest = new Board(PCBoardFields);
        boardTest.setRandomShips();
        boardTest.setHealth(17);
        while (boardTest.getHealth() != 0) {
            hits++;
            boardTest.smartAITest(ai);
        }
        return hits;
    }

    public void test(MouseEvent mouseEvent) {

        XYChart.Series<Number, Number> random = new XYChart.Series<>();
        random.setName("Random");
        XYChart.Series<Number, Number> sequnece = new XYChart.Series<>();
        sequnece.setName("Sequence");
        XYChart.Series<Number, Number> strategyOne = new XYChart.Series<>();
        strategyOne.setName("StrategyOne");
        XYChart.Series<Number, Number> strategyTwo = new XYChart.Series<>();
        strategyTwo.setName("StrategyTwo");
        Thread thread = new Thread(() -> {
            for (int i = 1; i <= iterationsSpinner.getValue(); i++) {
                DummyAI randomAI = new RandomAI();
                DummyAI sequenceAI = new SequenceAI();
                SmartAI strategyOneAI = new StrategyOneAI();
                SmartAI strategyTwoAI = new StrategyTwoAI();
                int hits = 0;
                Board boardTest = new Board(PCBoardFields);
                boardTest.setRandomShips();
                boardTest.setHealth(17);

                while (boardTest.getHealth() != 0) {
                    hits++;
                    boardTest.dummyAITest(randomAI);
                }
                random.getData().add(new XYChart.Data<>(i, hits));

                hits = 0;
                boardTest.resetTest();

                while (boardTest.getHealth() != 0) {
                    hits++;
                    boardTest.dummyAITest(sequenceAI);
                }
                sequnece.getData().add(new XYChart.Data<>(i, hits));

                hits = 0;
                boardTest.resetTest();

                while (boardTest.getHealth() != 0) {
                    hits++;
                    boardTest.smartAITest(strategyOneAI);
                }
                strategyOne.getData().add(new XYChart.Data<>(i, hits));

                hits = 0;
                boardTest.resetTest();

                while (boardTest.getHealth() != 0) {
                    hits++;
                    boardTest.smartAITest(strategyTwoAI);
                }
                strategyTwo.getData().add(new XYChart.Data<>(i, hits));
                final int finalI = i;
                Platform.runLater(() -> loadingBar.setProgress(finalI / (double) iterationsSpinner.getValue()));
            }
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/report.fxml"));
                    Parent root = loader.load();
                    ReportController reportController = loader.getController();
                    reportController.transferData(random, sequnece, strategyOne, strategyTwo, iterationsSpinner.getValue());
                    Stage reportStage = new Stage();
                    reportStage.initStyle(StageStyle.UNDECORATED);
                    reportStage.initModality(Modality.APPLICATION_MODAL);
                    reportStage.setResizable(false);
                    reportStage.setScene(new Scene(root));
                    reportStage.show();
                } catch (IOException error) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Problem " + error.getMessage());
                    alert.show();
                }
            });
        });
        thread.start();
    }

    public void drag(MouseEvent mouseEvent) {
        Stage stage = (Stage) primaryScene.getScene().getWindow();
        title.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - mouseEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - mouseEvent.getSceneY());
        });
    }

    public void close(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void sound(MouseEvent mouseEvent) {
        if (Sound.INSTANCE.isSoundEnabled()) {
            Sound.INSTANCE.setSoundEnabled(false);
            soundButton.setGraphic(new ImageView(soundOffButtonImage));
        } else {
            Sound.INSTANCE.setSoundEnabled(true);
            soundButton.setGraphic(new ImageView(soundOnButtonImage));
        }
    }
}
