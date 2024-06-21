package Server.Controller;

import Client.Deck;
import Server.Card.*;
import Server.Enums.*;
import Server.Exception.AlreadyFinishedException;
import Server.Exception.AlreadyStartedException;
import Server.Exception.TooFewElementsException;
import Server.Exception.TooManyElementsException;
import Server.GameModel.GameModel;
import Server.Messages.OtherPlayerPlayCardMessage;
import Server.Messages.ReconnectionMessage;
import Server.Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class for the state where players place their cards
 */
public class PlaceCardState implements ServerState{
    private Controller controller;
    private GameModel gameModel;
    /**
     * Constructor
     * @param controller the controller
     * @param gameModel the game model
     */
    public PlaceCardState(Controller controller, GameModel gameModel){
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
        if(controller.getPlayerList().size() == 1){
            controller.disconnectionLeaderboard();
        }
    }

    @Override
    public void playCard(Player player, int position, int xCoord, int yCoord, CornerCardFace cardFace) throws TooFewElementsException {
        player.removeCardFromHand(position);
        int cardPoints;
        int obtainedPoints = 0;
        try {
            cardPoints = cardFace.getScore();
        } catch (UnsupportedOperationException e) {
            if (e.getMessage() == "Regular cards do not have scores") ;
            cardPoints = 0;
        }
        Map<Symbol, Integer> scoreRequirements;
        try {
            scoreRequirements = cardFace.getScoreRequirements();
        } catch (UnsupportedOperationException e) {
            if (e.getMessage() == "Regular cards do not have score requirements") ;
            scoreRequirements = null;
        }
        System.out.println("ScoreRequirements " + scoreRequirements + "Player " + player.getName() + " CardPoints " + cardPoints);
        if (scoreRequirements != null) {
            Symbol requiredSymbol = (Symbol) scoreRequirements.keySet().toArray()[0];
            int i = 0;
            for (Symbol symbol : scoreRequirements.keySet()) {
                if (scoreRequirements.get(symbol) != 0) {
                    requiredSymbol = (Symbol) scoreRequirements.keySet().toArray()[i];
                }
                i++;
            }
            /*System.out.println("///////////////////////////");
            System.out.println("RequiredSymbol " + requiredSymbol);
            System.out.println("///////////////////////////");*/
            int requiredQuantity = scoreRequirements.get(requiredSymbol);
            System.out.println("RequiredQuantity " + requiredQuantity);
            int actualQuantity;
            if (requiredSymbol == Symbol.COVERED_CORNER) {
                actualQuantity = player.getManuscript().getCardsUnder(cardFace).size();
            } else {
                actualQuantity = player.getManuscript().getSymbolCount(requiredSymbol);
                Symbol finalRequiredSymbol = requiredSymbol;
                int quantityOnCard = cardFace.getCornerSymbols().entrySet().stream()
                        .filter(entry -> entry.getValue() == finalRequiredSymbol).collect(Collectors.toList()).size();
                actualQuantity += quantityOnCard;
            }
            //System.out.println("RequiredSymbols " + requiredSymbol + " ScoreRequirements" + scoreRequirements);
            if(requiredQuantity != 0){
                obtainedPoints = actualQuantity / requiredQuantity * cardPoints;
                player.addPoints(obtainedPoints);
            } else {
                player.addPoints(cardPoints);
            }
        } else {
            player.addPoints(cardPoints);
        }
        player.getManuscript().addCard(xCoord, yCoord, cardFace, controller.getTurn());
        OtherPlayerPlayCardMessage otherPlayerPlayCardMessage = new OtherPlayerPlayCardMessage(
                player.getName(),
                cardFace,
                xCoord,
                yCoord,
                obtainedPoints
        );
        controller.changeState(new DrawCardState(controller, gameModel));
        controller.getConnectionHandler().sendAllMessage(otherPlayerPlayCardMessage);
    }

    @Override
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws TooManyElementsException {
        throw new TooManyElementsException("Play a card first");
    }

    @Override
    public void reactToDisconnection(String id) {
        String playerName = controller.getConnectionHandler().getPlayerNameByID(id);
        String activePlayerName = controller.getPlayerList().get(gameModel.getActivePlayerIndex()).getName();
        if(activePlayerName.equals(playerName)) {
            controller.nextTurn();
        }
        new DisconnectionTimer(controller, controller.getConnectionHandler(), id, 180);
    }

    @Override
    public void reconnect(String oldId, String newId, Player player, Map<Player, List<AchievementCard>> givenSecretObjectiveCards, Map<Player, StartingCard> givenStartingCards) {
        System.out.println("Reconnecting player in game");
        controller.getConnectionHandler().setOnline(newId);
        String playerName = controller.getConnectionHandler().getPlayerNameByID(newId);
        controller.getConnectionHandler().changePlayerId(playerName, oldId);
        List<AchievementCard> commonAchievementCards = new ArrayList<>();
        commonAchievementCards.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.FIRST_CARD));
        commonAchievementCards.add(gameModel.getAchievementDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
        Deck<GoldCard> goldDeck = new Deck<GoldCard>(
                gameModel.getGoldDeck().getNumberOfCards(),
                new ArrayList<>(List.of(gameModel.getGoldDeck().getTopCardNoPop() ,gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD)))
        );
        Deck<ResourceCard> resourceDeck = new Deck<ResourceCard>(
                gameModel.getResourceDeck().getNumberOfCards(),
                new ArrayList<>(List.of((ResourceCard) gameModel.getResourceDeck().getTopCardNoPop() ,gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD)))
        );
        List<Client.Player> playerList = new ArrayList<>();
        for (Player p : gameModel.getPlayerList()){
            playerList.add(new Client.Player(
                    p.getName(),
                    p.getPoints(),
                    p.getHand().size(),
                    gameModel.getActivePlayerIndex() == controller.getPlayerList().indexOf(p),
                    p.getColor(),
                    p.getManuscript()
            ));
        }
        ReconnectionMessage reconnectionMessage = new ReconnectionMessage(
                newId,
                commonAchievementCards,
                goldDeck,
                resourceDeck,
                playerName,
                player.getSecretObjective(),
                player.getHand(),
                gameModel.getTurn(),
                playerList,
                gameModel.getChat(),
                GameState.PLACE_CARD
        );
        controller.getConnectionHandler().sendMessage(reconnectionMessage, playerName);
    }

    @Override
    public boolean isInSavedGameLobby() {
        return false;
    }
}
