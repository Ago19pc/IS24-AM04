package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.util.List;
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

    @Override
    public Map<CardCorners, Symbol> getCornerSymbols() {
        throw new UnsupportedOperationException("Achievement cards do not have corner symbols");
    }

    @Override
    public List<Symbol> getCenterSymbols() {
        throw new UnsupportedOperationException("Achievement cards do not have center symbols");
    }

    @Override
    public Map<Symbol, Integer> getPlacementRequirements() {
        throw new UnsupportedOperationException("Achievement cards do not have placement requirements");
    }

    @Override
    public int getXCoord() {
        throw new UnsupportedOperationException("Achievement cards do not have coordinates");
    }

    @Override
    public int getYCoord() {
        throw new UnsupportedOperationException("Achievement cards do not have coordinates");
    }

    @Override
    public void setPlacementTurn(int placementTurn) {
        throw new UnsupportedOperationException("Achievement cards do not have placement turns");
    }

    @Override
    public void setXCoord(int xCoord) {
        throw new UnsupportedOperationException("Achievement cards do not have coordinates");
    }

    @Override
    public void setYCoord(int yCoord) {
        throw new UnsupportedOperationException("Achievement cards do not have coordinates");
    }

    @Override
    public int getPlacementTurn() {
        throw new UnsupportedOperationException("Achievement cards do not have placement turns");
    }

    @Override
    public Symbol getKingdom() {
        throw new UnsupportedOperationException("Achievement cards do not have kingdoms");
    }

}