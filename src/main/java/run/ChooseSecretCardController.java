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
    @FXML
    Text waitText;

    private int index = 0;


    public void chooseFirstCard() {
        index = 0;
        confirmButton.setDisable(false);
        firstCard.setStyle("-fx-opacity: 1;");
        secondCard.setStyle("-fx-opacity: 0.5;");
    }

    public void chooseSecondCard() {
        index = 1;
        confirmButton.setDisable(false);


        secondCard.setStyle("-fx-opacity: 1;");
        firstCard.setStyle("-fx-opacity: 0.5;");

    }

    public void confirmation() {
        controller.chooseSecretAchievement(index);
        waitText.setVisible(true);
        confirmButton.setVisible(false);
        confirmButton.setDisable(true);
    }

    public void setUp(List<AchievementCard> cards) {
        confirmButton.setDisable(true);
        firstCard.setImage(new Image(getClass().getResource("/images/FrontFaces/0.jpeg").toExternalForm()));
        secondCard.setImage(new Image(getClass().getResource("/images/BackFaces/0.jpeg").toExternalForm()));
        waitText.setVisible(false);
    }
}
