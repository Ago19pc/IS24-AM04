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
import Server.Exception.PlayerNotFoundByNameException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import run.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GUI implements UI{
    private static ClientController controller;
    private static Map<SceneName,Scene> sceneMap = new HashMap<>();
    private Stage stage;
    private Map<SceneName,SceneController> sceneControllerMap = new HashMap<>();


    public GUI(ClientController controller) {
        this.controller = controller;
    }


    public void generateScene(String fxmlPath, SceneName sceneName, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());
        SceneController sceneController = (SceneController) fxmlLoader.getController();
        sceneController.setAll(controller, stage, sceneMap, sceneControllerMap);
        sceneMap.put(sceneName, scene);
        sceneControllerMap.put(sceneName, sceneController);
    }

    public Scene getScene(SceneName sceneName) {
        return sceneMap.get(sceneName);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void askConnectionMode() {
        stage.setScene(getScene(SceneName.NETWORK));
        stage.show();
    }

    @Override
    public void nameChanged(String name) {
        Platform.runLater(() -> {
            if (controller.isSavedGame()) {
                //todo: implement graphically with another scene
                System.out.println("WAITING FOR OTHERS, TODO");
            } else {
                ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).messageToSend.setPromptText("Scrivi un messaggio!");
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
                ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player.getItems().set(i, name + "   " + color + "   " + ready);
            }
            stage.setScene(getScene(SceneName.SETNAME));
            stage.show();
        });
    }

    @Override
    public void displayNewCardInHand() {
        Platform.runLater(() -> {
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setHandCards();
        });
    }

    @Override
    public void nameChangeFailed() {
        Platform.runLater(() -> {
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).possible_Name.clear();
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).possible_Name.setPromptText("Nome già esistente");
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
    public void needConnection() {
        System.out.println("Need connection");
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
    public void playerListChanged() {
        System.out.println("Player list changed");
    }

    @Override
    public void displayPlayerColors() {
        Platform.runLater(() -> {
            listViewSetUpdate();

        });
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
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).chat_messages.getItems().add( message.getName() + ": "+  message.getMessage());
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).messageToSend.clear();
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).chat_message.getItems().add( message.getName() + ": "+  message.getMessage());

            //todo: for others scene
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
        System.out.println("End game started");
    }

    @Override
    public void displayHand() {
        Platform.runLater(() -> {
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setHandCards();
        });

    }

    @Override
    public void displayLeaderboard(LinkedHashMap<String, Integer> playerPoints) {
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

    // I DONT KNOW WHY
    @Override
    public void otherPlayerDraw(String name, Decks deckFrom, DeckPosition position) {
        Platform.runLater(() -> {
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setBoardCards(deckFrom);
        });
    }

    @Override
    public void newTurn() {
        Platform.runLater(() -> {
        if (controller.getActivePlayer().getName().equals(controller.getMyName())) {
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).yourTurnText.setVisible(true);
        } else {
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).yourTurnText.setVisible(false);
        }
        });

    }

    @Override
    public void otherPlayerInitialHand(String name) {
        System.out.println("Other player initial hand");
    }

    @Override
    public void secretAchievementChosen(String name) {
        Platform.runLater(() -> {
            System.out.println("Secret achievement chosen name : "+ name);
            System.out.println("Secret achievement chosen achievement : "+ controller.getSecretAchievement());
            if (name.equals(controller.getMyName())){
                ((ChooseSecretCardController) sceneControllerMap.get(SceneName.SECRETCARDCHOICE)).confirmation();
                ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).setSecretCard(controller.getSecretAchievement());
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
        Platform.runLater(() -> {
            ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateAllFloorCards();
        });
    }

    @Override
    public void chooseStartingCardFace(Card card) {
        Platform.runLater(() -> {
            ((ChooseStartingCardController) sceneControllerMap.get(SceneName.STARTINGCARDCHOICE)).setUp(card);
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
    public void displayPlayerPoints(String playerName) throws PlayerNotFoundByNameException {
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
        Platform.runLater(() -> {
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).setup();
        });

    }

    @Override
    public void playerRemoved(String name) {
        System.out.println("Player removed");
    }

    @Override
    public void otherPlayerReconnected(String name) {
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
        System.out.println("Game info displayed before platform runlater");
        Platform.runLater(() -> {
            System.out.println("Game info displayed");
            switch (controller.getGameState()) {
                case LOBBY:
                    System.out.println("Lobby");
                    throw new RuntimeException("Unexpected game state");
                case CHOOSE_STARTING_CARD:
                    System.out.println("Choose starting card");
                    stage.setScene(getScene(SceneName.STARTINGCARDCHOICE));
                    stage.show();
                    break;
                case CHOOSE_SECRET_ACHIEVEMENT:
                    chooseSecretAchievement(controller.getPotentialSecretAchievements());
                    break;
                default:
                    System.out.println("Game started! reconnection  19");
                    displayHand();
                    displayBoardCards();
                    displayCommonAchievements();
                    regenerateManuscript();

                    if (controller.getActivePlayer().getName().equals(controller.getMyName())) {
                        ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).yourTurnText.setVisible(true);
                    } else {
                        ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).yourTurnText.setVisible(false);
                    }

                    secretAchievementChosen(controller.getMyName());

                    stage.setScene(getScene(SceneName.GAME));
                    stage.show();
                    break;
        }
        System.out.println("Game info displayed 19");
        });
    }


    // Only GUI methods

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
        // todo: implement this
         }
    private void regenerateManuscript()
    {
        try {
            for(CornerCardFace c : controller.getPlayerByName(controller.getMyName()).getManuscript().getAllCards())
            {
                ((MainBoardSceneController) sceneControllerMap.get(SceneName.GAME)).updateManuscript(controller.getMyName(), c, c.getXCoord(), c.getYCoord());
            }
        } catch (PlayerNotFoundByNameException e) {
            throw new RuntimeException(e);
        }
    }
}
