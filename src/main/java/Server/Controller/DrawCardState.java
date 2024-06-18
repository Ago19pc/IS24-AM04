package Server.Controller;

import Client.Deck;
import Server.Card.*;
import Server.Enums.DeckPosition;
import Server.Enums.Decks;
import Server.Enums.GameState;
import Server.Exception.*;
import Server.GameModel.GameModel;
import Server.Messages.DrawCardMessage;
import Server.Messages.OtherPlayerDrawCardMessage;
import Server.Messages.ReconnectionMessage;
import Server.Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawCardState implements ServerState{
    private Controller controller;
    private GameModel gameModel;
    public DrawCardState(Controller controller, GameModel gameModel){
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
            try{
                controller.computeLeaderboard();
            } catch (AlreadyFinishedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setPlayerColor() {

    }

    @Override
    public void setSecretObjectiveCard() {

    }

    @Override
    public void setStartingCard() {

    }

    @Override
    public void playCard(Player player, int position, int xCoord, int yCoord, CornerCardFace face) throws TooFewElementsException {
        System.out.println("Player has already played");
        throw new TooFewElementsException("Already played");
    }

    @Override
    public void drawCard(Player player, DeckPosition deckPosition, Decks deck) throws AlreadyFinishedException, TooManyElementsException {
        Card drawnCard;
        switch (deck) {
            case RESOURCE -> {
                drawnCard = gameModel.getResourceDeck().popCard(deckPosition);
                player.addCardToHand(drawnCard);

            }
            case GOLD -> {
                drawnCard = gameModel.getGoldDeck().popCard(deckPosition);
                player.addCardToHand(drawnCard);
            }
            default -> {
                throw new IllegalArgumentException("Invalid deck");
            }
        }
        DrawCardMessage drawCardMessage = new DrawCardMessage(drawnCard);
        controller.getConnectionHandler().sendMessage(drawCardMessage, player.getName());
        if(gameModel.getResourceDeck().isEmpty() && gameModel.getGoldDeck().isEmpty()){
            try {
                controller.endGame();
            } catch (AlreadySetException e) {
                //do nothing as it's normal that it's already set
            }
        }
        OtherPlayerDrawCardMessage otherPlayerDrawCardMessage = new OtherPlayerDrawCardMessage(
                player.getName(),
                deck,
                deckPosition,
                switch (deck) {
                    //todo: aggiungere un metodo ai deck che ritorni le boardcards in una lista per evitare sto macello
                    case RESOURCE -> List.of(gameModel.getResourceDeck().getBoardCard().get(DeckPosition.DECK),  gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getResourceDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
                    case GOLD -> List.of(gameModel.getGoldDeck().getBoardCard().get(DeckPosition.DECK), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD), gameModel.getGoldDeck().getBoardCard().get(DeckPosition.SECOND_CARD));
                    default -> null;
                }
        );
        controller.getConnectionHandler().sendAllMessage(otherPlayerDrawCardMessage);
        controller.nextTurn();
    }

    @Override
    public void reactToDisconnection(String id) {
        String playerName = controller.getConnectionHandler().getPlayerNameByID(id);
        String activePlayerName = controller.getPlayerList().get(gameModel.getActivePlayerIndex()).getName();
        if(activePlayerName.equals(playerName)){
            if(!gameModel.getResourceDeck().isEmpty()){
                if(gameModel.getResourceDeck().getNumberOfCards() >= 1){
                    try {
                        controller.drawCard(controller.getPlayerByName(playerName), DeckPosition.DECK, Decks.RESOURCE);
                    } catch (TooManyElementsException | InvalidMoveException | AlreadyFinishedException | NotYetStartedException e) {
                        e.printStackTrace();
                    } catch (PlayerNotFoundByNameException e) {
                        throw new RuntimeException(e);
                    }
                } else if (gameModel.getResourceDeck().getBoardCard().get(DeckPosition.FIRST_CARD) != null){
                    try {
                        controller.drawCard(controller.getPlayerByName(playerName), DeckPosition.FIRST_CARD, Decks.RESOURCE);
                    } catch (TooManyElementsException | InvalidMoveException | AlreadyFinishedException | NotYetStartedException e) {
                        e.printStackTrace();
                    } catch (PlayerNotFoundByNameException e) {
                        throw new RuntimeException(e);
                    }
                } else{
                    try {
                        controller.drawCard(controller.getPlayerByName(playerName), DeckPosition.SECOND_CARD, Decks.RESOURCE);
                    } catch (TooManyElementsException | InvalidMoveException |
                             AlreadyFinishedException | NotYetStartedException |
                             PlayerNotFoundByNameException e) {
                        e.printStackTrace();
                    }
                }
            } else if(!gameModel.getGoldDeck().isEmpty()){
                if(gameModel.getGoldDeck().getNumberOfCards() >= 1){
                    try {
                        controller.drawCard(controller.getPlayerByName(playerName), DeckPosition.DECK, Decks.GOLD);
                    } catch (TooManyElementsException | InvalidMoveException |
                             AlreadyFinishedException | NotYetStartedException |
                             PlayerNotFoundByNameException e) {
                        e.printStackTrace();
                    }
                } else if (gameModel.getGoldDeck().getBoardCard().get(DeckPosition.FIRST_CARD) != null){
                    try {
                        controller.drawCard(controller.getPlayerByName(playerName), DeckPosition.FIRST_CARD, Decks.GOLD);
                    } catch (TooManyElementsException | InvalidMoveException |
                             AlreadyFinishedException | NotYetStartedException |
                             PlayerNotFoundByNameException e) {
                        e.printStackTrace();
                    }
                } else{
                    try {
                        controller.drawCard(controller.getPlayerByName(playerName), DeckPosition.SECOND_CARD, Decks.GOLD);
                    } catch (TooManyElementsException | AlreadyFinishedException | PlayerNotFoundByNameException |
                             InvalidMoveException | NotYetStartedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                controller.nextTurn();
            }
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
                GameState.DRAW_CARD
        );
        controller.getConnectionHandler().sendMessage(reconnectionMessage, playerName);
    }

    @Override
    public boolean isInSavedGameLobby() {
        return false;
    }
}
