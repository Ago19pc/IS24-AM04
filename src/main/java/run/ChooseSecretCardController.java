package run;

import Server.Card.AchievementCard;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class ChooseSecretCardController extends SceneController {
    @FXML
    public Button confirmButton;
    @FXML
    public ImageView firstCard, secondCard;
    public Text firstCardText;
    public Text secondCardText;


    private int index = 0;


    public void chooseFirstCard() {
        index = 0;
        confirmButton.setDisable(false);
        firstCard.setStyle("-fx-opacity: 1;");
        secondCard.setStyle("-fx-opacity: 0.5;");
        confirmButton.setOpacity(1);
        confirmButton.setOpacity(1);
    }

    public void chooseSecondCard() {
        index = 1;
        confirmButton.setDisable(false);
        confirmButton.setOpacity(1);
        secondCard.setStyle("-fx-opacity: 1;");
        firstCard.setStyle("-fx-opacity: 0.5;");
        confirmButton.setOpacity(1);
    }

    public void confirmation() {
        controller.chooseSecretAchievement(index);
    }

    public void setUp(List<AchievementCard> cards) {
        confirmButton.setDisable(true);
        confirmButton.setOpacity(0);
        firstCard.setImage(new Image(getClass().getResource("/images/FrontFaces/" + cards.get(0).getImageURI()).toExternalForm()));
        secondCard.setImage(new Image(getClass().getResource("/images/FrontFaces/" + cards.get(1).getImageURI()).toExternalForm()));
    }
}
