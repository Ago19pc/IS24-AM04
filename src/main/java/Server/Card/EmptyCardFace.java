package Server.Card;

import java.util.List;
import java.util.Map;

import Server.Enums.Symbol;

public class EmptyCardFace implements CardFace {
    private final String imageURI;

    /**
     * Constructor for the EmptyCardFace
     * @param imageURI the URI of the image
     */
    public EmptyCardFace(String imageURI) {
        this.imageURI = imageURI;
    }

    /**
     * @return String the URI of the image
     */
    @Override
    public String getImageURI() {
        return imageURI;
    }

    public int getScore() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have scores");
    }

    public Map<Integer, Symbol> getCornerSymbols() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have corner symbols");
    }

    public List<Symbol> getCenterSymbols() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have center symbols");
    }

    public Map<Symbol, Integer> getScoreRequirements() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have score requirements");
    }

    public Map<Symbol, Integer> getPlacementRequirements() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have placement requirements");
    }

    public int getPlacementTurn() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Empty cards do not have placement turns");
    }
}
