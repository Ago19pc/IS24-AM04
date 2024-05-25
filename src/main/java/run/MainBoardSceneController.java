package run;

import Server.Card.ResourceCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainBoardSceneController extends SceneController {
    @FXML
    public ImageView firstCardImage, secondCardImage, thirdCardImage, secretCardImage;
    @FXML
    public Button rotateFirstCardButton, rotateSecondCardButton, rotateThirdCardButton;


    public void setHandCards() {
        firstCardImage.setImage(getImageFromCard(controller.getHand().get(0)));
        secondCardImage.setImage(getImageFromCard(controller.getHand().get(1)));
        thirdCardImage.setImage(getImageFromCard(controller.getHand().get(2)));
    }

    private Image getImageFromCard(ResourceCard resourceCard) {
        return new Image(getClass().getResourceAsStream("/images/FrontFaces/0.jpeg"));
    }
}
