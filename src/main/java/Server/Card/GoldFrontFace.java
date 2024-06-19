package Server.Card;

import Server.Enums.CardCorners;
import Server.Enums.Symbol;


import java.util.List;
import java.util.Map;

/**
 * This class represents the front face of gold cards
 */
public class GoldFrontFace extends ResourceFrontFace {
    private final Map<Symbol, Integer> placementRequirements;
    private final Map<Symbol, Integer> scoreRequirements;

    /**
     * Constructor for the GoldFrontFace
     *
     * @param imageURI the URI of the image
     * @param cornerSymbols the corner symbols
     * @param score the score
     * @param placementRequirements the placement requirements
     * @param scoreRequirements the score requirements
     */
    public GoldFrontFace(String imageURI, Map<CardCorners, Symbol> cornerSymbols, int score, Map<Symbol, Integer> placementRequirements, Map<Symbol, Integer> scoreRequirements, Symbol kingdom){
        super(imageURI, cornerSymbols, score, kingdom);
        this.placementRequirements = placementRequirements;
        this.scoreRequirements = scoreRequirements;
    }

    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException{
        return placementRequirements;
    }

    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException {
        return scoreRequirements;
    }
    /**
     * @throws UnsupportedOperationException because front faces of gold cards do not have score requirements
     */
    @Override
    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Gold cards do not have center symbols");
    }

    /**
     * Returns a string representation of the card face
     * @return a string representation of the card face
     */
    @Override
    public String toString() {
        //
        String boundaries = "-".repeat(32);
        boundaries += "\n";
        String upper = String.format("|%-9s|%10s|%9s|\n", getCornerSymbols().get(CardCorners.TOP_LEFT), scoreReqForPrint(), getCornerSymbols().get(CardCorners.TOP_RIGHT));
        String middle = String.format("|%30s|\n", "REGNO: " + getKingdom());
        String lower = String.format("|%-9s|%10s|%9s|\n", getCornerSymbols().get(CardCorners.BOTTOM_LEFT), placeReqForPrint(), getCornerSymbols().get(CardCorners.BOTTOM_RIGHT));

        return boundaries + upper + middle + lower + boundaries;
    }

    /**
     * Returns a string representation of the placement requirements
     * @return the string
     */
    private String placeReqForPrint() {
        StringBuilder toRet = new StringBuilder();

        for (Symbol s : placementRequirements.keySet()){
            if (placementRequirements.get(s) != 0) {
                if (!toRet.isEmpty()) {
                    toRet.append(" ");
                }
                for(int i = 0; i < placementRequirements.get(s); i++) {
                    toRet.append(s.toString()).append("");
                }
            }
        }

        return toRet.toString();
    }
    /**
     * Returns a string representation of the score requirements
     * @return the string
     */
    private String scoreReqForPrint() {
        StringBuilder toRet = new StringBuilder();
        toRet.append(getScore());
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

        if (toRet.isEmpty()) {
            toRet.append(" ".repeat(placeReqForPrint().length()));
        }
        if (toRet.length() < placeReqForPrint().length()) {
            toRet.append(" ".repeat(placeReqForPrint().length() - toRet.length()));
        }

        return toRet.toString();
    }
}
