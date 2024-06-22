package Client.View;

import Client.Controller.ClientController;
import Client.Player;
import Interface.MainBoardSceneController;
import Interface.SceneName;
import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.CardFace;
import Server.Card.CornerCardFace;
import Server.Chat.Message;
import Client.View.StartingCardChoiceState;
import Server.Enums.*;
import Server.Exception.PlayerNotFoundByNameException;

import java.util.*;
/**
 * This class is responsible for the user interface logic of the command line interface of the client side of the application.
 */
public class CLI extends Thread implements UI {
    private final ClientController controller;
    private final Scanner in = new Scanner(System.in);
    private CLIState scene;
    /**
     * The constructor for the class
     * @param controller the controller
     */
    public CLI(ClientController controller){
        System.out.println("Avvio gioco...");
        this.controller = controller;
    }

    /**
     * Changes the scene of the CLI
     */
    public void changeScene(CLIState scene) {
        this.scene = scene;
    }

    /**
     * Prints > on a new line
     */
    public void printPromptLine(){
        System.out.println();
        System.out.print("> ");
    }

    /**
     * Prints a message on a new line
     * @param message the message to print
     */
    public void printOnNewLine(String message){
        System.out.println();
        System.out.print(message);
    }

    /**
     * Runs CLI thread: Initializes the scene then reads input from the console and calls the decoding method
     */
    public void run(){
        changeScene(new NetworkState(this));
        while(true){
            String[] args = in.nextLine().split(" ");
            scene.decode(args);
        }
    }

    public void askConnectionMode(){
        printOnNewLine("Vuoi connetterti tramite RMI o SOCKET?");
        printPromptLine();
    }

    /**
     * Sets the controller's RMI mode to the specified value and goes to join scene
     * @param rmimode true to set RMI mode, false to set SOCKET mode
     */
    public void setRMIMode(boolean rmimode) {
        controller.setRMIMode(rmimode);
        changeScene(new JoinState(this));
    }

    /**
     * Joins the server with the specified ip and port
     * @param ip the server ip
     * @param port the server port
     */
    public void joinServer(String ip, int port) {
        controller.joinServer(ip, port);
    }

    // Successful actions

    public void nameChanged(){
        if(controller.isSavedGame()){
            printOnNewLine("Sei entrato in partita");
        } else {
            changeScene(new SetColorState(this, controller));
        }
    }
    public void colorChanged() {
        printOnNewLine("Il tuo colore è stato cambiato con successo. Ora sei " + controller.getMyColor());
        printOnNewLine("Quando sei pronto, inserisci 'pronto'");
        printPromptLine();
    }
    public void successfulConnection(){
        printOnNewLine("Connessione avvenuta con successo al server");
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
    public void needName(){
        printOnNewLine("Devi impostare il tuo nome prima di poter impostare il colore");
        printPromptLine();
    }
    public void needColor(){
        printOnNewLine("Devi prima impostare il tuo colore");
        printPromptLine();
    }
    public void needNameOrColor() {
        printOnNewLine("Devi impostare il tuo nome e il tuo colore prima di poter iniziare la partita");
        printPromptLine();
    }
    public void unavailableColor() {
        printOnNewLine("Il colore richiesto non è disponibile. I colori disponibili sono: ");
        for (Color color : controller.getAvailableColors()) {
            System.out.println(color + " ");
        }
        printPromptLine();
    }
    public void invalidColor() {
        printOnNewLine("Il colore richiesto non è valido. I colori validi sono: RED, YELLOW, BLUE, GREEN");
        printPromptLine();
    }
    public void colorChangeFailed() {
        printOnNewLine("Impossibile cambiare il colore. Assicurati di aver inserito un colore disponibile");
        printPromptLine();
    }
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
    public void playerListChanged() {
        printOnNewLine("E' entrato un nuovo giocatore! I giocatori ora sono: ");
        for (Player player : controller.getPlayers()) {
            System.out.print(player.getName() + " ");
        }
        printPromptLine();
    }
    public void displayPlayerColors(){
        printOnNewLine("Ora i giocatori hanno questi colori: \n");
        for (Player player : controller.getPlayers()) {
            System.out.println("    " + player.getName() + ": " + (player.getColor() != null ? player.getColor().toString() : "nessun colore"));
        }
        printPromptLine();
    }
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
    public void chat(Message message){
        printOnNewLine(message.getName() + ": " + message.getMessage());
        printPromptLine();
    }
    public void displayCommonAchievements(){
        printOnNewLine("Gli obiettivi comuni sono: \n");
        for (int i = 0; i < controller.getCommonAchievements().size(); i++) {
            System.out.println("    - " + controller.getCommonAchievements().get(i));
        }
        printPromptLine();
    }
    public void chooseSecretAchievement(List<AchievementCard> possibleAchievements){
        printOnNewLine("Scegli un obiettivo segreto: \n");
        for (int i = 0; i < possibleAchievements.size(); i++) {
            System.out.println("    " + (i + 1) + ": " + possibleAchievements.get(i));
        }
        changeScene(new SecretCardChoiceState(this, controller));
    }
    public void endGameStarted(){
        printOnNewLine("La partita sta per finire");
        printPromptLine();
    }
    public void displayHand(){
        printOnNewLine("La tua mano contiene le carte: \n");
        for (Card card : controller.getHand()) {
            System.out.println("    " + card);
        }
        printPromptLine();
    }
    public void displayLeaderboard(LinkedHashMap<String, Integer> playerPoints){
        printOnNewLine("Classifica: \n");
        int i = 1;
        for(String player: playerPoints.keySet()){
            System.out.println("    Il giocatore n." + i++ + "è " + player + ": " + playerPoints.get(player) + " punti");
        }
        printPromptLine();
    }
    public void displayNewPlayer(){
        String newPlayerName = controller.getPlayers().getLast().getName();
        if(!Objects.equals(newPlayerName, controller.getMyName())){
            printOnNewLine("E' entrato un nuovo giocatore: " + newPlayerName);
            printPromptLine();
        }
    }
    public void otherPlayerDraw(String name, Decks deckFrom, DeckPosition position){
        printOnNewLine(name + " ha pescato una carta dalla posizione " + position + " del mazzo " + deckFrom);
        printOnNewLine("Ora il mazzo ha " + controller.getDeckSize(deckFrom));
        printOnNewLine("Le carte a terra sono" + controller.getBoardCards(deckFrom));
        printPromptLine();
    }
    public void newTurn(){
        printOnNewLine("E' il turno " + controller.getTurn() + ". Tocca a " + controller.getActivePlayer().getName());
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
        printOnNewLine("Tutti i giocatori sono pronti! Inizia la partita...");
    }
    public void displayPlayerOrder(){
        printOnNewLine("L'ordine dei giocatori è:");
        for (int i = 0; i < controller.getPlayerNames().size(); i++) {
            System.out.print(" " + (i + 1) + ": " + controller.getPlayerNames().get(i));
        }
        changeScene(new GameState(this, controller));
    }
    public void displayBoardCards(){
        printOnNewLine("MAZZO ORO:");
        printOnNewLine("La carta in cima al mazzo è: \n" + controller.getBoardCards(Decks.GOLD).getFirst().getFace(Face.BACK));
        printOnNewLine("Le carte a terra sono: ");
        printOnNewLine(controller.getBoardCards(Decks.GOLD).get(1).toString());
        printOnNewLine(controller.getBoardCards(Decks.GOLD).get(2).toString());
        printOnNewLine("MAZZO RISORSA:");
        printOnNewLine("La carta in cima al mazzo è: \n" + controller.getBoardCards(Decks.RESOURCE).getFirst().getFace(Face.BACK));
        printOnNewLine("Le carte a terra sono: ");
        printOnNewLine(controller.getBoardCards(Decks.RESOURCE).get(1).toString());
        printOnNewLine(controller.getBoardCards(Decks.RESOURCE).get(2).toString());
        printPromptLine();
    }
    public void chooseStartingCardFace(Card card){
        printOnNewLine("La tua carta iniziale è: ");
        printOnNewLine(card.toString());
        changeScene(new StartingCardChoiceState(this, controller));
    }
    public void doFirst(Actions action){
        printOnNewLine("Devi prima " + action);
        printPromptLine();
    }
    public void displayLobby(){
        changeScene(new SetNameState(this, controller));
    }
    public void displayPlayerPoints(String playerName) throws PlayerNotFoundByNameException {
        Player player = controller.getPlayerByName(playerName);
        printOnNewLine("Punti di " + playerName + ": " + player.getPoints());
        printPromptLine();
    }
    public void cardPlaced(String playerName, CornerCardFace cornerCardFace, int x, int y) {
        printOnNewLine(playerName + " ha piazzato una carta in posizione " + x + ", " + y);
        printPromptLine();
    }

    public void playerDisconnected(String playerName) {
        printOnNewLine(playerName + " si è disconnesso");
        printPromptLine();
    }

    public void tooManyPlayers() {
        printOnNewLine("E' stato già raggiunto il limite massimo di giocatori, sei uno spettatore");
        printPromptLine();
    }

    public void playerRemoved(String playerName) {
        printOnNewLine(playerName + " è stato rimosso dalla partita per inattività");
        printPromptLine();
    }

    public void otherPlayerReconnected(String name){
        printOnNewLine(name + " si è riconnesso");
        printPromptLine();
    }

    public void displayId(){
        printOnNewLine("Il tuo id è: " + controller.getMyId() + ". Salvalo per riconnetterti");
    }

    public void idNotInGame(){
        printOnNewLine("L'id specificato non corrisponde a nessun giocatore");
        printPromptLine();
    }

    public void playerAlreadyPlaying(){
        printOnNewLine("Il giocatore specificato è già in partita");
        printPromptLine();
    }

    /**
     * Displays the size of the decks
     */
    public void displayDeckSizes(){
        printOnNewLine("Il mazzo oro ha " + controller.getDeckSize(Decks.GOLD) + " carte");
        printOnNewLine("Il mazzo risorsa ha " + controller.getDeckSize(Decks.RESOURCE) + " carte");
        printPromptLine();
    }

    /**
     * Displays the player's secret achievement
     */
    public void displaySecretAchievement(){
        printOnNewLine("Il tuo obiettivo segreto è: " + controller.getSecretAchievement());
        printPromptLine();
    }

    /**
     * Displays the turn number
     */
    public void displayTurn(){
        printOnNewLine("E' il turno " + controller.getTurn() + ". Tocca a " + controller.getActivePlayer().getName());
        printPromptLine();
    }

    /**
     * Displays the manuscript of a player
     * @param playerName the name of the player
     */
    public void displayManuscript(String playerName){
        try{
            printOnNewLine("Manoscritto di " + playerName + ":");
            int minX, minY, maxX, maxY;
            List<CornerCardFace> manuscriptCards = controller.getPlayerByName(playerName).getManuscript().getAllCards();
            minX = manuscriptCards.stream().mapToInt(CornerCardFace::getXCoord).min().orElse(0);
            minY = manuscriptCards.stream().mapToInt(CornerCardFace::getYCoord).min().orElse(0);
            maxX = manuscriptCards.stream().mapToInt(CornerCardFace::getXCoord).max().orElse(0);
            maxY = manuscriptCards.stream().mapToInt(CornerCardFace::getYCoord).max().orElse(0);
            //first rows: x axis
            System.out.println();
            printOnNewLine("   ");
            for(int x = minX; x <= maxX; x++){
                System.out.print(" " + x + " ");
            }
            //other rows
            for(int y = minY; y <= maxY; y++){
                printOnNewLine("   ");
                for(int x = minX; x <= maxX; x++){
                    CardFace cardToDisplay = controller.getPlayerByName(playerName).getManuscript().getCardByCoord(x, y);
                    if(cardToDisplay != null){
                        System.out.print(cardToDisplay.getCornerSymbols().get(CardCorners.TOP_LEFT).toShortString());
                        System.out.print((x == 0 && y == 0)? " ": cardToDisplay.getKingdom().toShortString());
                        System.out.print(cardToDisplay.getCornerSymbols().get(CardCorners.TOP_RIGHT).toShortString());
                    } else {
                        System.out.print("   ");
                    }
                }
                printOnNewLine(" " + y + " ");
                for(int x = minX; x <= maxX; x++){
                    CardFace cardToDisplay = controller.getPlayerByName(playerName).getManuscript().getCardByCoord(x, y);
                    if(cardToDisplay != null){
                        System.out.print((x == 0 && y == 0)? " ": cardToDisplay.getKingdom().toShortString());
                        System.out.print("X");
                        System.out.print((x == 0 && y == 0)? " ": cardToDisplay.getKingdom().toShortString());
                    } else {
                        System.out.print("   ");
                    }
                }
                printOnNewLine("   ");
                for(int x = minX; x <= maxX; x++){
                    CardFace cardToDisplay = controller.getPlayerByName(playerName).getManuscript().getCardByCoord(x, y);
                    if(cardToDisplay != null){
                        System.out.print(cardToDisplay.getCornerSymbols().get(CardCorners.BOTTOM_LEFT).toShortString());
                        System.out.print((x == 0 && y == 0)? " ": cardToDisplay.getKingdom().toShortString());
                        System.out.print(cardToDisplay.getCornerSymbols().get(CardCorners.BOTTOM_RIGHT).toShortString());
                    } else {
                        System.out.print("   ");
                    }
                }
            }
        } catch (PlayerNotFoundByNameException e){
            printOnNewLine("Il giocatore specificato non esiste");
        }
        printPromptLine();
    }
    /**
     * Displays a card in the manuscript of a player
     * @param playerName the name of the player
     * @param x the x coordinate of the card
     * @param y the y coordinate of the card
     */
    public void displayCard(String playerName, int x, int y){
        try{
            CardFace cardToDisplay = controller.getPlayerByName(playerName).getManuscript().getCardByCoord(x, y);
            if(cardToDisplay != null){
                printOnNewLine("La carta in posizione " + x + ", " + y + " è: ");
                printOnNewLine(cardToDisplay.toString());
                printOnNewLine("Questa carta è stata piazzata al turno " + cardToDisplay.getPlacementTurn());
            } else {
                printOnNewLine("Non c'è nessuna carta in posizione " + x + ", " + y);
            }
        } catch (PlayerNotFoundByNameException e){
            printOnNewLine("Il giocatore specificato non esiste");
        }
        printPromptLine();
    }

    /**
     * Displays the players' info
     */
    public void displayPlayerInfo(){
        displayPlayerOrder();
        displayPlayerColors();
        for (Player player : controller.getPlayers()) {
            System.out.println("    " + player.getName() + ": " + player.getPoints() + " punti");
            displayManuscript(player.getName());
        }
    }

    /**
     * Displays the full chat
     */
    public void displayChat(){
        for (Message message : controller.getChat()) {
            System.out.println(message.getName() + ": " + message.getMessage());
        }
    }

    /**
     * Displays the current game state
     */
    public void displayGameState(){
        printOnNewLine("Stato del gioco: " + controller.getGameState());
        printPromptLine();
    }

    public void displayGameInfo(){
        displayId();
        displayCommonAchievements();
        displayDeckSizes();
        displayBoardCards();
        displaySecretAchievement();
        displayHand();
        displayTurn();
        displayPlayerInfo();
        displayChat();
        displayGameState();
        switch (controller.getGameState()) {
            case CHOOSE_STARTING_CARD:
                changeScene(new StartingCardChoiceState(this, controller));
                break;
            case CHOOSE_SECRET_ACHIEVEMENT:
                if (controller.getSecretAchievement() == null)
                    chooseSecretAchievement(controller.getPotentialSecretAchievements());
                changeScene(new SecretCardChoiceState(this, controller));
                break;
            default:
                changeScene(new GameState(this, controller));
        }
    }

    public void gameAlreadyFinished() {
        printOnNewLine("La partita è già finita");
        printPromptLine();
    }

    @Override
    public void serverDisconnected() {
        changeScene(new JoinState(this));
    }
}
