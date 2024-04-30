package Client.View;

import Client.Controller.ClientController;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CLI extends Thread{
    private ClientController controller;
    private Scanner in = new Scanner(System.in);

    public CLI(ClientController controller){
        System.out.println("Avvio gioco...");
        this.controller = controller;

    }

    /**
     * Runs CLI thread: reads input from the console and calls the decoding method
     */
    public void run(){
        System.out.println("Digita \"help\" per la lista dei comandi");
        while(true){
            System.out.print("> ");
            String[] args = in.nextLine().split(" ");
            decode(args);
        }
    }

    /**
     * Decodes the input from the console and calls the corresponding method in the controller
     */
    public void decode(String[] args){
        switch(args[0]){
            case "help":
                System.out.println("Lista dei comandi:");
                System.out.println("help: mostra la lista dei comandi");
                System.out.println("join <ip> <porta>: si connette al server con l'indirizzo ip e la porta specificati");
                break;
            case "join":
                if (args.length != 3) {
                    System.out.println("Utilizzo corretto: join <ip> <porta>");
                    return;
                }
                try {
                    controller.getClientConnectionHandler().setSocket(new Socket(args[1], Integer.parseInt(args[2])));
                    System.out.println("Connessione avvenuta con successo al server " + args[1] + " : " + args[2]);
                } catch (IOException e) {
                    System.out.println("Impossibile connettersi al server");
                }
                break;
            default:
                System.out.println("Comando non valido. Digita \"help\" per la lista dei comandi");
        }
    }
}
