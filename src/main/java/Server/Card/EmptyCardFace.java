package main.java.Server.Card;

import java.util.List;
import java.util.Map;

import main.java.Server.Enums.Symbol;

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

    public int getScore() {
        return 0;
    }

    public Map<Integer, Symbol> getCornerSymbols() {
        return null;
    }

    public List<Symbol> getCenterSymbols() {
        return null;
    }
}
