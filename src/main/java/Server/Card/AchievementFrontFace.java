package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;

import java.util.List;
import java.util.Map;

/**
 * This is the class for the front face of achievement cards
 */
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


    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException{
        return scoreRequirements;
    }


    public int getScore() throws UnsupportedOperationException {
        return score;
    }

    /**
     * @throws UnsupportedOperationException because achievement cards do not have corners
     */
    @Override
    public Map<CardCorners, Symbol> getCornerSymbols() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Achievement cards do not have corner symbols");
    }
    /**
     * @throws UnsupportedOperationException because achievement cards do not have center symbols
     */
    @Override
    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Achievement cards do not have center symbols");
    }
    /**
     * @throws UnsupportedOperationException because achievement cards do not have placement requirements
     */
    @Override
    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Achievement cards do not have placement requirements");
    }
    /**
     * @throws UnsupportedOperationException because achievement cards do not have coordinates
     */
    @Override
    public int getXCoord() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Achievement cards do not have coordinates");
    }
    /**
     * @throws UnsupportedOperationException because achievement cards do not have coordinates
     */
    @Override
    public int getYCoord() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Achievement cards do not have coordinates");
    }
    /**
     * @throws UnsupportedOperationException because achievement cards are not placeable
     */
    @Override
    public void setPlacementTurn(int placementTurn) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Achievement cards do not have placement turns");
    }
    /**
     * @throws UnsupportedOperationException because achievement cards do not have coordinates
     */
    @Override
    public void setXCoord(int xCoord) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Achievement cards do not have coordinates");
    }
    /**
     * @throws UnsupportedOperationException because achievement cards do not have coordinates
     */
    @Override
    public void setYCoord(int yCoord) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Achievement cards do not have coordinates");
    }
    /**
     * @throws UnsupportedOperationException because achievement cards are not placeable
     */
    @Override
    public int getPlacementTurn() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Achievement cards do not have placement turns");
    }
    /**
     * @throws UnsupportedOperationException because achievement cards don't have a kingdom
     */
    @Override
    public Symbol getKingdom() {
        throw new UnsupportedOperationException("Achievement cards do not have kingdoms");
    }

    /**
     * Returns a string representation of the score requirements
     * @return the string
     */
    public String scoreReqForPrint() {
        StringBuilder toRet = new StringBuilder();
        for (Symbol s: scoreRequirements.keySet()){
            if (scoreRequirements.get(s) != 0) {
                if (!toRet.isEmpty()) {
                    toRet.append(" ");
                }
                for(int i = 0; i < scoreRequirements.get(s); i++) {
                    toRet.append(s.toString()).append("");
                }
            }
        }
        return toRet.toString();
    }

}