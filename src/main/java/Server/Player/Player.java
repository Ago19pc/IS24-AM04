package Server.Player;

import Server.Card.AchievementCard;
import Server.Card.Card;
import Server.Enums.Color;
import Server.Enums.Face;
import Server.Exception.AlreadySetException;
import Server.Exception.TooFewElementsException;
import Server.Exception.TooManyElementsException;
import Server.Manuscript.Manuscript;

import java.io.Serializable;
import java.util.List;

public interface Player extends Serializable {
    /**
     * Add a card to the player's hand
     * @param card the card to add
     */
    public void addCardToHand(Card card) throws TooManyElementsException;

    /**
     * Remove a card from the player's hand
     * @param position the position card to remove
     */
    public void removeCardFromHand(int position) throws IndexOutOfBoundsException, TooFewElementsException;

    /**
     * Returns the player's hand
     * @return the player's hand
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
    public void setSecretObjective(AchievementCard achievementCard) throws AlreadySetException;

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
    public void initializeManuscript(Card startingCard, Face face) throws AlreadySetException;


    /**
     * Sets a player ready status
     */
    public void setReady(boolean ready);

    /**
     * @return boolean the player's ready status
     */
    public boolean isReady();
}
