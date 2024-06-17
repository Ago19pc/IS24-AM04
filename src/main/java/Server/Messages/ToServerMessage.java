package Server.Messages;

import Server.Controller.Controller;

/**
 * This interface is used to define the execution of a message that is sent to the server
 */
public interface ToServerMessage {
    /**
     * This method is used to execute the message on the server side
     * @param controller the controller where the message will be executed
     */
    public void serverExecute(Controller controller);
}
