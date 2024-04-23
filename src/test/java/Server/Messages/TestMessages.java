package Server.Messages;

import Client.Connection.ClientConnectionHandler;
import Server.Connections.ServerConnectionHandler;
import Server.Controller.Controller;
import Server.Controller.ControllerInstance;
import Server.Enums.MessageType;
import Server.Player.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestMessages {
    @Test
    public void TestPlayerNameMessage() throws IOException, InterruptedException {
        // CREA UN SERVER
        ServerConnectionHandler connectionHandler;
        Controller controller;
        try {
            connectionHandler = new ServerConnectionHandler(true);
            controller = new ControllerInstance(connectionHandler);
            connectionHandler.setController(controller);
            connectionHandler.start();
        } catch (Exception e) {
            throw new IOException();
        }

        System.out.println("Starting Controller");
        controller.start();

        // CREA UN CLIENT
        ClientConnectionHandler cch = new ClientConnectionHandler(true);

        // GENERA UN MESSAGGIO
        PlayerNameMessage playerNameMessage = new PlayerNameMessage("TestPlayer2");

        // SERIALIZZA IL MESSAGGIO
        // INVIA IL MESSAGGIO
        try {
            cch.sendMessage(playerNameMessage, MessageType.PLAYERNAME);
        } catch (IOException e) {
            throw new IOException();
        }
        Thread.sleep(1000);

        System.out.println("PLAYERS LIST");
        for (Player p : controller.getPlayerList()) {
            System.out.println(p.getName());
        }
        System.out.println("END PLAYERS LIST");

    }
}
