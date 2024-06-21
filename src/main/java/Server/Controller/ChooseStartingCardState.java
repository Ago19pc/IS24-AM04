package Server.Controller;

import Server.Card.AchievementCard;
import Server.Card.CornerCardFace;
import Server.Card.ResourceCard;
import Server.Card.StartingCard;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Exception.*;
import Server.GameModel.GameModel;
import Server.Messages.ReconnectionNameMessage;
import Server.Messages.SetStartingCardMessage;
import Server.Messages.StartGameMessage;
import Server.Messages.StartingCardsMessage;
import Server.Player.Player;

import java.util.List;
import java.util.Map;

/**
 * Class for the state where players choose their starting card

 */
public class ChooseStartingCardState implements ServerState{
    private Controller controller;
    private GameModel gameModel;

    /**
     * Constructor for the class
     * @param controller the controller
     * @param gameModel the game model
     */
    public ChooseStartingCardState(Controller controller, GameModel gameModel){
        this.controller = controller;
        this.gameModel = gameModel;
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
        boolean allset = controller.getPlayerList().stream().allMatch(p -> p.getManuscript() != null);
        if(allset){
            try {
                controller.giveInitialHand();
            } catch (AlreadySetException | AlreadyFinishedException e) {
                //do nothing as it's normal that it's already set
            }
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
        System.out.println("Reconnecting player in starting card choice");
        controller.getConnectionHandler().setOnline(newId);
        String playerName = controller.getConnectionHandler().getPlayerNameByID(newId);
        controller.getConnectionHandler().changePlayerId(playerName, oldId);
        System.out.println("Player " + playerName + " has now id " + oldId + "=" + controller.getConnectionHandler().getIdByName(playerName));
        ReconnectionNameMessage reconnectionNameMessage = new ReconnectionNameMessage(playerName);
        controller.getConnectionHandler().sendMessage(reconnectionNameMessage, playerName);
        StartingCardsMessage startingCardsMessage = new StartingCardsMessage(givenStartingCards.get(player));
        controller.getConnectionHandler().sendMessage(startingCardsMessage, playerName);
        if(player.getManuscript() != null){
            SetStartingCardMessage setStartingCardMessage = new SetStartingCardMessage(playerName, player.getManuscript().getCardByCoord(0, 0));
            controller.getConnectionHandler().sendMessage(setStartingCardMessage, playerName);
        }
        StartGameMessage startGameMessage = new StartGameMessage(
                List.of(
                        gameModel.getGoldDeck().getTopCardNoPop(),
                        gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                        gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                ),
                List.of(
                        (ResourceCard)gameModel.getResourceDeck().getTopCardNoPop(),
                        gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD),
                        gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)
                )
        );
        controller.getConnectionHandler().sendMessage(startGameMessage, playerName);
        for(Player p : controller.getPlayerList()){
            if(p.getManuscript() != null){
                SetStartingCardMessage setStartingCardMessage = new SetStartingCardMessage(p.getName(), p.getManuscript().getCardByCoord(0, 0));
                controller.getConnectionHandler().sendMessage(setStartingCardMessage, playerName);
            }
        }
    }

    @Override
    public boolean isInSavedGameLobby() {
        return false;
    }
}
