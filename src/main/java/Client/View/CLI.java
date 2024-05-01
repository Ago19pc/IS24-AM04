package Client.View;

import Client.Controller.ClientController;
import Server.Enums.Color;
import Server.Player.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CLI extends Thread{
    private ClientController controller;
    private final Scanner in = new Scanner(System.in);

    public CLI(ClientController controller){
        System.out.println("Avvio gioco...");
        this.controller = controller;
    }

    /**
     * Runs CLI thread: reads input from the console and calls the decoding method
     */
    public void run(){
        System.out.println("> Digita \"help\" per la lista dei comandi");
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
                System.out.println("-------------------");
                System.out.println(" Lista dei comandi");
                System.out.println("-------------------");
                System.out.println();
                System.out.println("> help: mostra la lista dei comandi");
                System.out.println();
                System.out.println("> join <ip> <porta>: si connette al server con l'indirizzo ip e la porta specificati");
                System.out.println();
                System.out.println("> setName <nome>: imposta il nome del giocatore e entra in partita");
                System.out.println();
                System.out.println("> setColor <colore>: imposta il colore del giocatore");
                System.out.println("  I colori disponibili sono: RED, YELLOW, BLUE, GREEN");
                System.out.println();
                System.out.println("> ready: mettiti pronto per iniziare la partita");
                break;
            case "join":
                if (args.length != 3) {
                    System.out.println("> Utilizzo corretto: join <ip> <porta>");
                    return;
                }
                try {
                    controller.getClientConnectionHandler().setSocket(new Socket(args[1], Integer.parseInt(args[2])));
                    System.out.println("> Connessione avvenuta con successo al server " + args[1] + ":" + args[2]);
                } catch (IOException e) {
                    System.out.println("> Impossibile connettersi al server");
                }
                break;
            case "setName":
                if(args.length != 2){
                    System.out.println("> Utilizzo corretto: setName <nome>");
                    return;
                }
                controller.askSetName(args[1]);
                break;
            case "setColor":
                if(args.length != 2){
                    System.out.println("> Utilizzo corretto: setColor <colore>");
                    return;
                }
                controller.askSetColor(args[1]);
                break;
            case "ready":
                controller.setReady();
                break;
            default:
                System.out.println("> Comando non valido. Digita \"help\" per la lista dei comandi");
        }
    }

    /**
     * Prints the new name
     * @param name new name
     */
    public void nameChanged(String name){
        System.out.println("Sei entrato in partita. Il tuo nome è: " + name);
    }
    public void nameChangeFailed(){
        System.out.println("Impossibile impostare il nome. Assicurati di non avere già un nome e di aver inserito un nome disponibile");
    }

    /**
     * Prints the new player list
     */
    public void playerListChanged() {
        System.out.print("> E' entrato un nuovo giocatore! I giocatori ora sono: ");
        for (Player player : controller.getPlayers()) {
            System.out.print(player.getName() + " ");
        }
        System.out.println();
    }

    /**
     * Tells the user that the player name is needed
     * Called by the controller when the user tries to set a color without setting a name
     */
    public void needName(){
        System.out.println("> Devi impostare il tuo nome prima di poter impostare il colore");
    }

    /**
     * Tells the user that either the player color or the player name is needed
     * Called by the controller when the user tries to set ready without having set a name or a color
     */
    public void needNameOrColor() {
        System.out.println("> Devi impostare il tuo nome e il tuo colore prima di poter iniziare la partita");
    }

    /**
     * Tells the user that the color requested is not available
     * Called by the controller when the user tries to set an unavailable color
     */
    public void unavailableColor() {
        System.out.println("> Il colore richiesto non è disponibile. I colori disponibili sono: ");
        for (Color color : controller.getAvailableColors()) {
            System.out.println(color + " ");
        }
    }

    /**
     * Tells the user that the color requested is not valid
     * Called by the controller when the user tries to set an invalid color
     */
    public void invalidColor() {
        System.out.println("> Il colore richiesto non è valido. I colori validi sono: RED, YELLOW, BLUE, GREEN");
    }

    /**
     * Shows the players and their colors
     */
    public void displayPlayerColors(){
        System.out.println("> I giocatori hanno i seguenti colori: ");
        for (Player player : controller.getPlayers()) {
            System.out.println("    " + player.getName() + ": " + player.getColor());
        }
    }

    /**
     * Tells the user that the player color has been changed successfully
     */
    public void colorChanged() {
        System.out.println("> Il tuo colore è stato cambiato con successo. Ora sei " + controller.getMyColor());
    }

    /**
     * Tells the user that the player color has not been changed
     */
    public void colorChangeFailed() {
        System.out.println("> Impossibile cambiare il colore. Assicurati di aver inserito un colore disponibile");
    }

    /**
     * Tells the user that a player is ready
     * @param name name of the player
     * @param ready ready status
     */
    public void updateReady(String name, boolean ready) {
        if(name == controller.getMyName()){
            if (ready) {
                System.out.println("> Ora sei pronto");
            } else {
                System.out.println("> Non sei più pronto");
            }
        } else {
            if (ready) {
                System.out.println("> " + name + " è pronto");
            } else {
                System.out.println("> " + name + " non è più pronto");
            }
        }
    }

    /**
     * Tells the user that an action cannot be performed because the client is not connected to any server
     */
    public void needConnection() {
        System.out.println("> Devi connetterti a un server prima di poter eseguire questa azione");
        System.out.println("> Per connetterti a un server utilizza: join <ip> <porta>");
    }
}
