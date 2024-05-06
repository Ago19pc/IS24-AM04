package Client.View;

import Client.Controller.ClientController;
import Client.Deck;
import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Chat.Message;
import Server.Enums.Actions;
import Server.Enums.Color;
import Client.Player;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Messages.StartingCardsMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class CLI extends Thread{
    private ClientController controller;
    private final Scanner in = new Scanner(System.in);

    //temp variables
    private List<AchievementCard> potentialSecretAchievements;
    private Card startingCard;

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
                controller.joinServer(args[1], Integer.parseInt(args[2]));
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

    // Successful actions

    /**
     * Prints the new name
     * @param name new name
     */
    public void nameChanged(String name){
        printOnNewLine("Sei entrato in partita. Il tuo nome è: " + name);
        printPromptLine();
    }
    /**
     * Tells the user that the player color has been changed successfully
     */
    public void colorChanged() {
        printOnNewLine("Il tuo colore è stato cambiato con successo. Ora sei " + controller.getMyColor());
        printPromptLine();
    }
    public void successfulConnection(){
        printOnNewLine("Connessione avvenuta con successo al server");
        printPromptLine();
    }
    public void displayNewCardInHand(){
        Card newCard = controller.getHand().getLast();
        printOnNewLine("Hai pescato la carta: " + newCard);
        printPromptLine();
    }

    // Failed actions

    public void nameChangeFailed(){
        printOnNewLine("Impossibile impostare il nome. Assicurati di non avere già un nome e di aver inserito un nome disponibile");
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
    public void needColor(){
        printOnNewLine("Devi prima impostare il tuo colore");
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
     * Tells the user that the player color has not been changed
     */
    public void colorChangeFailed() {
        printOnNewLine("Impossibile cambiare il colore. Assicurati di aver inserito un colore disponibile");
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
    public void connectionFailed(){
        printOnNewLine("Connessione al server fallita");
        printPromptLine();
    }
    public void cantDrawAchievementCards(){
        printOnNewLine("Non puoi pescare le carte obiettivo");
        printPromptLine();
    }
    public void alreadyDone(Actions action){
        printOnNewLine("Hai già eseguito questa azione: " + action);
        printPromptLine();
    }
    public void cardNotPlaceable(){
        printOnNewLine("Piazzamento carta fallito");
        printPromptLine();
    }
    public void chatMessageIsEmpty(){
        printOnNewLine("Il messaggio non può essere vuoto");
        printPromptLine();
    }
    public void deckIsEmpty(){
        printOnNewLine("Il mazzo è vuoto");
        printPromptLine();
    }
    public void gameAlreadyStarted(){
        printOnNewLine("La partita è già iniziata");
        printPromptLine();
    }
    public void gameNotYetStarted(){
        printOnNewLine("La partita non è ancora iniziata");
        printPromptLine();
    }
    public void invalidCardForAction(Actions cardType){
        printOnNewLine("La carta selezionata non esiste o non è nella tua mano");
        printPromptLine();
    }
    public void notYetGivenCard(Actions cardType){
        printOnNewLine("Le carte " + cardType + " non sono ancora state date");
        printPromptLine();
    }
    public void notYourTurn(){
        printOnNewLine("Non è il tuo turno");
        printPromptLine();
    }

    // Updates

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
     * Displays a chat message
     * @param message chat message
     */
    public void chat(Message message){
        printOnNewLine(message.getName() + ": " + message.getMessage());
        printPromptLine();
    }
    public void displayCommonAchievements(){
        printOnNewLine("Gli obiettivi comuni sono: \n");
        for (int i = 0; i < controller.getCommonAchievements().size(); i++) {
            System.out.println("    " + i + ": " + controller.getCommonAchievements().get(i));
        }
        printPromptLine();
    }
    public void chooseSecretAchievement(List<Card> possibleAchievements){
        printOnNewLine("Scegli un obiettivo segreto: \n");
        potentialSecretAchievements = new ArrayList<>();
        for (int i = 0; i < possibleAchievements.size(); i++) {
            potentialSecretAchievements.add((AchievementCard) possibleAchievements.get(i));
            System.out.println("    " + i + ": " + possibleAchievements.get(i));
        }
        printPromptLine();
        //todo: add selection
    }
    public void endGameStarted(){
        printOnNewLine("La partita sta per finire");
        printPromptLine();
    }
    public void displayInitialHand(){
        printOnNewLine("La tua mano contiene le carte: \n");
        for (Card card : controller.getHand()) {
            System.out.println("    " + card);
        }
        printPromptLine();
    }
    public void displayLeaderboard(){
        printOnNewLine("Classifica: \n");
        controller.getLeaderboard().forEach((player, points) -> {
            System.out.println("    " + player + ": " + points);
        });
        printPromptLine();
    }
    public void displayNewPlayer(){
        printOnNewLine("E' entrato un nuovo giocatore: " + controller.getPlayers().getLast().getName());
        printPromptLine();
    }
    public void otherPlayerDraw(String name, Decks deckFrom, DeckPosition position){
        printOnNewLine(name + " ha pescato una carta dalla posizione " + position + " del mazzo " + deckFrom);
        printOnNewLine("Ora il mazzo ha " + controller.getDeckSize(deckFrom));
        printOnNewLine("Le carte a terra sono" + controller.getBoardCards(deckFrom));
        printPromptLine();
    }
    public void newTurn(){
        printOnNewLine("E' il turno " + controller.getTurn() + ". Tocca a " + controller.getActivePlayer());
        printPromptLine();
    }
    public void otherPlayerInitialHand(String name){
        printOnNewLine(name + " ha ricevuto la sua mano iniziale");
        printPromptLine();
    }
    public void secretAchievementChosen(String name){
        printOnNewLine(name + " ha scelto il proprio obiettivo segreto");
        printPromptLine();
    }
    public void startingCardChosen(String name){
        printOnNewLine(name + " ha scelto la propria carta iniziale");
        printPromptLine();
    }
    public void gameStarted(){
        printOnNewLine("La partita è iniziata!");
        printPromptLine();
    }
    public void displayPlayerOrder(){
        printOnNewLine("L'ordine dei giocatori è:");
        for (int i = 0; i < controller.getPlayerNames().size(); i++) {
            System.out.print(" " + i + ": " + controller.getPlayerNames());
        }
        printPromptLine();
    }
    public void displayBoardCards(){
        printOnNewLine("Le carte a terra sono: \n");
        printOnNewLine("MAZZO ORO: \n");
        for (Card card : controller.getBoardCards(Decks.GOLD)) {
            System.out.println("    " + card);
        }
        printOnNewLine("MAZZO RISORSA: \n");
        for (Card card : controller.getBoardCards(Decks.RESOURCE)) {
            System.out.println("    " + card);
        }
        printPromptLine();
    }
    public void chooseStartingCardFace(Card card){
        printOnNewLine("Scegli la faccia della carta iniziale: \n" + card);
        printPromptLine();
    }
    public void doFirst(Actions action){
        printOnNewLine("Devi prima " + action);
        printPromptLine();
    }
    public void displayLobby(){
        printOnNewLine("I giocatori sono: ");
        for (Player player : controller.getPlayers()) {
            System.out.print("    " + player.getName() + " - " + player.getColor() + " - ");
            if(player.isReady()){
                System.out.print("Pronto");
            } else {
                System.out.print("Non pronto");
            }
        }
        printPromptLine();
    }
}
