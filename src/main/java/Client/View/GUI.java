package Client.View;

import Client.Controller.ClientController;
import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Chat.Message;
import Server.Enums.Actions;
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
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).messageToSend.setPromptText("Scrivi un messaggio!");
            stage.setScene(getScene(SceneName.SETCOLOR));
            stage.show();
        });
    }

    @Override
    public void colorChanged() {
        Platform.runLater(() -> {
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setDisable(false);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).green_Button.setDisable(false);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).blue_Button.setDisable(false);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).yellow_Button.setDisable(false);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).red_Button.setDisable(false);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).label_color.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).confirm_Color_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).green_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).blue_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).yellow_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).red_Button.setOpacity(0);
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).readyButton.setOpacity(1);
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

    }

    @Override
    public void needColor() {

    }

    @Override
    public void needNameOrColor() {
        //maybe useless here
    }

    @Override
    public void unavailableColor() {
        //maybe useless here
    }

    @Override
    public void colorChangeFailed() {
        //maybe useless here
    }

    @Override
    public void invalidColor() {
        //maybe useless here
    }

    @Override
    public void needConnection() {
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
        stage.setScene(getScene(SceneName.JOIN));
        stage.show();
    }

    @Override
    public void cantDrawAchievementCards() {

    }

    @Override
    public void alreadyDone(Actions action) {

    }

    @Override
    public void cardNotPlaceable() {

    }

    @Override
    public void chatMessageIsEmpty() {

    }

    @Override
    public void deckIsEmpty() {

    }

    @Override
    public void gameAlreadyStarted() {

    }

    @Override
    public void gameNotYetStarted() {

    }

    @Override
    public void invalidCardForAction(Actions cardType) {

    }

    @Override
    public void notYetGivenCard(Actions cardType) {

    }

    @Override
    public void notYourTurn() {

    }

    @Override
    public void playerListChanged() {

    }

    @Override
    public void displayPlayerColors() {
        System.out.println("displayPlayerColors");
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
            for (i = 0; i < controller.getPlayers().size(); i++) {
                if(!controller.getPlayers().get(i).isReady()) {
                    break;
                }
            }
            if(i == controller.getPlayers().size() && i > 1) {
                stage.setScene(getScene(SceneName.GAME));
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

    }

    @Override
    public void endGameStarted() {

    }

    @Override
    public void displayHand() {

    }

    @Override
    public void displayLeaderboard() {

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

    }

    @Override
    public void newTurn() {

    }

    @Override
    public void otherPlayerInitialHand(String name) {

    }

    @Override
    public void secretAchievementChosen(String name) {

    }

    @Override
    public void startingCardChosen(String name) {

    }

    @Override
    public void gameStarted() {

    }

    @Override
    public void displayPlayerOrder() {

    }

    @Override
    public void displayBoardCards() {

    }

    @Override
    public void chooseStartingCardFace(Card card) {

    }

    @Override
    public void doFirst(Actions action) {

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

    }

    @Override
    public void cardPlaced(String playerName, int x, int y) {

    }

    @Override
    public void playerDisconnected(String playerName) {

    }

    @Override
    public void displayCommonAchievements() {

    }

    @Override
    public void tooManyPlayers() {

    }





    // Only GUI methods

    private void listViewSetUpdate() {
        for(int i = 0; i < controller.getPlayers().size(); i++) {
            String name = controller.getPlayers().get(i).getName();
            String color = controller.getPlayers().get(i).getColor() == null ? "No Color" : controller.getPlayers().get(i).getColor().toString();
            String ready = controller.getPlayers().get(i).isReady() ? "Ready" : "Not Ready";
            ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().set(i, name + "   " + color + "   " + ready);
            //----------------------------------------------------------q
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
            }
        }
        ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).cloneListView(((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player);
    }

}
