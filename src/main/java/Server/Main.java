package Server;

import Server.Connections.ServerConnectionHandler;
import Server.Controller.Controller;
import Server.Controller.ControllerInstance;

import java.io.IOException;


public class Main {


    public static void main(String[] args) throws IOException {
        ServerConnectionHandler connectionHandler = new ServerConnectionHandler();
        Controller controller = new ControllerInstance(connectionHandler);
        try {
            connectionHandler = new ServerConnectionHandler();
            connectionHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again");
        }

        System.out.println("Starting Controller");
        controller.start();


    }

}
