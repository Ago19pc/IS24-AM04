package Server.Messages;

import Server.Card.AchievementCard;
import Server.Controller.Controller;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.AlreadySetException;
import Server.Exception.MissingInfoException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;

public class SecretCardsMessage implements Serializable, GeneralMessage {

    private final String name;
    private final AchievementCard secretCard;

    public SecretCardsMessage(String name, AchievementCard secretCard) {
        this.name = name;
        this.secretCard = secretCard;
    }

    @Override
    public void serverExecute(Controller controller) {
        try {
            controller.setSecretObjectiveCard(controller.getPlayerByName(this.name), this.secretCard);
        } catch (PlayerNotFoundByNameException | AlreadySetException | AlreadyFinishedException | MissingInfoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientExecute() {

    }
}
