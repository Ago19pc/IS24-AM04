package Server.Messages;

import java.io.Serializable;

public class QuitOrPlayAgainMessage implements Serializable {/*

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
    public void clientExecute(ClientController controller) throws ClientExecuteNotCallableException {

        throw new ClientExecuteNotCallableException();

    }*/
}
