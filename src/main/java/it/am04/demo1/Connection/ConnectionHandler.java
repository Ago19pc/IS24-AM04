package it.am04.demo1.Connection;

import ConnectionUtils.MessagePacket;
import ConnectionUtils.Receiver;
import ConnectionUtils.Sender;
import Payloads.PlayerNamePayload;
import Server.Enums.EventType;
import Server.EventManager.EventManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler {
    private Socket clientSocket;

    public Sender sender;
    public Receiver receiver;

    private EventManager eventManager;

    public ConnectionHandler(String ip, int port, EventManager eventManager) {
        this.eventManager = eventManager;
        try {
            startConnection(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        System.out.println("Connection established");
        sender = new Sender(clientSocket);
        receiver = new Receiver(clientSocket, eventManager);

        try {
            Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread th, Throwable ex) {
                    System.out.println("Uncaught exception: " + ex);
                    try {
                        clientSocket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            receiver.setUncaughtExceptionHandler(h);
            sender.setUncaughtExceptionHandler(h);

            sender.start();
            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        PlayerNamePayload payload = new PlayerNamePayload("ciao");
        MessagePacket message = new MessagePacket(payload, EventType.PLAYERSDATA);
        //String json = gson.toJson(message);
        //sender.sendMessage(json);

    }

    public void sendMessage(String msg) throws IOException {
        sender.sendMessage(msg);
    }

    public void sendMessage() throws IOException {
        sender.sendMessage();
    }



    public void stopConnection() throws IOException {
        sender.interrupt();
        receiver.interrupt();
        clientSocket.close();
    }
}
