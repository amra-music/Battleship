package sample;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Collections;


public class Controller {
    public Rectangle boatSubmarine;
    public Rectangle boatCarrier;
    public Rectangle boatCruiser;
    public Rectangle boatDestroyer;
    public Rectangle boatBattleship;
    public GridPane playerBoard;
    public Pane ships;
    private Image boatFiveImage = new Image("sample/boatFive.png");
    private Image boatFourImage = new Image("sample/boatFour.png");
    private Image boatThree1Image = new Image("sample/boatThree1.png");
    private Image boatThree2Image = new Image("sample/boatThree2.png");
    private Image boatTwoImage = new Image("sample/boatTwo.png");
    private Image ocean = new Image("sample/ocean.jpg");
    public GridPane scene2;
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


    @FXML
    public void initialize() {
        boatCarrier.setFill(new ImagePattern(boatFiveImage));
        boatBattleship.setFill(new ImagePattern(boatFourImage));
        boatCruiser.setFill(new ImagePattern(boatThree2Image));
        boatSubmarine.setFill(new ImagePattern(boatThree1Image));
        boatDestroyer.setFill(new ImagePattern(boatTwoImage));
    }


    public void boatCarrierDragDetected(MouseEvent mouseEvent) {

    }


    public void boatCarrierMousePressed(MouseEvent mouseEvent) {
        boatCarrier.setCursor(Cursor.CLOSED_HAND);


        orgSceneX = mouseEvent.getSceneX();
        orgSceneY = mouseEvent.getSceneY();
        orgTranslateX = ((Rectangle)(mouseEvent.getSource())).getTranslateX();
        orgTranslateY = ((Rectangle)(mouseEvent.getSource())).getTranslateY();
        //na desni klik, rotacija
        if (mouseEvent.isSecondaryButtonDown()) {
            setRotation(boatCarrier);
        }
    }
    public void boatCarrierMouseReleased(MouseEvent mouseEvent) {
        boatCarrier.setCursor(Cursor.OPEN_HAND);

        //ako nije na pravom mjestu vrati se na pocetno mjesto -- ako nije pozicioniran vrati se na pocetno
        /*if(mouseEvent.getSceneX()>playerBoard.getScene().getWidth())
            ((Rectangle)(mouseEvent.getSource())).setTranslateX(orgTranslateX);
            ((Rectangle)(mouseEvent.getSource())).setTranslateY(orgTranslateY);*/

    }

    public void boatCarrierMouseDragged(MouseEvent mouseEvent) {
        boatCarrier.toFront();
        double offsetX = mouseEvent.getSceneX() - orgSceneX;
        double offsetY = mouseEvent.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;

        ((Rectangle)(mouseEvent.getSource())).setTranslateX(newTranslateX);
        ((Rectangle)(mouseEvent.getSource())).setTranslateY(newTranslateY);
    }

    private void setRotation (Rectangle ship){
        if (ship.getRotate() == 0)
            ship.setRotate(270);
        else
            ship.setRotate(0);
    }

    // TODO Istražiti je li moguće uzeti svu djecu Vbox-a i napisati funkciju klikanja, rotiranja za sve brodiće odjednom

}
