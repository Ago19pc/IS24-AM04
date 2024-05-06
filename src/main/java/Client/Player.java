package Client;

import Server.Enums.Color;

public class Player {
    private final String name;
    private int manuscriptPoints;
    private int achievementPoints;
    private int handSize;
    private boolean active;
    private Color color;
    private boolean ready;

    public Player(String name) {
        this.name = name;
        this.manuscriptPoints = 0;
        this.achievementPoints = 0;
        this.handSize = 0;
        this.active = false;
        this.ready = false;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return manuscriptPoints + achievementPoints;
    }

    public void setManuscriptPoints(int points) {
        this.manuscriptPoints = points;
    }

    public void setAchievementPoints(int points) {
        this.achievementPoints = points;
    }

    public int getAchievementPoints() {
        return achievementPoints;
    }

    public void setHandSize(int size) {
        this.handSize = size;
    }

    public int getHandSize() {
        return handSize;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}
