package main.java.Card;

import main.java.Enums.Symbol;

import java.util.ArrayList;
import java.util.List;

public class AchievementFrontFace extends EmptyCardFace {
    private final List<Symbol> scoreRequirements;
    private final int score;

    /**
     * Constructor for the AchievementFrontFace
     *
     * @param imageURI the URI of the image
     * @param card the card that owns the face
     * @param scoreRequirements the score requirements
     * @param score the score
     */
    public AchievementFrontFace(String imageURI, Card card, ArrayList<Symbol> scoreRequirements, int score) {
        super(imageURI, card);
        this.scoreRequirements = new ArrayList<Symbol>(scoreRequirements);
        this.score = score;
    }

    /**
     * Returns the score requirements
     * @return List<Symbol> the score requirements
     */
    public List<Symbol> getScoreRequirements() {
        return scoreRequirements;
    }

    /**
     * Returns the score
     * @return int the score
     */
    public int getScore() {
        return score;
    }

}
