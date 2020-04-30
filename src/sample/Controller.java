package sample;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Controller {
    public Rectangle boatSubmarine;
    public Rectangle boatCarrier;
    public Rectangle boatCruiser;
    public Rectangle boatDestroyer;
    public Rectangle boatBattleship;
    public GridPane playerBoard;
    public Pane ships;
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
    public VBox vbox;
    private boolean clicked;
    private boolean needToRotate;
    private boolean isRotated;
    private Rectangle selectedShip;
    private double cellSize = 30;
    private double orgSceneX;
    private double orgSceneY;
    private double orgTranslateX;
    private double orgTranslateY;
    private SimpleDoubleProperty boatCarrierX = new SimpleDoubleProperty();
    private List<Shape> nodes = new ArrayList();

    private Bounds boundsInScene;
    private Bounds ploca;
    private Bounds brodic;
    private Paint boja;
    private Bounds polje;

    @FXML
    public void initialize() {


        for (Node currentNode : playerBoard.getChildren()) {
            if (currentNode instanceof Rectangle) {
                nodes.add((Shape) currentNode);
            }
        }
        /*nodes.add(field);
        nodes.add(field1);
        nodes.add(field2);
        nodes.add(field3);
        nodes.add(field5);
        nodes.add(field4);*/

        boatCarrier.setFill(new ImagePattern(boatFiveImage));
        boatBattleship.setFill(new ImagePattern(boatFourImage));
        boatCruiser.setFill(new ImagePattern(boatThree2Image));
        boatSubmarine.setFill(new ImagePattern(boatThree1Image));
        boatDestroyer.setFill(new ImagePattern(boatTwoImage));
        playerBoard.setStyle("-fx-border-color: #ffdc32");

        boundsInScene = scene2.localToScene(scene2.getBoundsInLocal());

        ploca = playerBoard.localToScene(playerBoard.getBoundsInLocal());


        boja = field.getFill();


        text.textProperty().bind(boatCarrier.boundsInLocalProperty().asString());

    }

    public void boatCarrierDragDetected(MouseEvent mouseEvent) {
        boatCarrier.startFullDrag();
    }


    public void boatCarrierMousePressed(MouseEvent mouseEvent) {
        boatCarrier.toBack();
        playerBoard.toFront();
        //   boatCarrier.setMouseTransparent(true);
        boatCarrier.setCursor(Cursor.CLOSED_HAND);


        orgSceneX = mouseEvent.getSceneX();
        orgSceneY = mouseEvent.getSceneY();
        orgTranslateX = ((Rectangle) (mouseEvent.getSource())).getTranslateX();
        orgTranslateY = ((Rectangle) (mouseEvent.getSource())).getTranslateY();

        //na desni klik, rotacija
        if (mouseEvent.isSecondaryButtonDown()) {
            setRotation(boatCarrier);
        }
        System.out.println(boatCarrierX.get());

    }

    public void playerBoardDragDetected(MouseEvent mouseEvent) {
        playerBoard.startFullDrag();
    }

    public void boatCarrierMouseReleased(MouseEvent mouseEvent) {
        boatCarrier.setCursor(Cursor.OPEN_HAND);


        // Bounds boundsInScreen = scene2.localToScreen(scene2.getBoundsInLocal());

        if (mouseEvent.getSceneX() > ploca.getMaxX()) {
            ((Rectangle) (mouseEvent.getSource())).setTranslateX(orgTranslateX);
            ((Rectangle) (mouseEvent.getSource())).setTranslateY(orgTranslateY);
        }

        //boatCarrier.setMouseTransparent(false);
        playerBoard.toBack();

    }

    public void boatCarrierMouseDragged(MouseEvent mouseEvent) {

        double offsetX = mouseEvent.getSceneX() - orgSceneX;
        double offsetY = mouseEvent.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;

        boatCarrier.setTranslateX(newTranslateX);
        boatCarrier.setTranslateY(newTranslateY);

        Bounds brodic = boatCarrier.localToScene(boatCarrier.getBoundsInParent());

        Bounds polje = field.localToScene(field.getBoundsInLocal());
        //if(boatCarrier.intersects(polje))field.setFill(Color.RED);
        checkShapeIntersection(boatCarrier);

    }

    private void setRotation(Rectangle ship) {
        if (ship.getRotate() == 0)
            ship.setRotate(270);
        else
            ship.setRotate(0);
    }

    public void playerBoardMouseDragEntered(MouseDragEvent mouseDragEvent) {
        System.out.println("NO");
    }


    public void playerBoardMousePressed(MouseEvent mouseEvent) {
        System.out.println("AMRAA");
    }

    private void checkShapeIntersection(Shape block) {
        for (Shape static_bloc : nodes) {
            Shape intersect = Shape.intersect(block, static_bloc);
            if (intersect.getBoundsInLocal().getWidth() != -1)
                static_bloc.setFill(Color.GREEN);
            else
                static_bloc.setFill(Color.BLUE);
        }
    }
}


