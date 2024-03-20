package main.java.Player;

import main.java.Card.AchievementCard;
import main.java.Card.Card;
import main.java.Card.StartingCard;
import main.java.Enums.Color;
import main.java.Enums.Face;
import main.java.Manuscript.Manuscript;

import java.util.List;

public interface Player {
    /**
     * Add a card to the player's hand
     * @param card the card to add
     */
    public void addCardToHand(Card card);

    /**
     * Remove a card from the player's hand
     * @param card the card to remove
     */
    public void removeCardFromHand(Card card);

    /**
     * @return List<Card> the player's hand
     */
    public List<Card> getHand();

    /**
     * @return Manuscript the player's manuscript
     */
    public Manuscript getManuscript();

    /**
     * @return String the player's name
     */
    public String getName();

    /**
     * @return Color the player's color
     */
    public Color getColor();

    /**
     * @return AchievementCard the player's secret objective
     */
    public AchievementCard getSecretObjective();

    /**
     * @return int the player's points
     */
    public int getPoints();

    /**
     * Set the player's secret objective
     * @param achievementCard the secret objective card
     */
    public void setSecretObjective(AchievementCard achievementCard);

    /**
     * Set the player's color
     * @param color the player's color
     */
    public void setColor(Color color);

    /**
     * Add points to the player's total
     * @param num the number of points to add
     */
    public void addPoints(int num);

    /**
     * Initialize the player's manuscript
     * @param startingCard the manuscript's starting card
     * @param face the face of the starting card
     */
    public void initializeManuscript(StartingCard startingCard, Face face);
}
