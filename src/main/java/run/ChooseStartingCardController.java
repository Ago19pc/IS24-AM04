package run;

import Server.Card.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import Server.Enums.Face;

public class ChooseStartingCardController extends SceneController {
    @FXML
    public Button confirmButton;
    @FXML
    public ImageView firstCard, secondCard;
    @FXML
    public Text waitText;

    private Face chosenFace;


    public void chooseFirstCard() {
        chosenFace = Face.FRONT;
        confirmButton.setDisable(false);
        firstCard.setStyle("-fx-opacity: 1;");
        secondCard.setStyle("-fx-opacity: 0.5;");
    }

    public void chooseSecondCard() {
        chosenFace = Face.BACK;
        confirmButton.setDisable(false);


        secondCard.setStyle("-fx-opacity: 1;");
        firstCard.setStyle("-fx-opacity: 0.5;");

    }

    public void confirmation() {
        controller.chooseStartingCardFace(chosenFace);
        waitText.setVisible(true);
        confirmButton.setVisible(false);
        confirmButton.setDisable(true);
    }

    public void setUp(Card card) {
        confirmButton.setDisable(true);
        firstCard.setImage(new Image(getClass().getResource("/images/FrontFaces/"+ card.getImageURI()).toExternalForm()));
        secondCard.setImage(new Image(getClass().getResource("/images/BackFaces/"+ card.getImageURI()).toExternalForm()));
        waitText.setVisible(false);
    }
}
