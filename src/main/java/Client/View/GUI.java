package Client.View;

import Client.Controller.ClientController;
import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.CornerCardFace;
import Server.Chat.Message;
import Server.Enums.Actions;
import Server.Enums.Color;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Enums.GameState;
import Server.Exception.PlayerNotFoundByNameException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Interface.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for the graphical user interface logic of the client side of the application.
 */
public class GUI implements UI{
    private static ClientController controller;
    private static final Map<SceneName,Scene> sceneMap = new HashMap<>();
    private Stage stage;
    private final Map<SceneName,SceneController> sceneControllerMap = new HashMap<>();

    /**
     * Constructor
     * @param controller the controller
     */
    public GUI(ClientController controller) {
        GUI.controller = controller;
    }

    /**
     * This method generates a scene from a fxml file, loads the scene controller , and adds the scene to the sceneMap and sceneControllerMap.
     * @param fxmlPath the path of the fxml file
     * @param sceneName the name of the scene to generate
     * @param stage the stage where the scene will be displayed
     * @throws IOException if the fxml file is not found
     */
    public void generateScene(String fxmlPath, SceneName sceneName, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());
        SceneController sceneController = fxmlLoader.getController();
        sceneController.setAll(controller, stage, sceneMap, sceneControllerMap);
        sceneMap.put(sceneName, scene);
        sceneControllerMap.put(sceneName, sceneController);
    }

    /**
     * This method generates all the scenes from a fxml file, loads the scene controller , and adds the scenes to the sceneMap and sceneControllerMap.
     * @param stage the stage where the scene will be displayed
     * @throws IOException if the fxml file is not found
     */
    public void generateScenes(Stage stage) throws IOException {
        generateScene("/rmi_or_socket.fxml", SceneName.NETWORK, stage);
        generateScene("/graficap1.fxml", SceneName.JOIN, stage);
        generateScene("/setName.fxml", SceneName.SETNAME, stage);
        generateScene("/setColor.fxml", SceneName.SETCOLOR, stage);
        generateScene("/chooseStartingCard.fxml", SceneName.STARTINGCARDCHOICE, stage);
        generateScene("/chooseSecretCard.fxml", SceneName.SECRETCARDCHOICE, stage);
        generateScene("/mainboard.fxml", SceneName.GAME, stage);
        generateScene("/waiting.fxml", SceneName.WAITING, stage);
    }

    /**
     * Gets a scene by its name
     * @param sceneName the name of the scene
     * @return the scene
     */
    public Scene getScene(SceneName sceneName) {
        return sceneMap.get(sceneName);
    }

    /**
     * Sets the stage
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void askConnectionMode() {
        stage.setScene(getScene(SceneName.NETWORK));
        stage.show();
    }

    @Override
    public void nameChanged() {
        Platform.runLater(() -> {
            if (controller.isSavedGame()) {
                stage.setScene(getScene(SceneName.WAITING));
            } else {
                sceneControllerMap.get(SceneName.SETCOLOR).messageToSend.setPromptText("Scrivi un messaggio!");
                stage.setScene(getScene(SceneName.SETCOLOR));
                stage.show();
            }
        });
    }

    @Override
    public void colorChanged() {
        Platform.runLater(() -> {
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setDisable(true);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).green_Button.setDisable(true);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).blue_Button.setDisable(true);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).yellow_Button.setDisable(true);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).red_Button.setDisable(true);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).label_color.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).green_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).blue_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).yellow_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).red_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).readyButton.setOpacity(1);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).readyButton.setDisable(false);
            listViewSetUpdate();
        });
    }
    @Override
    public void successfulConnection() {
        Platform.runLater(() -> {
            for(int i = 0; i < controller.getPlayers().size(); i++) {
                String name = controller.getPlayers().get(i).getName();
                String color = controller.getPlayers().get(i).getColor() == null ? "No Color" : controller.getPlayers().get(i).getColor().toString();
                String ready = controller.getPlayers().get(i).isReady() ? "Ready" : "Not Ready";
                try {
                    ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player.getItems().set(i, name + "   " + color + "   " + ready);
                } catch (IndexOutOfBoundsException e) {
                    ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player.getItems().add(name + "   " + color + "   " + ready);
                }
            }
            stage.setScene(getScene(SceneName.SETNAME));
            stage.show();
        });
    }

    @Override
    public void displayNewCardInHand() {
        Platform.runLater(() -> ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setHandCards());
    }

    @Override
    public void nameChangeFailed() {
        Platform.runLater(() -> {
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).possible_Name.clear();
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).possible_Name.setPromptText("Nome non valido");
            stage.setScene(getScene(SceneName.SETNAME));
            stage.show();
        });
    }

    @Override
    public void needName() {
        System.out.println("Need name");
    }

    @Override
    public void needColor() {
        System.out.println("Need color");
    }

    @Override
    public void needNameOrColor() {
        System.out.println("Need name or color");
        //maybe useless here
    }

    @Override
    public void unavailableColor() {
        System.out.println("Unavailable color");
        //maybe useless here
    }

    @Override
    public void colorChangeFailed() {
        System.out.println("Color change failed");
        //maybe useless here
    }

    @Override
    public void invalidColor() {
        System.out.println("Invalid color");
        //maybe useless here
    }

    @Override
    public void connectionFailed() {
        Platform.runLater(() -> {
            ((JoinSceneController) sceneControllerMap.get(SceneName.JOIN)).text_ip.clear();
            ((JoinSceneController) sceneControllerMap.get(SceneName.JOIN)).text_Port.clear();
            ((JoinSceneController) sceneControllerMap.get(SceneName.JOIN)).text_ip.setPromptText("Indirizzo IP non valido");
            ((JoinSceneController) sceneControllerMap.get(SceneName.JOIN)).text_Port.setPromptText("Porta non valida");
            stage.setScene(getScene(SceneName.JOIN));
            stage.show();
        });
    }

    @Override
    public void cantDrawAchievementCards() {
        System.out.println("Can't draw achievement cards");
    }

    @Override
    public void alreadyDone(Actions action) {
        System.out.println("Already done");
    }

    @Override
    public void cardNotPlaceable() {
        System.out.println("Card not placeable");
    }

    @Override
    public void chatMessageIsEmpty() {
        Platform.runLater(() -> {
            if(sceneMap.get(SceneName.SETCOLOR) == stage.getScene()){
                sceneControllerMap.get(SceneName.SETCOLOR).messageToSend.clear();
                sceneControllerMap.get(SceneName.SETCOLOR).messageToSend.setPromptText("Cannot send empty message");

            }
            if(sceneMap.get(SceneName.SECRETCARDCHOICE) == stage.getScene()){
                sceneControllerMap.get(SceneName.SECRETCARDCHOICE).messageToSend.clear();
                sceneControllerMap.get(SceneName.SECRETCARDCHOICE).messageToSend.setPromptText("Cannot send empty message");
            }
            if(sceneMap.get(SceneName.STARTINGCARDCHOICE) == stage.getScene()){
                sceneControllerMap.get(SceneName.STARTINGCARDCHOICE).messageToSend.clear();
                sceneControllerMap.get(SceneName.STARTINGCARDCHOICE).messageToSend.setPromptText("Cannot send empty message");
            }
            if(sceneMap.get(SceneName.GAME) == stage.getScene()){
                sceneControllerMap.get(SceneName.GAME).messageToSend.clear();
                sceneControllerMap.get(SceneName.GAME).messageToSend.setPromptText("Cannot send empty message");
            }
        });

        System.out.println("Chat message is empty");
    }

    @Override
    public void deckIsEmpty() {
        System.out.println("Deck is empty");
    }

    @Override
    public void gameAlreadyStarted() {
        System.out.println("Game already started");
    }

    @Override
    public void gameNotYetStarted() {
        System.out.println("Game not yet started");
    }

    @Override
    public void invalidCardForAction(Actions cardType) {
        System.out.println("Invalid card for action");
    }

    @Override
    public void notYetGivenCard(Actions cardType) {
        System.out.println("Not yet given card");
    }

    @Override
    public void notYourTurn() {
        System.out.println("Not your turn");
    }

    @Override
    public void displayPlayerColors() {
        Platform.runLater(this::listViewSetUpdate);
    }

    @Override
    public void updateReady(String name, boolean ready) {
        Platform.runLater(() ->
        {
            int i;
            for(i = 0; i < controller.getPlayers().size(); i++) {
                String name1 = controller.getPlayers().get(i).getName();
                String color = controller.getPlayers().get(i).getColor() == null ? "No Color" : controller.getPlayers().get(i).getColor().toString();
                String ready1 = controller.getPlayers().get(i).isReady() ? "Ready" : "Not Ready";
                ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().set(i, name1 + "   " + color + "   " + ready1);
                //------------------------------------------------------------------
                ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).cloneListView(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player);
            }
        });
    }

    @Override
    public void chat(Message message) {
        Platform.runLater(()->
        {
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).chat_messages.getItems().add( message.name() + ": "+  message.message());
            sceneControllerMap.get(SceneName.SETCOLOR).messageToSend.clear();
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).chat_message.getItems().add( message.name() + ": "+  message.message());
            ((ChooseSecretCardController) sceneControllerMap.get(SceneName.SECRETCARDCHOICE)).chat_message.getItems().add( message.name() + ": "+  message.message());
            sceneControllerMap.get(SceneName.SECRETCARDCHOICE).messageToSend.clear();
            ((ChooseStartingCardController) sceneControllerMap.get(SceneName.STARTINGCARDCHOICE)).chat_message.getItems().add( message.name() + ": "+  message.message());
            sceneControllerMap.get(SceneName.STARTINGCARDCHOICE).messageToSend.clear();
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).chatMessages.getItems().add( message.name() + ": "+  message.message());
            sceneControllerMap.get(SceneName.GAME).messageToSend.clear();
        });
    }

    @Override
    public void chooseSecretAchievement(List<AchievementCard> possibleAchievements) {
        Platform.runLater( () -> {
            ((ChooseSecretCardController) sceneControllerMap.get(SceneName.SECRETCARDCHOICE)).setUp(possibleAchievements);
            stage.setScene(getScene(SceneName.SECRETCARDCHOICE));
        });


    }

    @Override
    public void endGameStarted() {
        Platform.runLater(() -> ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).endingLabel.setOpacity(1));
        System.out.println("End game started");
    }

    @Override
    public void displayHand() {
        Platform.runLater(() -> ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setHandCards());

    }

    @Override
    public void displayLeaderboard(LinkedHashMap<String, Integer> playerPoints) {
        Platform.runLater(() -> {

            if (!stage.getScene().equals(getScene(SceneName.GAME))){
                sceneControllerMap.get(SceneName.GAME).setup();
                stage.setScene(getScene(SceneName.GAME));
            }
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).enableBackToLobbyButton();
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateLeaderBoard();
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).tabPane.getTabs().forEach(tab -> {
                if (!tab.getText().equals("LeaderBoard")) {
                    tab.setStyle("-fx-opacity: 0.5;");
                }
            });

        });
        System.out.println("Leaderboard displayed");
    }

    @Override
    public void displayNewPlayer() {
        Platform.runLater(() -> {
            String name = controller.getPlayers().getLast().getName();
            String color = controller.getPlayers().getLast().getColor() == null ? "No Color" : controller.getPlayers().getLast().getColor().toString();
            String ready = controller.getPlayers().getLast().isReady() ? "Ready" : "Not Ready";
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().add(name + "   " + color + "   " + ready);
            //----------------------------------------------------------------------q
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).cloneListView(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player);

        });

    }

    @Override
    public void otherPlayerDraw(String name, Decks deckFrom, DeckPosition position) {
        Platform.runLater(() -> {
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setBoardCards(deckFrom);
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateDeckSizes(controller.getDeckSize(Decks.GOLD), controller.getDeckSize(Decks.RESOURCE));
        });
    }

    @Override
    public void newTurn() {
        Platform.runLater(() -> ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).yourTurnLabel.setVisible(controller.getActivePlayer().getName().equals(controller.getMyName())));

    }

    @Override
    public void otherPlayerInitialHand(String name) {
        System.out.println("Other player initial hand");
        Platform.runLater(() -> ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateDeckSizes(controller.getDeckSize(Decks.GOLD), controller.getDeckSize(Decks.RESOURCE)));
    }

    @Override
    public void secretAchievementChosen(String name) {
        Platform.runLater(() -> {
            if (name.equals(controller.getMyName())){
                ((ChooseSecretCardController) sceneControllerMap.get(SceneName.SECRETCARDCHOICE)).confirmation();
                ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setSecretCard(controller.getSecretAchievement());
                sceneControllerMap.get(SceneName.GAME).setup();
                stage.setScene(getScene(SceneName.GAME));
            }

        });
    }

    @Override
    public void startingCardChosen(String name) {
        Platform.runLater(() -> {
            try {
                if (controller.getMyName().equals(name)) {

                    ((ChooseStartingCardController) sceneControllerMap.get(SceneName.STARTINGCARDCHOICE)).confirmation();
                }
                ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateManuscript(name, controller.getPlayerByName(name).getManuscript().getCardByCoord(0,0),0 ,0);
            } catch (PlayerNotFoundByNameException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void gameStarted() {
        System.out.println("Game started");
    }

    @Override
    public void displayPlayerOrder() {
        System.out.println("Player order displayed");
    }

    @Override
    public void displayBoardCards() {
        Platform.runLater(() -> ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateAllFloorCards());
    }

    @Override
    public void chooseStartingCardFace(Card card) {
        Platform.runLater(() -> {
            ((ChooseStartingCardController) sceneControllerMap.get(SceneName.STARTINGCARDCHOICE)).setUp(card);
            sceneControllerMap.get(SceneName.GAME).setup();
            stage.setScene(getScene(SceneName.STARTINGCARDCHOICE));
        });


    }

    @Override
    public void doFirst(Actions action) {
        System.out.println("Do first");
    }

    @Override
    public void displayLobby() {
        Platform.runLater(() -> {
            // see better
            for (int i = 0; i < controller.getPlayers().size(); i++) {
                String name = controller.getPlayers().get(i).getName();
                String color = controller.getPlayers().get(i).getColor() == null ? "No Color" : controller.getPlayers().get(i).getColor().toString();
                String ready = controller.getPlayers().get(i).isReady() ? "Ready" : "Not Ready";
                ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().add(name + "   " + color + "   " + ready);
                //-------------------------------------------------------------q
                ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).cloneListView(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player);

                switch (controller.getPlayers().getLast().getColor()) {
                    case RED:
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).red_Button.setDisable(true);
                        break;
                    case GREEN:
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).green_Button.setDisable(true);
                        break;
                    case BLUE:
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).blue_Button.setDisable(true);
                        break;
                    case YELLOW:
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).yellow_Button.setDisable(true);
                        break;
                    case null:
                        break;

                }
            }
        });

    }

    @Override
    public void displayPlayerPoints(String playerName) {
        Platform.runLater(() -> {
            System.out.println("Player points displayed");
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateLeaderBoard();
        });

    }

    @Override
    public void cardPlaced(String playerName, CornerCardFace cornerCardFace, int x, int y) {
        System.out.println("Card placed in x: " + x + " y: " + y + " by " + playerName);
        Platform.runLater(() -> {
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateManuscript(playerName, cornerCardFace, x, y);
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).removeCardFromHand(playerName);
        });
    }

    @Override
    public void playerDisconnected(String playerName) {
        Platform.runLater(() -> {
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).chat_messages.getItems().add( playerName + " left the match ");
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).chat_message.getItems().add( playerName + " left the match ");
            ((ChooseSecretCardController) sceneControllerMap.get(SceneName.SECRETCARDCHOICE)).chat_message.getItems().add( playerName + " left the match ");
            ((ChooseStartingCardController) sceneControllerMap.get(SceneName.STARTINGCARDCHOICE)).chat_message.getItems().add( playerName + " left the match ");
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).chatMessages.getItems().add( playerName + " left the match ");
        });
        System.out.println("Player disconnected");
    }

    @Override
    public void displayCommonAchievements() {
        Platform.runLater( () -> {
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setCardOnFloor(controller.getCommonAchievements().get(0), Decks.ACHIEVEMENT, DeckPosition.FIRST_CARD);
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setCardOnFloor(controller.getCommonAchievements().get(1), Decks.ACHIEVEMENT, DeckPosition.SECOND_CARD);
        });
    }

    @Override
    public void tooManyPlayers() {
        System.out.println("Too many players");
    }

    @Override
    public void displayId() {
        Platform.runLater(() -> sceneControllerMap.get(SceneName.SETNAME).setup());

    }

    @Override
    public void playerRemoved(String name) {
        Platform.runLater(()->{
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().stream().filter(s -> s.contains(name)).findFirst().ifPresent(s -> ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().remove(s));
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player.getItems().stream().filter(s -> s.contains(name)).findFirst().ifPresent(s -> ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player.getItems().remove(s));
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).cloneListView(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player);
        });
        System.out.println("Player removed");
    }

    @Override
    public void otherPlayerReconnected(String name) {
        Platform.runLater(()-> {
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).chat_messages.getItems().add(name + " reconnected");
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).chat_message.getItems().add(name + " reconnected");
            ((ChooseSecretCardController) sceneControllerMap.get(SceneName.SECRETCARDCHOICE)).chat_message.getItems().add(name + " reconnected");
            ((ChooseStartingCardController) sceneControllerMap.get(SceneName.STARTINGCARDCHOICE)).chat_message.getItems().add(name + " reconnected");
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).chatMessages.getItems().add(name + " reconnected");
        });
        System.out.println("Other player reconnected");
    }

    @Override
    public void idNotInGame() {
        System.out.println("ID not in game");
    }

    @Override
    public void playerAlreadyPlaying() {
        System.out.println("Player already playing");
    }

    @Override
    public void displayGameInfo() {
        Platform.runLater(() -> {
            System.out.println("Game info displayed");
            switch (controller.getGameState()) {
                case LOBBY:
                    throw new RuntimeException("Unexpected game state");
                case CHOOSE_STARTING_CARD:
                    System.out.println("Choose starting card");
                    sceneControllerMap.get(SceneName.GAME).setup();
                    stage.setScene(getScene(SceneName.STARTINGCARDCHOICE));
                    stage.show();
                    break;
                case CHOOSE_SECRET_ACHIEVEMENT:
                    sceneControllerMap.get(SceneName.GAME).setup();
                    chooseSecretAchievement(controller.getPotentialSecretAchievements());
                    break;
                default:
                    displayHand();
                    displayBoardCards();
                    displayCommonAchievements();
                    sceneControllerMap.get(SceneName.GAME).setup();
                    regenerateManuscript();
                    Platform.runLater(() -> ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateDeckSizes(controller.getDeckSize(Decks.GOLD), controller.getDeckSize(Decks.RESOURCE)));
                    ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).yourTurnLabel.setVisible(controller.getActivePlayer().getName().equals(controller.getMyName()));

                    secretAchievementChosen(controller.getMyName());
                    sceneControllerMap.get(SceneName.GAME).setup();

                    stage.setScene(getScene(SceneName.GAME));
                    stage.show();
                    break;
        }
        });
    }


    // Only GUI methods

    /**
     * Displays an update on player colors
     */
    private void listViewSetUpdate() {
        for(int i = 0; i < controller.getPlayers().size(); i++) {
            String name = controller.getPlayers().get(i).getName();
            String color = controller.getPlayers().get(i).getColor() == null ? "No Color" : controller.getPlayers().get(i).getColor().toString();
            String ready = controller.getPlayers().get(i).isReady() ? "Ready" : "Not Ready";
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().set(i, name + "   " + color + "   " + ready);
            //----------------------------------------------------------q
            switch (controller.getPlayers().get(i).getColor()) {
                case RED:
                    ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).red_Button.setDisable(true);
                    ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).red_Button.setStyle("-fx-background-color: #eae1b8;"+ "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
                    if(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).color == Color.RED)
                    {
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setOpacity(0);
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setDisable(true);
                    }
                    break;
                case GREEN:
                    ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).green_Button.setDisable(true);
                    ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).green_Button.setStyle("-fx-background-color: #eae1b8;"+ "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
                    if(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).color == Color.GREEN)
                    {
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setOpacity(0);
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setDisable(true);
                    }
                    break;
                case BLUE:
                    ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).blue_Button.setDisable(true);
                    ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).blue_Button.setStyle("-fx-background-color: #eae1b8;"+ "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
                    if(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).color == Color.BLUE)
                    {
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setOpacity(0);
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setDisable(true);
                    }
                    break;
                case YELLOW:
                    ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).yellow_Button.setDisable(true);
                    ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).yellow_Button.setStyle("-fx-background-color: #eae1b8;"+ "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
                    if(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).color == Color.YELLOW)
                    {
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setOpacity(0);
                        ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setDisable(true);
                    }
                    break;
                case null:
                    break;
            }
        }
        ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).cloneListView(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player);
    }
    public void gameAlreadyFinished() {
        System.out.println("Game already finished");
    }

    @Override
    public void clear() {
        Platform.runLater(() -> {
            try {
                generateScenes(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(getScene(SceneName.NETWORK));
            stage.show();
        });
    }

    @Override
    public void serverDisconnected() {
        if(controller.getGameState() != GameState.LEADERBOARD){
            clear();
        }
    }


    /**
     * Generate a manuscript based on its data
     */
    private void regenerateManuscript()
    {
        for(String playerName: controller.getPlayerNames()) {
            try {
                for (CornerCardFace c : controller.getPlayerByName(playerName).getManuscript().getAllCards()) {
                    ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateManuscript(playerName, c, c.getXCoord(), c.getYCoord());
                }
            } catch (PlayerNotFoundByNameException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
