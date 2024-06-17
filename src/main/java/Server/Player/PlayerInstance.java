package Server.Player;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.ResourceCard;
import Server.Enums.Color;
import Server.Enums.Face;
import Server.Exception.AlreadySetException;
import Server.Exception.TooFewElementsException;
import Server.Exception.TooManyElementsException;
import Server.Manuscript.Manuscript;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the Player interface
 */
public class PlayerInstance implements Player {
    private AchievementCard secretObjective;
    private final List<ResourceCard> handCards;
    private Color color;
    private final String name;
    private int points;
    private Manuscript manuscript;
    private boolean ready;
    public PlayerInstance(String name) {
        this.name = name;
        this.handCards = new LinkedList<>();
        this.points = 0;
        this.ready = false;
    }


    @Override
    public void addCardToHand(Card card) throws TooManyElementsException{
        if(handCards.size() > 2){
            throw new TooManyElementsException("Player hand is full");
        }
        handCards.add((ResourceCard) card);
    }

    @Override
    public void removeCardFromHand(int position) throws IndexOutOfBoundsException{
        if(position < 0 || position >= handCards.size()){
            throw new IndexOutOfBoundsException("Position out of bounds");
        }
        handCards.remove(position);
    }

    @Override
    public List<Card> getHand() {
        List<Card> handToReturn = new LinkedList<>();
        for(ResourceCard card : handCards){
            handToReturn.add((Card) card);
        }
        return handToReturn;
    }


    @Override
    public Manuscript getManuscript() {
        return manuscript;
    }


    @Override
    public String getName() {
        return name;
    }


    @Override
    public Color getColor() {
        return color;
    }


    @Override
    public AchievementCard getSecretObjective() {
        return secretObjective;
    }


    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void setSecretObjective(AchievementCard achievementCard) throws AlreadySetException{
        if(this.secretObjective != null){
            throw new AlreadySetException("Secret objective already set");
        }
        this.secretObjective = achievementCard;
    }


    @Override
    public void setColor(Color color) {
        this.color = color;
    }


    @Override
    public void addPoints(int num) {
        this.points += num;
    }


    @Override
    public void initializeManuscript(Card startingCard, Face face) throws AlreadySetException{
        if(this.manuscript != null){
            throw new AlreadySetException("Manuscript already initialized");
        }
        this.manuscript = new Manuscript(startingCard.getCornerFace(face));
    }


    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}
