package run;

import Server.Card.Card;
import Server.Enums.Face;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainBoardSceneController extends SceneController {
    @FXML
    public ImageView firstCardImage, secondCardImage, thirdCardImage, secretCardImage;
    @FXML
    public Button rotateFirstCardButton, rotateSecondCardButton, rotateThirdCardButton;

    private Face firstFace, secondFace, thirdFace;


    public void setHandCards() {
        firstCardImage.setImage(getImageFromCard(controller.getHand().get(0), Face.FRONT));
        secondCardImage.setImage(getImageFromCard(controller.getHand().get(1), Face.FRONT));
        thirdCardImage.setImage(getImageFromCard(controller.getHand().get(2), Face.FRONT));
        firstFace = Face.FRONT;
        secondFace = Face.FRONT;
        thirdFace = Face.FRONT;
    }

    public void setSecretCard(Card card) {
        secretCardImage.setImage(getImageFromCard(card, Face.FRONT));
    }

    private Image getImageFromCard(Card card, Face face) {
        if (face == Face.FRONT) {
            return new Image(getClass().getResourceAsStream("/images/FrontFaces/" + card.getImageURI()));
        } else {
            return new Image(getClass().getResourceAsStream("/images/BackFaces/" + card.getImageURI()));
        }
    }

    public void rotateFirstCard() {
        firstCardImage.setImage(getImageFromCard(controller.getHand().get(0), firstFace.getOpposite()));
        firstFace = firstFace.getOpposite();
    }

    public void rotateSecondCard() {
        secondCardImage.setImage(getImageFromCard(controller.getHand().get(1), secondFace.getOpposite()));
        secondFace = secondFace.getOpposite();
    }

    public void rotateThirdCard() {
        thirdCardImage.setImage(getImageFromCard(controller.getHand().get(2), thirdFace.getOpposite()));
        thirdFace = thirdFace.getOpposite();
    }

}
