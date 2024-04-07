package Server.Player;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Card.StartingCard;
import Server.EventManager.EventManager;
import Server.Enums.Color;
import Server.Enums.EventType;
import Server.Enums.Face;
import Server.Manuscript.Manuscript;
import Server.Messages.ColorMessage;

import java.util.LinkedList;
import java.util.List;

public class PlayerInstance implements Player {
    private AchievementCard secretObjective;
    private final List<Card> handCards;
    private Color color;
    private final String name;
    private int points;
    private Manuscript manuscript;

    private final EventManager eventManager;

    public PlayerInstance(String name, EventManager eventManager) {
        this.name = name;
        this.eventManager = eventManager;
        this.handCards = new LinkedList<>();
    }

    /**
     * Add a card to the player's hand
     *
     * @param card the card to add
     */
    @Override
    public void addCardToHand(Card card) {
        handCards.add(card);
        System.out.println("addCardToHand");
    }

    /**
     * Remove a card from the player's hand
     *
     * @param card the card to remove
     */
    @Override
    public void removeCardFromHand(Card card) {
        handCards.remove(card);
        System.out.println("removeCardFromHand");
    }

    /**
     * @return List<Card> the player's hand
     */
    @Override
    public List<Card> getHand() {
        System.out.println("getHand");
        return handCards;
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
        System.out.println("getName");
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
    public void setSecretObjective(AchievementCard achievementCard) {
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
        eventManager.notify(EventType.SET_COLOR,new ColorMessage(this, color));
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
    public void initializeManuscript(StartingCard startingCard, Face face) {
        this.manuscript = new Manuscript(startingCard, face);
        System.out.println("initializeManuscript");
    }
}
