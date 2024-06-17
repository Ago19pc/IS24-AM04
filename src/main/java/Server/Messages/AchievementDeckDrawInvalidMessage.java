package Server.Messages;

import java.io.Serializable;

/**
 * This message is used to inform the client that the draw from the achievement deck is invalid
 */
public class AchievementDeckDrawInvalidMessage implements Serializable, ToClientMessage{
    public AchievementDeckDrawInvalidMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.achievementDeckDrawInvalid();
    }
}
