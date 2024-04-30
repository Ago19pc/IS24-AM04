package Client.View;

import Client.Controller.ClientController;
import Server.Player.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
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
                System.out.println("setName <nome>: imposta il nome del giocatore");
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
            case "setName":
                if(args.length != 2){
                    System.out.println("Utilizzo corretto: setName <nome>");
                    return;
                }
                controller.askSetName(args[1]);
                break;
            default:
                System.out.println("Comando non valido. Digita \"help\" per la lista dei comandi");
        }
    }

    /**
     * Prints the new name
     * @param name new name
     */
    public void nameChanged(String name){
        System.out.println("Il tuo nome è stato cambiato in: " + name);
    }
    public void nameChangeFailed(){
        System.out.println("Impossibile cambiare il nome. Assicurati di aver inserito un nome disponibile");
        System.out.print(">");
    }

    /**
     * Prints the new player list
     * @param players list of players
     */
    public void playerListChanged(List<Player> players) {
        System.out.println("I giocatori ora sono: ");
        for (Player player : players) {
            System.out.println(player.getName());
        }
        System.out.print(">");
    }
}
