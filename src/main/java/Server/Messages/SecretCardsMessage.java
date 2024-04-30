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
    private int secretCardChosen;
    private List<AchievementCard> secretCards;

    public SecretCardsMessage(String name, int secretCardChosen) {
        this.name = name;
        this.secretCardChosen = secretCardChosen;
    }

    public SecretCardsMessage(List<AchievementCard> secretCards){
        this.secretCards = secretCards;
    }
    @Override
    public void serverExecute(Controller controller) {
        //la setSecretObjectiveCard va modificata
        try {
            controller.setSecretObjectiveCard(controller.getPlayerByName(this.name), this.secretCardChosen);
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
