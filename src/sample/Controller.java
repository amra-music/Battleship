package sample;

import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;



public class Controller {
    public Rectangle boatSubmarine;
    public Rectangle boatCarrier;
    public Rectangle boatCruiser;
    public Rectangle boatDestroyer;
    public Rectangle boatBattleship;
    public GridPane playerBoard;
    private Image boatFiveImage = new Image("sample/boatFive.png");
    private Image boatFourImage = new Image("sample/boatFour.png");
    private Image boatThree1Image = new Image("sample/boatThree1.png");
    private Image boatThree2Image = new Image("sample/boatThree2.png");
    private Image boatTwoImage = new Image("sample/boatTwo.png");
    public GridPane scene2;

    @FXML
    public void initialize() {
        boatCarrier.setFill(new ImagePattern(boatFiveImage));
        boatBattleship.setFill(new ImagePattern(boatFourImage));
        boatCruiser.setFill(new ImagePattern(boatThree2Image));
        boatSubmarine.setFill(new ImagePattern(boatThree1Image));
        boatDestroyer.setFill(new ImagePattern(boatTwoImage));

        Image ocean = new Image("sample/ocean.jpg");

    }
  /*  boatCarrier.setOnDragDetected(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            /* drag was detected, start a drag-and-drop gesture*/
            /* allow any transfer mode */
            //Dragboard db = source.startDragAndDrop(TransferMode.ANY);

            /* Put a string on a dragboard


            event.consume();
        }
    }*/

    public void boatCarrierDragDetected(MouseEvent mouseEvent) {
        
        playerBoard.getChildren().set(0,boatCarrier);

    }

    public void boatCarrierMouseClicked(MouseEvent mouseEvent) {

       boatCarrierDragDetected(mouseEvent);
    }

    public void boatCarrierMousePressed(MouseEvent mouseEvent) {
        boatCarrier.setCursor(Cursor.CLOSED_HAND);
    }

    public void boatCarrierMouseReleased(MouseEvent mouseEvent) {
        boatCarrier.setCursor(Cursor.OPEN_HAND);
    }
}
