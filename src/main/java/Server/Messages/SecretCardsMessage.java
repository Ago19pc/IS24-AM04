package Server.Messages;

import Client.Controller.ClientController;
import Server.Card.AchievementCard;
import Server.Controller.Controller;
import Server.Enums.Face;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.AlreadySetException;
import Server.Exception.MissingInfoException;
import Server.Exception.PlayerNotFoundByNameException;

import java.io.Serializable;
import java.util.List;

public class SecretCardsMessage implements Serializable, GeneralMessage {

    private String name;
    private AchievementCard secretCard;
    private List<AchievementCard> secretCards;

    public SecretCardsMessage(String name, AchievementCard secretCard) {
        this.name = name;
        this.secretCard = secretCard;
    }

    public SecretCardsMessage(List<AchievementCard> secretCards){
        this.secretCards = secretCards;
    }
    @Override
    public void serverExecute(Controller controller) {
        //la setSecretObjectiveCard va modificata
        try {
            controller.setSecretObjectiveCard(controller.getPlayerByName(this.name), this.secretCard);
        } catch (PlayerNotFoundByNameException | AlreadySetException | AlreadyFinishedException | MissingInfoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientExecute(ClientController controller) {

        for(int i = 0; i<2; i++){
            System.out.println(i + ": " + secretCards.get(i).getFace(Face.FRONT).getScoreRequirements().toString());
            System.out.println(secretCards.get(i).getFace(Face.FRONT).getScore());
        }

    }
}
