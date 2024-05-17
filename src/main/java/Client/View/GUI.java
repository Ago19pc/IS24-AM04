package Client.View;

import Client.Controller.ClientController;
import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Chat.Message;
import Server.Enums.Actions;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.PlayerNotFoundByNameException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import run.MainGUI;
import run.NameColorReadySceneController;
import run.SceneController;
import run.SceneName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUI implements UI{
    private static ClientController controller;
    private static Map<SceneName,Scene> sceneMap = new HashMap<>();
    private Stage stage;
    private final NameColorReadySceneController nameColorReadySceneController;


    public GUI(ClientController controller, NameColorReadySceneController nameColorReadySceneController) {
        this.controller = controller;
        this.nameColorReadySceneController = nameColorReadySceneController;
    }
    public void generateScene(String fxmlPath, SceneName sceneName, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());
        SceneController sceneController = (SceneController) fxmlLoader.getController();
        sceneController.setAll(controller, stage, sceneMap);
        sceneMap.put(sceneName, scene);
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
        stage.setScene(getScene(SceneName.SETCOLOR));
        stage.show();
    }

    @Override
    public void colorChanged() {

    }

    @Override
    public void successfulConnection() {
        stage.setScene(getScene(SceneName.SETNAME));
        stage.show();
    }

    @Override
    public void displayNewCardInHand() {

    }

    @Override
    public void nameChangeFailed() {
        stage.setScene(getScene(SceneName.SETNAME));
        stage.show();
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



}
