package Server.Messages;

import Server.Connections.ServerConnectionHandler;
import Server.Controller.Controller;
import Server.Controller.ControllerInstance;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestMessages {
    @Test
    public void TestCardPlacementMessage() throws IOException {
        ServerConnectionHandler sch = new ServerConnectionHandler();
        Controller controller = new ControllerInstance(sch);



    }
}
