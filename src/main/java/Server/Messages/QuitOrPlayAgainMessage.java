package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.QuitOrPlayAgain;

import java.io.Serializable;

public class QuitOrPlayAgainMessage implements Serializable, GeneralMessage {

    private QuitOrPlayAgain quitOrPlayAgain;
    private String name;

    public QuitOrPlayAgainMessage(QuitOrPlayAgain quitOrPlayAgain, String name){
      this.quitOrPlayAgain = quitOrPlayAgain;
      this.name = name;
  }
    @Override
    public void serverExecute(Controller controller) {
        switch (this.quitOrPlayAgain){
            case QUIT -> controller.getConnectionHandler().killClient(this.name);
            //case PLAY_AGAIN ->
        }
    }

    @Override
    public void clientExecute(ClientController controller) {

    }
}
