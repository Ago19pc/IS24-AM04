package Server.Messages;

import Server.Controller.Controller;

public interface ToServerMessage {
    /**
     * This method is used to execute the message on the server side
     * @param controller the controller where the message will be executed
     */
    void serverExecute(Controller controller);
}
