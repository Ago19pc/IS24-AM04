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

/**
 * Interface for a player
 */
public interface Player extends Serializable {
    /**
     * Add a card to the player's hand
     * @param card the card to add
     * @throws TooManyElementsException if the player's hand is full
     */
    public void addCardToHand(Card card) throws TooManyElementsException;

    /**
     * Remove a card from the player's hand
     * @param position the position card to remove
     * @throws IndexOutOfBoundsException if the hand does not have the position
     */
    public void removeCardFromHand(int position) throws IndexOutOfBoundsException, TooFewElementsException;

    /**
     * Returns the player's hand
     * @return the player's hand
     */
    public List<Card> getHand();

    /**
     * Returns the player's manuscript
     * @return the player's manuscript
     */
    public Manuscript getManuscript();

    /**
     * Returns the player's name
     * @return the player's name
     */
    public String getName();

    /**
     * Returns the player's color
     * @return the player's color
     */
    public Color getColor();

    /**
     * Returns the player's secret objective
     * @return the player's secret objective
     */
    public AchievementCard getSecretObjective();

    /**
     * Returns the player's points
     * @return the player's points
     */
    public int getPoints();

    /**
     * Set the player's secret objective
     * @param achievementCard the secret objective card
     * @throws AlreadySetException if the secret objective has already been set
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
     * @throws AlreadySetException if the manuscript has already been set
     */
    public void initializeManuscript(Card startingCard, Face face) throws AlreadySetException;


    /**
     * Sets a player ready status
     * @param ready the player's ready status
     */
    public void setReady(boolean ready);

    /**
     * Returns the player's ready status
     * @return boolean the player's ready status
     */
    public boolean isReady();
}
