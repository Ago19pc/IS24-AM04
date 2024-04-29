package Server.Messages;

import Client.Controller.ClientController;
import Server.Controller.Controller;
import Server.Enums.Color;
import Server.Exception.ServerExecuteNotCallableException;

import java.io.Serializable;
import java.util.List;

public class UnavailableColorsMessage implements Serializable, GeneralMessage {

    private List<Color> unavaiableColors;

    public UnavailableColorsMessage(List<Color> unavaiableColor) {
        this.unavaiableColors = unavaiableColor;
    }

    @Override
    public void serverExecute(Controller controller)throws ServerExecuteNotCallableException {
        throw new ServerExecuteNotCallableException();

    }
    @Override
    public void clientExecute(ClientController controller) {

        controller.setUnavaiableColors(unavaiableColors);

    }
}
