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
        sceneController.setAll(controller, stage, sceneMap);
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
            for(int i = 0; i < controller.getPlayers().size(); i++) {
                String name = controller.getPlayers().get(i).getName();
                String color = controller.getPlayers().get(i).getColor() == null ? "No Color" : controller.getPlayers().get(i).getColor().toString();
                String ready = controller.getPlayers().get(i).isReady() ? "Ready" : "Not Ready";
                ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().set(i, name + "   " + color + "   " + ready);
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
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player = ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player;

        });
    }


    @Override
    public void successfulConnection() {
        stage.setScene(getScene(SceneName.SETNAME));
        stage.show();
        Platform.runLater(() -> {
               ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player = ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player;
        });
    }

    @Override
    public void displayNewCardInHand() {

    }

    @Override
    public void nameChangeFailed() {
        Platform.runLater(() -> {
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).name_exist.setOpacity(1);
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).possible_Name.clear();
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

    }

    @Override
    public void unavailableColor() {

    }

    @Override
    public void colorChangeFailed() {

    }

    @Override
    public void invalidColor() {

    }

    @Override
    public void needConnection() {

    }

    @Override
    public void connectionFailed() {
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
            for(int i = 0; i < controller.getPlayers().size(); i++) {
                String name = controller.getPlayers().get(i).getName();
                String color = controller.getPlayers().get(i).getColor() == null ? "No Color" : controller.getPlayers().get(i).getColor().toString();
                String ready = controller.getPlayers().get(i).isReady() ? "Ready" : "Not Ready";
                ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().set(i, name + "   " + color + "   " + ready);
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

            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player = ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player;

        });
    }

    @Override
    public void updateReady(String name, boolean ready) {

    }

    @Override
    public void chat(Message message) {

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
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player = ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player;

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
            for (int i = 0; i < controller.getPlayers().size(); i++) {
                ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player.getItems().add(controller.getPlayers().get(i).getName() + "   " + controller.getPlayers().get(i).getColor().toString() + "   " + (controller.getPlayers().get(i).isReady() ? "Ready" : "Not Ready"));
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
            ((NameReadySceneController) sceneControllerMap.get(SceneName.SETNAME)).list_Player = ((ColorReadySceneController) sceneControllerMap.get(SceneName.SETCOLOR)).list_Player;

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


}
