package Server.Player;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.ResourceCard;
import Server.Enums.Color;
import Server.Enums.Face;
import Server.Exception.AlreadySetException;
import Server.Exception.TooManyElementsException;
import Server.Manuscript.Manuscript;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the Player interface
 */
public class PlayerInstance implements Player {
    /**
     * The secret objective of the player
     */
    private AchievementCard secretObjective;
    /**
     * The hand of the player
     */
    private final List<ResourceCard> handCards;
    /**
     * The color of the player
     */
    private Color color;
    /**
     * The name of the player
     */
    private final String name;
    /**
     * The points of the player
     */
    private int points;
    /**
     * The manuscript of the player
     */
    private Manuscript manuscript;
    /**
     * The ready status of the player
     */
    private boolean ready;
    /**
     * Constructor
     * @param name the name of the player
     */
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
        return new LinkedList<>(handCards);
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
