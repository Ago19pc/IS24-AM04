package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Card.ResourceCard;
import Server.Card.StartingCard;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.AlreadyStartedException;
import Server.Exception.TooFewElementsException;
import Server.Exception.TooManyElementsException;
import Server.GameModel.GameModel;
import Server.Messages.*;
import Server.Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class for the state where players choose their secret achievement
 */
public class ChooseSecretAchievementState implements ServerState{
    private Controller controller;
    private GameModel gameModel;

    /**
     * Constructor for the class
     * @param controller the controller
     * @param gameModel the game model
     */
    public ChooseSecretAchievementState(Controller controller, GameModel gameModel){
        this.controller = controller;
    }

    @Override
    public void addPlayer(String name, String clientID) throws AlreadyStartedException {
        throw new AlreadyStartedException("Game already started");
    }

    @Override
    public void addSavedPlayer(String clientId, String name) throws AlreadyStartedException {
        throw new AlreadyStartedException("Game already started");
    }

    @Override
    public void removePlayer(Player player) {

        if(controller.getPlayerList().size() == 1){
            controller.disconnectionLeaderboard();
        }

        boolean allSet = controller.getPlayerList().stream().allMatch(p -> p.getSecretObjective() != null);
        if(allSet){
            controller.shufflePlayerList();
            controller.nextTurn();
        }
    }

    @Override
    public void playCard(Player player, int position, int xCoord, int yCoord, CornerCardFace face) throws TooFewElementsException {
        System.out.println("Player has already played");
        throw new TooFewElementsException("Already played");
    }

    @Override
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException {
        throw new TooManyElementsException("Play a card first");
    }

    @Override
    public void reactToDisconnection(String id) {
        new DisconnectionTimer(controller, controller.getConnectionHandler(), id, 60);
    }

    @Override
    public void reconnect(String oldId, String newId, Player player, Map<Player, List<AchievementCard>> givenSecretObjectiveCards, Map<Player, StartingCard> givenStartingCards) {
        controller.getConnectionHandler().setOnline(newId);
        String playerName = controller.getConnectionHandler().getPlayerNameByID(newId);
        controller.getConnectionHandler().changePlayerId(playerName, oldId);
        ReconnectionNameMessage reconnectionNameMessage = new ReconnectionNameMessage(playerName);
        controller.getConnectionHandler().sendMessage(reconnectionNameMessage, playerName);
        StartGameMessage startMessage = new StartGameMessage(
                List.of(
                        gameModel.getGoldDeck().getTopCardNoPop(),
                        gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                        gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                ),
                List.of(
                        (ResourceCard) gameModel.getResourceDeck().getTopCardNoPop(),
                        gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                        gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                )
        );
        controller.getConnectionHandler().sendMessage(startMessage, playerName);
        for(Player p: controller.getPlayerList()){
            SetStartingCardMessage startingMessage = new SetStartingCardMessage(p.getName(), p.getManuscript().getCardByCoord(0, 0));
            controller.getConnectionHandler().sendMessage(startingMessage, playerName);
        }
        InitialHandMessage initialHandMessage = new InitialHandMessage(player.getHand());
        controller.getConnectionHandler().sendMessage(initialHandMessage, playerName);
        for(Player p: controller.getPlayerList()){
            OtherPlayerInitialHandMessage otherPlayerInitialHandMessage = new OtherPlayerInitialHandMessage(p.getName());
            controller.getConnectionHandler().sendMessage(otherPlayerInitialHandMessage, playerName);
        }
        List<AchievementCard> secretObjectiveCards = givenSecretObjectiveCards.get(player);
        List<AchievementCard> commonAchievements = new ArrayList<>();
        commonAchievements.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.FIRST_CARD));
        commonAchievements.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
        AchievementCardsMessage achievementCardsMessage = new AchievementCardsMessage(secretObjectiveCards, commonAchievements);
        controller.getConnectionHandler().sendMessage(achievementCardsMessage, playerName);
        if(player.getSecretObjective() != null){
            SetSecretCardMessage setSecretCardMessage = new SetSecretCardMessage(player.getName());
            controller.getConnectionHandler().sendMessage(setSecretCardMessage, playerName);
        }
        for(Player p: controller.getPlayerList()){
            SetSecretCardMessage setSecretCardMessage = new SetSecretCardMessage(p.getName());
            controller.getConnectionHandler().sendMessage(setSecretCardMessage, playerName);
        }
    }

    @Override
    public boolean isInSavedGameLobby() {
        return false;
    }
}
