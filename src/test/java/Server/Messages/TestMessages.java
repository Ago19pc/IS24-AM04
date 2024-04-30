package Server.Messages;

import Client.Connection.ClientConnectionHandler;
import Client.Controller.ClientController;
import Server.Connections.ServerConnectionHandler;
import Server.Controller.Controller;
import Server.Controller.ControllerInstance;
import Server.Enums.Color;
import Server.Enums.Face;
import Server.Exception.AlreadySetException;
import Server.Exception.PlayerNotFoundByNameException;
import Server.Exception.TooFewElementsException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMessages {
    @Test
    public void TestPlayerNameMessage() throws IOException, InterruptedException, PlayerNotFoundByNameException, TooFewElementsException, AlreadySetException {
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


        // CREA UN CLIENT
        ClientController clientController1 = new ClientController();
        ClientController clientController2 = new ClientController();
        clientController1.mainDebug();
        clientController2.mainDebug();
        ClientController clientController3 = new ClientController();
        clientController3.mainDebug();
       //*  clientController1.debugConnect();
      //*   clientController2.debugConnect();
        ClientConnectionHandler cch =  clientController1.getClientConnectionHandler();
        ClientConnectionHandler cch2 =  clientController2.getClientConnectionHandler();
        ClientConnectionHandler cch3 =  clientController3.getClientConnectionHandler();


        // GENERA UN MESSAGGIO
        PlayerNameMessage playerNameMessage = new PlayerNameMessage("TestPlayer1");
        PlayerNameMessage playerNameMessage2 = new PlayerNameMessage("TestPlayer1");
        PlayerNameMessage playerNameMessage3 = new PlayerNameMessage("TestPlayer2");


        // SERIALIZZA IL MESSAGGIO
        // INVIA IL MESSAGGIO
        try {
            cch.sendMessage(playerNameMessage, MessageType.PLAYERNAME);
            cch2.sendMessage(playerNameMessage2, MessageType.PLAYERNAME);
            cch3.sendMessage(playerNameMessage3, MessageType.PLAYERNAME);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        Thread.sleep(100);

        assertEquals(2, controller.getPlayerList().size());

        assertTrue(controller.getPlayerList().stream().anyMatch(p -> p.getName().equals("TestPlayer1")));
        assertTrue(controller.getPlayerList().stream().anyMatch(p -> p.getName().equals("TestPlayer2")));

        // NOW TESTING COLOR MESSAGE
        PlayerColorMessage playerColorMessage = new PlayerColorMessage("TestPlayer1", Color.RED);
        PlayerColorMessage playerColorMessage2 = new PlayerColorMessage("TestPlayer2", Color.BLUE);

        try {
            cch.sendMessage(playerColorMessage, MessageType.PLAYERCOLOR);
            cch2.sendMessage(playerColorMessage2, MessageType.PLAYERCOLOR);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        Thread.sleep(100);
        assertTrue(controller.getPlayerList().stream().anyMatch(p -> p.getColor().equals(Color.RED)));
        assertTrue(controller.getPlayerList().stream().anyMatch(p -> p.getColor().equals(Color.BLUE)));

        // NOW TESTING READY MESSAGE
        ReadyStatusMessage readyStatusMessage = new ReadyStatusMessage(true, "TestPlayer1");
        ReadyStatusMessage readyStatusMessage2 = new ReadyStatusMessage(true, "TestPlayer2");

        try {
            cch.sendMessage(readyStatusMessage, MessageType.READYSTATUS);
            cch2.sendMessage(readyStatusMessage2, MessageType.READYSTATUS);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        Thread.sleep(100);
        assertEquals(2 , clientController1.getPlayers().size());
        assertTrue(controller.getPlayerList().get(0).isReady());
        assertTrue(controller.getPlayerList().get(1).isReady());



        // NOW TESTING STARTING CARD MESSAGE

        StartingCardsMessage startingCardMessage = new StartingCardsMessage("TestPlayer1", Face.FRONT);
        StartingCardsMessage startingCardMessage2 = new StartingCardsMessage("TestPlayer2", Face.BACK);

        try {
            cch.sendMessage(startingCardMessage, MessageType.STARTINGCARDS);
            cch2.sendMessage(startingCardMessage2, MessageType.STARTINGCARDS);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        Thread.sleep(100);
        // DA ASPETTARE AGGIORNAMENTO DI FILIPPO SU SETSTARTINGCARD
        System.out.println("TO CORRECT STARTING CARD TEST");
        // COMUNQUE PER FARE QUESTO TEST, CONTROLLA I SINGOLI VALORI DELLE CARTE, AL POSTO CHE SIANO LO STESSO OGGETTO
        //assertEquals(startingFrontFace, controller.getPlayerByName("TestPlayer1").getManuscript().getCardByCoord(0,0));
        //assertEquals(cornerCardFace, controller.getPlayerByName("TestPlayer2").getManuscript().getCardByCoord(0,0));



    }



}
