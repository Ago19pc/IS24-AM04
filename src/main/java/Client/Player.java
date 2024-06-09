package Client;

import Server.Card.CornerCardFace;
import Server.Enums.Color;
import Server.Manuscript.Manuscript;

import java.io.Serializable;

public class Player implements Serializable {
    private final String name;
    private int manuscriptPoints;
    private int achievementPoints;
    private int handSize;
    private boolean active;
    private Color color;
    private boolean ready;

    private Manuscript manuscript;

    public Player(String name) {
        this.name = name;
        this.manuscriptPoints = 0;
        this.achievementPoints = 0;
        this.handSize = 0;
        this.active = false;
        this.ready = false;
    }

    public Player(String name, int points, int handSize, boolean active, Color color, Manuscript manuscript) {
        this.name = name;
        this.manuscriptPoints = points;
        this.achievementPoints = 0;
        this.handSize = handSize;
        this.active = active;
        this.color = color;
        this.ready = true;
        this.manuscript = manuscript;
    }

    /**
     * Get the name of the player
     * @return name the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Get the total points of the player
     * @return manuscriptPoints + achievementPoints the total points of the player
     */
    public int getPoints() {
        return manuscriptPoints + achievementPoints;
    }

    /**
     * Set the manuscript points of the player
     * @param points the manuscript points of the player
     */
    public void addManuscriptPoints(int points) {
        this.manuscriptPoints += points;
    }

    public void initializeManuscript(CornerCardFace startingCard) {
        System.out.println("Initializing manuscript for player " + name);
        this.manuscript = new Manuscript(startingCard);
    }



    /**
     * Set the achievement points of the player
     * @param points the achievement points of the player
     */
    public void setAchievementPoints(int points) {
        this.achievementPoints = points;
    }

    /**
     * Get the achievement points of the player
     * @return achievementPoints the achievement points of the player
     */
    public int getAchievementPoints() {
        return achievementPoints;
    }

    /**
     * set the size of the hand of the player
     * @param size the size of the hand of the player
     */
    public void setHandSize(int size) {
        this.handSize = size;
    }

    /**
     * Get the size of the hand of the player
     * @return handSize the size of the hand of the player
     */
    public int getHandSize() {
        return handSize;
    }

    /**
     * Set the active status of the player
     * @param active the status of the player
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get the active status of the player
     * @return active the status of the player
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the color of the player
     * @param color the color of the player
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the color of the player
     * @return color the color of the player
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set the ready status of the player
     * @param ready the status of the player
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     * Get the ready status of the player
     * @return ready the status of the player
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Adds a card to the manuscript of the player
     * @param x the x coordinate of the card
     * @param y the y coordinate of the card
     * @param cardFace the face of the card
     * @param turn the turn of the card
     */
    public void addCardToManuscript(int x, int y, CornerCardFace cardFace, int turn) {
        manuscript.addCard(x, y, cardFace, turn);
    }

    public Manuscript getManuscript() {
        return manuscript;
    }
}
