package Server.Messages;

import Server.Card.AchievementCard;
import Server.Controller.Controller;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

public class SecretCardSelection implements Serializable, GeneralMessage {

    private final String name;
    private final AchievementCard secretCard;

    public SecretCardSelection(String name, AchievementCard secretCard) {
        this.name = name;
        this.secretCard = secretCard;
    }

    @Override
    public void serverExecute(Controller controller) {
        try {
            controller.setSecretObjectiveCard(controller.getPlayerByName(this.name), this.secretCard);
        } catch (PlayerNotFoundByNameException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientExecute() {

    }
}
