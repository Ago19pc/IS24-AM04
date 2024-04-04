package Server.Connections;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler extends Thread {
    private final Socket socket;
    BufferedReader in;
    PrintWriter out;
    String s;
    public ClientHandler(Socket client) throws IOException {
        this.socket = client;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    public void run() {

        System.out.println("Client connected: " + this.socket.getInetAddress());
        try {
            while ((s = in.readLine()) != null) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void sendMessages(String message){
        out.println(message);
    }

    public void receiveMessages(){
        try {
            String message = in.readLine();
            System.out.println("Received message: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
