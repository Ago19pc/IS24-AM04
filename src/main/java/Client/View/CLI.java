package Client.View;

import Client.Controller.ClientController;
import Server.Chat.Message;
import Server.Enums.Color;
import Server.Player.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class CLI extends Thread{
    private ClientController controller;
    private final Scanner in = new Scanner(System.in);

    public CLI(ClientController controller){
        System.out.println("Avvio gioco...");
        this.controller = controller;
    }
    public void printPromptLine(){
        System.out.println();
        System.out.print("> ");
    }
    public void printOnNewLine(String message){
        System.out.println();
        System.out.print(message);
    }

    /**
     * Runs CLI thread: reads input from the console and calls the decoding method
     */
    public void run(){
        printOnNewLine("> Digita \"help\" per la lista dei comandi");
        printPromptLine();
        while(true){
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
                printOnNewLine("-------------------");
                printOnNewLine(" Lista dei comandi");
                printOnNewLine("-------------------");
                printOnNewLine("");
                printOnNewLine("  help: mostra la lista dei comandi");
                printOnNewLine("");
                printOnNewLine("  join <ip> <porta>: si connette al server con l'indirizzo ip e la porta specificati");
                printOnNewLine("");
                printOnNewLine("  setName <nome>: imposta il nome del giocatore e entra in partita");
                printOnNewLine("");
                printOnNewLine("  setColor <colore>: imposta il colore del giocatore");
                printOnNewLine("  I colori disponibili sono: RED, YELLOW, BLUE, GREEN");
                printOnNewLine("");
                printOnNewLine("  ready: mettiti pronto per iniziare la partita");
                printOnNewLine("");
                printOnNewLine("  chat <messaggio>: invia un messaggio nella chat");
                printPromptLine();
                break;
            case "join":
                if (args.length != 3) {
                    printOnNewLine("Utilizzo corretto: join <ip> <porta>");
                    return;
                }
                try {
                    controller.getClientConnectionHandler().setSocket(new Socket(args[1], Integer.parseInt(args[2])));
                    printOnNewLine("Connessione avvenuta con successo al server " + args[1] + ":" + args[2]);
                } catch (IOException e) {
                    printOnNewLine("Impossibile connettersi al server");
                }
                printPromptLine();
                break;
            case "setName":
                if(args.length != 2){
                    printOnNewLine("Utilizzo corretto: setName <nome>");
                    return;
                }
                controller.askSetName(args[1]);
                break;
            case "setColor":
                if(args.length != 2){
                    printOnNewLine("Utilizzo corretto: setColor <colore>");
                    return;
                }
                controller.askSetColor(args[1]);
                break;
            case "ready":
                controller.setReady();
                break;
            case "chat":
                String message = "";
                for(int i = 1; i < args.length; i++){
                    message += args[i] + " ";
                }
                controller.sendChatMessage(message);
                break;
            default:
                printOnNewLine("Comando non valido. Digita \"help\" per la lista dei comandi");
                printPromptLine();
        }
    }

    /**
     * Prints the new name
     * @param name new name
     */
    public void nameChanged(String name){
        printOnNewLine("Sei entrato in partita. Il tuo nome è: " + name);
        printPromptLine();
    }
    public void nameChangeFailed(){
        printOnNewLine("Impossibile impostare il nome. Assicurati di non avere già un nome e di aver inserito un nome disponibile");
        printPromptLine();
    }

    /**
     * Prints the new player list
     */
    public void playerListChanged() {
        printOnNewLine("E' entrato un nuovo giocatore! I giocatori ora sono: ");
        for (Player player : controller.getPlayers()) {
            System.out.print(player.getName() + " ");
        }
        printPromptLine();
    }

    /**
     * Tells the user that the player name is needed
     * Called by the controller when the user tries to set a color without setting a name
     */
    public void needName(){
        printOnNewLine("Devi impostare il tuo nome prima di poter impostare il colore");
        printPromptLine();
    }

    /**
     * Tells the user that either the player color or the player name is needed
     * Called by the controller when the user tries to set ready without having set a name or a color
     */
    public void needNameOrColor() {
        printOnNewLine("Devi impostare il tuo nome e il tuo colore prima di poter iniziare la partita");
        printPromptLine();
    }

    /**
     * Tells the user that the color requested is not available
     * Called by the controller when the user tries to set an unavailable color
     */
    public void unavailableColor() {
        printOnNewLine("Il colore richiesto non è disponibile. I colori disponibili sono: ");
        for (Color color : controller.getAvailableColors()) {
            System.out.println(color + " ");
        }
        printPromptLine();
    }

    /**
     * Tells the user that the color requested is not valid
     * Called by the controller when the user tries to set an invalid color
     */
    public void invalidColor() {
        printOnNewLine("Il colore richiesto non è valido. I colori validi sono: RED, YELLOW, BLUE, GREEN");
        printPromptLine();
    }

    /**
     * Shows the players and their colors
     */
    public void displayPlayerColors(){
        printOnNewLine("I giocatori hanno i seguenti colori: \n");
        for (Player player : controller.getPlayers()) {
            System.out.println("    " + player.getName() + ": " + player.getColor());
        }
        printPromptLine();
    }

    /**
     * Tells the user that the player color has been changed successfully
     */
    public void colorChanged() {
        printOnNewLine("Il tuo colore è stato cambiato con successo. Ora sei " + controller.getMyColor());
        printPromptLine();
    }

    /**
     * Tells the user that the player color has not been changed
     */
    public void colorChangeFailed() {
        printOnNewLine("Impossibile cambiare il colore. Assicurati di aver inserito un colore disponibile");
        printPromptLine();
    }

    /**
     * Tells the user that a player is ready
     * @param name name of the player
     * @param ready ready status
     */
    public void updateReady(String name, boolean ready) {
        if(Objects.equals(name, controller.getMyName())){
            if (ready) {
                printOnNewLine("Ora sei pronto");
            } else {
                printOnNewLine("Non sei più pronto");
            }
        } else {
            if (ready) {
                printOnNewLine(name + " è pronto");
            } else {
                printOnNewLine(name + " non è più pronto");
            }
        }
        printPromptLine();
    }

    /**
     * Tells the user that an action cannot be performed because the client is not connected to any server
     */
    public void needConnection() {
        printOnNewLine("Devi connetterti a un server prima di poter eseguire questa azione");
        printOnNewLine("Per connetterti a un server utilizza: join <ip> <porta>");
        printPromptLine();
    }

    /**
     * Displays a chat message
     * @param message chat message
     */
    public void chat(Message message){
        printOnNewLine(message.getName() + ": " + message.getMessage());
        printPromptLine();
    }
}
