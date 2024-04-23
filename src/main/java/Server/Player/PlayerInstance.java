package Server.Player;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.ResourceCard;
import Server.Card.StartingCard;
import Server.Enums.Color;
import Server.Enums.Face;
import Server.Exception.AlreadySetException;
import Server.Exception.TooFewElementsException;
import Server.Exception.TooManyElementsException;
import Server.Manuscript.Manuscript;

import java.util.LinkedList;
import java.util.List;

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

    /**
     * Add a card to the player's hand
     *
     * @param card the card to add
     */
    @Override
    public void addCardToHand(Card card) throws TooManyElementsException{
        if(handCards.size() > 2){
            throw new TooManyElementsException("Player hand is full");
        }
        handCards.add((ResourceCard) card);
        System.out.println("addCardToHand");
    }

    /**
     * Remove a card from the player's hand
     *
     * @param position the position of the card to remove
     */
    @Override
    public void removeCardFromHand(int position) throws IndexOutOfBoundsException, TooFewElementsException{
        if(handCards.size() != 3){
            throw new TooFewElementsException("Hand is not full");
        }
        if(position < 0 || position >= handCards.size()){
            throw new IndexOutOfBoundsException("Position out of bounds");
        }
        handCards.remove(position);
    }

    /**
     * @return List<Card> the player's hand
     */
    @Override
    public List<Card> getHand() {
        System.out.println("getHand");
        List<Card> handToReturn = new LinkedList<>();
        for(ResourceCard card : handCards){
            handToReturn.add((Card) card);
        }
        return handToReturn;
    }

    /**
     * @return Manuscript the player's manuscript
     */
    @Override
    public Manuscript getManuscript() {
        System.out.println("getManuscript");
        return manuscript;
    }

    /**
     * @return String the player's name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return Color the player's color
     */
    @Override
    public Color getColor() {
        System.out.println("getColor");
        return color;
    }

    /**
     * @return AchievementCard the player's secret objective
     */
    @Override
    public AchievementCard getSecretObjective() {
        System.out.println("getSecretObjective");
        return secretObjective;
    }

    /**
     * @return int the player's points
     */
    @Override
    public int getPoints() {
        System.out.println("getPoints");
        return points;
    }

    /**
     * Set the player's secret objective
     *
     * @param achievementCard the secret objective card
     */
    @Override
    public void setSecretObjective(AchievementCard achievementCard) throws AlreadySetException{
        if(this.secretObjective != null){
            throw new AlreadySetException("Secret objective already set");
        }
        this.secretObjective = achievementCard;
        System.out.println("setSecretObjective");
    }

    /**
     * Set the player's color
     *
     * @param color the player's color
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Add points to the player's total
     *
     * @param num the number of points to add
     */
    @Override
    public void addPoints(int num) {
        this.points += num;
        System.out.println("addPoints");
    }

    /**
     * Initialize the player's manuscript
     *
     * @param startingCard the manuscript's starting card
     * @param face         the face of the starting card
     */
    @Override
    public void initializeManuscript(StartingCard startingCard, Face face) throws AlreadySetException{
        if(this.manuscript != null){
            throw new AlreadySetException("Manuscript already initialized");
        }
        this.manuscript = new Manuscript(startingCard, face);
        System.out.println("initializeManuscript");
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}
