package Server.Messages;

import Server.Enums.Actions;

import java.io.Serializable;

/**
 * Message to inform the client that he has to do something before doing what he tried to do
 */
public class ToDoFirstMessage implements Serializable, ToClientMessage {
    private final Actions actionToDo;
    /**
     * Constructor
     * @param actionToDo the action to do
     */
    public ToDoFirstMessage(Actions actionToDo) {
        this.actionToDo = actionToDo;
    }
    @Override
    public void clientExecute(Client.Controller.ClientController controller) {
        controller.toDoFirst(actionToDo);
    }
}
