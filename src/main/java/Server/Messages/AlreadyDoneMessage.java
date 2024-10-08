package Server.Messages;

import Client.Controller.ClientController;
import Server.Enums.Actions;

import java.io.Serializable;

/**
 * This message is used to inform the client that the action they tried to do is already done
 */
public class AlreadyDoneMessage implements Serializable, ToClientMessage {
    /**
     * The action that is already done
     */
    private final Actions action;

    /**
     * Constructor. The message needs to contain the action that is already done
     * @param action the action that is already done
     */
    public AlreadyDoneMessage(Actions action) {
        this.action = action;
    }
    @Override
    public void clientExecute(ClientController controller) {
        controller.alreadyDone(action);
    }
}
