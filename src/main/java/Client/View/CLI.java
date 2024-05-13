package Client.View;

import Client.Controller.ClientController;
import Client.Player;
import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.CardFace;
import Server.Card.ResourceCard;
import Server.Chat.Message;
import Server.Enums.*;
import Server.Exception.PlayerNotFoundByNameException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class CLI extends Thread{
    private ClientController controller;
    private final Scanner in = new Scanner(System.in);

    //temp variables
    private List<AchievementCard> potentialSecretAchievements;
    private Integer chosenHandCard;

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
     * Asks the user which connection mode to use
     */
    public void askConnectionMode(){
        printOnNewLine("Vuoi connetterti tramite RMI o SOCKET?");
        printPromptLine();
        String connectionMode = in.nextLine().toUpperCase();
        if(connectionMode.equals("RMI")) {
            controller.setRMIMode(true);
        } else if (connectionMode.equals("SOCKET")) {
            controller.setRMIMode(false);
        } else {
            printOnNewLine("Modalità di connessione non valida.");
            askConnectionMode();
        }
        printPromptLine();
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
                printOnNewLine("");
                printOnNewLine("  chooseFace <faccia>: scegli la faccia della carta selezionata");
                printOnNewLine("  Le facce disponibili sono: FRONT, BACK");
                printOnNewLine("");
                printOnNewLine("  chooseCard <numero>: scegli il numero della carta da selezizonare. Se vuoi la prima carta digita 1, etc.");
                printOnNewLine("");
                printOnNewLine("  printHand: mostra le carte nella tua mano");
                printOnNewLine("");
                printOnNewLine("  placeCard <faccia> <x> <y>: piazza la carta selezionata nella posizione x, y");
                printOnNewLine("  Le facce disponibili sono: FRONT, BACK");
                printOnNewLine("");
                printOnNewLine("  drawCard <mazzo> (opzionale <posizione a terra>): pesca una carta dal mazzo specificato. Se <posizione a terra> è specificata, pesca la carta dalla posizione specificata, altrimenti pesca dal mazzo");
                printOnNewLine("  I mazzi disponibili sono: GOLD, RESOURCE");
                printOnNewLine("  Le posizioni a terra disponibili sono: 1, 2");
                printOnNewLine("");
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
            case "chooseFace":
                if(args.length != 2){
                    printOnNewLine("Utilizzo corretto: chooseFace <faccia>");
                    return;
                }
                if(!args[1].toUpperCase().equals("FRONT") && !args[1].toUpperCase().equals("BACK")){
                    printOnNewLine("Faccia non valida. Le facce disponibili sono: FRONT, BACK");
                    return;
                }
                switch(controller.getGameState()){
                    case CHOOSE_STARTING_CARD:
                        Face chosenFace = Face.valueOf(args[1].toUpperCase());
                        controller.chooseStartingCardFace(chosenFace);
                        break;
                    default:
                        printOnNewLine("C'è un tempo e un luogo per ogni cosa! Ma non ora...");
                }
                break;
            case "chooseCard":
                if(args.length != 2){
                    printOnNewLine("Utilizzo corretto: chooseCard <numero>");
                    return;
                }
                int cardNumber = Integer.parseInt(args[1]) - 1;
                switch(controller.getGameState()){
                    case CHOOSE_SECRET_ACHIEVEMENT:
                        if(cardNumber < 0 || cardNumber > 1){
                            printOnNewLine("Numero non valido. Scegli 1 o 2");
                            return;
                        }
                        controller.chooseSecretAchievement(potentialSecretAchievements.get(cardNumber), cardNumber);
                        break;
                    case PLACE_CARD:
                        chosenHandCard = cardNumber;
                        printOnNewLine("Carta selezionata: " + controller.getHand().get(cardNumber));
                        break;
                    default:
                        printOnNewLine("C'è un tempo e un luogo per ogni cosa! Ma non ora...");
                }
                break;
            case "printHand":
                displayHand();
                break;
            case "placeCard":
                if(args.length != 4){
                    printOnNewLine("Utilizzo corretto: placeCard <faccia> <x> <y>");
                    return;
                }
                if(chosenHandCard == null){
                    printOnNewLine("Devi selezionare una carta della tua mano");
                    printPromptLine();
                    return;
                }
                if(!args[1].toUpperCase().equals("FRONT") && !args[1].toUpperCase().equals("BACK")){
                    printOnNewLine("Faccia non valida. Le facce disponibili sono: FRONT, BACK");
                    return;
                }
                int x = Integer.parseInt(args[2]);
                int y = Integer.parseInt(args[3]);
                Face face = Face.valueOf(args[1].toUpperCase());
                controller.askPlayCard(chosenHandCard, face, x, y);
                chosenHandCard = null;
                break;
            case "drawCard":
                if(args.length < 2 || args.length > 3){
                    printOnNewLine("Utilizzo corretto: drawCard <mazzo> (opzionale <posizione a terra>)");
                    return;
                }
                DeckPosition position = DeckPosition.DECK;
                if(args.length == 3){
                    if(args[2].equals("1")){
                        position = DeckPosition.FIRST_CARD;
                    } else if(args[2].equals("2")){
                        position = DeckPosition.SECOND_CARD;
                    } else {
                        printOnNewLine("Posizione non valida. Le posizioni a terra disponibili sono: 1, 2");
                        return;
                    }
                }
                if(!args[1].equalsIgnoreCase("GOLD") && !args[1].equalsIgnoreCase("RESOURCE")){
                    printOnNewLine("Mazzo non valido. I mazzi disponibili sono: GOLD, RESOURCE");
                    return;
                }
                Decks deck = Decks.valueOf(args[1].toUpperCase());
                controller.askDrawCard(deck, position);
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
    /**
     * Tells the user that the connection has been set successfully
     */
    public void successfulConnection(){
        printOnNewLine("Connessione avvenuta con successo al server");
        printPromptLine();
    }
    /**
     * Tells the user that the player has drawn a card
     */
    public void displayNewCardInHand(){
        Card newCard = controller.getHand().getLast();
        printOnNewLine("Hai pescato la carta: " + newCard);
        printPromptLine();
    }

    // Failed actions

    /**
     * Tells the user that the it is impossible set the player name
     */
    public void nameChangeFailed(){
        printOnNewLine("Impossibile impostare il nome. Assicurati di non avere già un nome e di aver inserito un nome disponibile");
        printPromptLine();
    }
    /**
     * Tells the user that the player name is needed
     * Called by the controller when the user tries to set a name without setting a name
     */
    public void needName(){
        printOnNewLine("Devi impostare il tuo nome prima di poter impostare il colore");
        printPromptLine();
    }
    /**
     * Tells the user that the player color is needed
     * Called by the controller when the user tries to set a color without having set a color
     */
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
    /**
     * Tells the user that the connection to the server has failed
     */
    public void connectionFailed(){
        printOnNewLine("Connessione al server fallita");
        printPromptLine();
    }
    /**
     * Tells the user that it is impossible draw a card for an achievement deck
     */
    public void cantDrawAchievementCards(){
        printOnNewLine("Non puoi pescare le carte obiettivo");
        printPromptLine();
    }
    /**
     * Tells the user that the action has been already done
     */
    public void alreadyDone(Actions action){
        printOnNewLine("Hai già eseguito questa azione: " + action);
        printPromptLine();
    }
    /**
     * Tells the user that the  card can't be placed
     */
    public void cardNotPlaceable(){
        printOnNewLine("Piazzamento carta fallito");
        printPromptLine();
    }
    /**
     * Tells the user that the message can not be empty
     */
    public void chatMessageIsEmpty(){
        printOnNewLine("Il messaggio non può essere vuoto");
        printPromptLine();
    }
    /**
     * Tells the user that the deck is empty
     */
    public void deckIsEmpty(){
        printOnNewLine("Il mazzo è vuoto");
        printPromptLine();
    }
    /**
     * Tells the user that the game is already started
     */
    public void gameAlreadyStarted(){
        printOnNewLine("La partita è già iniziata");
        printPromptLine();
    }
    /**
     * Tells the user that the game is not yet started
     */
    public void gameNotYetStarted(){
        printOnNewLine("La partita non è ancora iniziata");
        printPromptLine();
    }
    /**
     * Tells the user that the selected card is not in the hand or does not exist
     */
    public void invalidCardForAction(Actions cardType){
        printOnNewLine("La carta selezionata non esiste o non è nella tua mano");
        printPromptLine();
    }
    /**
     * Tells the user that the cards have not been given yet
     */
    public void notYetGivenCard(Actions cardType){
        printOnNewLine("Le carte " + cardType + " non sono ancora state date");
        printPromptLine();
    }
    /**
     * Tells the user that it is not his turn
     */
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
    /**
     * Tells the users which are the common achievements
     */
    public void displayCommonAchievements(){
        printOnNewLine("Gli obiettivi comuni sono: \n");
        for (int i = 0; i < controller.getCommonAchievements().size(); i++) {
            System.out.println("    " + i + ": " + controller.getCommonAchievements().get(i));
        }
        printPromptLine();
    }

    /**
     * Asks the user to choose the secret achievement
     * @param possibleAchievements list of possible secret achievements (lis of 2 elements)
     */
    public void chooseSecretAchievement(List<AchievementCard> possibleAchievements){
        printOnNewLine("Scegli un obiettivo segreto: \n");
        potentialSecretAchievements = new ArrayList<>();
        for (int i = 0; i < possibleAchievements.size(); i++) {
            potentialSecretAchievements.add((AchievementCard) possibleAchievements.get(i));
            System.out.println("    " + i + ": " + possibleAchievements.get(i));
        }
        printPromptLine();
        //todo: add selection
    }

    /**
     * Says to all players that the ending phase is starting
     */
    public void endGameStarted(){
        printOnNewLine("La partita sta per finire");
        printPromptLine();
    }

    /**
     * says to the player which card he has on his hand
     */
    public void displayHand(){
        printOnNewLine("La tua mano contiene le carte: \n");
        for (Card card : controller.getHand()) {
            System.out.println("    " + card);
        }
        printPromptLine();
    }

    /**
     * Shows the players and their final points
     */
    public void displayLeaderboard(){
        printOnNewLine("Classifica: \n");
        controller.getLeaderboard().forEach((player, points) -> {
            System.out.println("    " + player + ": " + points);
        });
        printPromptLine();
    }

    /**
     * Says to all players that a new player has joined the match
     */
    public void displayNewPlayer(){
        printOnNewLine("E' entrato un nuovo giocatore: " + controller.getPlayers().getLast().getName());
        printPromptLine();
    }

    /**
     * A player has drown a card from a deck
     * @param name the name of the player
     * @param deckFrom the deck from which the card has been drawn
     * @param position the position of the card in the deck
     */
    public void otherPlayerDraw(String name, Decks deckFrom, DeckPosition position){
        printOnNewLine(name + " ha pescato una carta dalla posizione " + position + " del mazzo " + deckFrom);
        printOnNewLine("Ora il mazzo ha " + controller.getDeckSize(deckFrom));
        printOnNewLine("Le carte a terra sono" + controller.getBoardCards(deckFrom));
        printPromptLine();
    }

    /**
     * Says which player is playing in the current turn
     */
    public void newTurn(){
        printOnNewLine("E' il turno " + controller.getTurn() + ". Tocca a " + controller.getActivePlayer());
        printPromptLine();
    }

    /**
     * Says to all other players that a player has received his starting hand
     * @param name the name of the player
     */
    public void otherPlayerInitialHand(String name){
        printOnNewLine(name + " ha ricevuto la sua mano iniziale");
        printPromptLine();
    }

    /**
     * Says to all other players that a player has chosen his secret achievement
     * @param name the name of the player
     */
    public void secretAchievementChosen(String name){
        printOnNewLine(name + " ha scelto il proprio obiettivo segreto");
        printPromptLine();
    }

    /**
     * Says to all other players that a player has chosen his starting card
     * @param name the name of the player
     */
    public void startingCardChosen(String name){
        printOnNewLine(name + " ha scelto la propria carta iniziale");
        printPromptLine();
    }

    /**
     * Says to all the player that the game has started
     */
    public void gameStarted(){
        printOnNewLine("La partita è iniziata!");
        printPromptLine();
    }

    /**
     * Says to all player their order
     */
    public void displayPlayerOrder(){
        printOnNewLine("L'ordine dei giocatori è:");
        for (int i = 0; i < controller.getPlayerNames().size(); i++) {
            System.out.print(" " + i + ": " + controller.getPlayerNames());
        }
        printPromptLine();
    }

    /**
     * Shows which card are on the board
     */
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

    /**
     * Asks the user to choose the starting card
     * @param card
     */
    public void chooseStartingCardFace(Card card){
        printOnNewLine("Scegli la faccia della carta iniziale: \n" + card);
        printPromptLine();
    }

    /**
     * Says to the user which action he has to do first
     * @param action the action to do first
     */
    public void doFirst(Actions action){
        printOnNewLine("Devi prima " + action);
        printPromptLine();
    }

    /**
     * Says to all the player which players are ready
     */
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

    /**
     * Displays the points of a player
     * @param playerName the name of the player to display the points of
     */
    public void displayPlayerPoints(String playerName) throws PlayerNotFoundByNameException {
        Player player = controller.getPlayerByName(playerName);
        printOnNewLine("Punti di " + playerName + ": " + player.getPoints());
        printPromptLine();
    }

    /**
     * Shows that a player has placed a card
     * @param playerName
     * @param x
     * @param y
     */
    public void cardPlaced(String playerName, int x, int y) {
        printOnNewLine(playerName + " ha piazzato una carta in posizione " + x + ", " + y);
        printPromptLine();
    }

    public void playerDisconnected(String playerName) {
        printOnNewLine(playerName + " si è disconnesso");
        printPromptLine();
    }
}
