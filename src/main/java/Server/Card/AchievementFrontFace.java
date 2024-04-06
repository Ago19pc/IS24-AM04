package Server.Card;

import Server.Enums.Symbol;



import java.util.Map;

public class AchievementFrontFace extends EmptyCardFace {
    private final Map<Symbol, Integer> scoreRequirements;
    private final int score;

    /**
     * Constructor for the AchievementFrontFace
     *
     * @param imageURI          the URI of the image
     * @param scoreRequirements the score requirements
     * @param score             the score
     */
    public AchievementFrontFace(String imageURI, Map<Symbol, Integer> scoreRequirements, int score) {
        super(imageURI);
        this.scoreRequirements = scoreRequirements;
        this.score = score;
    }

    /**
     * Returns the score requirements
     *
     * @return List<Symbol> the score requirements
     */
    public Map<Symbol, Integer> getScoreRequirements() {
        return scoreRequirements;
    }

    /**
     * Returns the score
     *
     * @return int the score
     */
    public int getScore() {
        return score;
    }

}