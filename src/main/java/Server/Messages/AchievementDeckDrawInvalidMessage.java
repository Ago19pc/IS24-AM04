package Server.Messages;

import java.io.Serializable;

public class AchievementDeckDrawInvalidMessage implements Serializable, ToClientMessage{
    public AchievementDeckDrawInvalidMessage() {
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.achievementDeckDrawInvalid();
    }
}
