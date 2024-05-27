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
    public ImageView firstCard, secondCard, chosenCard;
    @FXML
    public Text waitText;
    public Text backFaceText;
    public Text frontFaceText;
    public Text chosenCardText;
    @FXML
    private Face chosenFace;



    public void chooseFirstCard() {
        chosenFace = Face.FRONT;
        confirmButton.setDisable(false);
        firstCard.setStyle("-fx-opacity: 1;");
        secondCard.setStyle("-fx-opacity: 0.5;");
        confirmButton.setOpacity(1);
        chosenCardText.setText("Back Face");
        chosenCard.setImage(firstCard.getImage());
    }

    public void chooseSecondCard() {
        chosenFace = Face.BACK;
        confirmButton.setDisable(false);
        secondCard.setStyle("-fx-opacity: 1;");
        firstCard.setStyle("-fx-opacity: 0.5;");
        confirmButton.setOpacity(1);
        chosenCardText.setText("Front Face");
        chosenCard.setImage(secondCard.getImage());
    }

    public void confirmation() {
        controller.chooseStartingCardFace(chosenFace);
        waitText.setVisible(true);
        firstCard.setOpacity(0);
        firstCard.setDisable(true);
        secondCard.setOpacity(0);
        secondCard.setDisable(true);
        confirmButton.setVisible(false);
        confirmButton.setDisable(true);
        backFaceText.setOpacity(0);
        frontFaceText.setOpacity(0);
        chosenCardText.setOpacity(1);
        chosenCard.setLayoutX(414);
        chosenCard.setLayoutY(264);
        chosenCard.setOpacity(1);
    }

    public void setUp(Card card) {
        confirmButton.setDisable(true);
        confirmButton.setOpacity(0);
        firstCard.setImage(new Image(getClass().getResource("/images/FrontFaces/"+ card.getImageURI()).toExternalForm()));
        secondCard.setImage(new Image(getClass().getResource("/images/BackFaces/"+ card.getImageURI()).toExternalForm()));
        waitText.setVisible(false);
        chosenCardText.setOpacity(0);
        chosenCard.setOpacity(0);
    }
}
