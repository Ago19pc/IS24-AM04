package main.java.Server.Card;

import java.util.List;
import java.util.Map;

import main.java.Server.Enums.Symbol;

public interface CardFace {
    /**
     * @return String the URI of the image
     */
    public String getImageURI();

    public Map<Integer, Symbol> getCornerSymbols();

    public List<Symbol> getCenterSymbols();

    public int getScore();

    public Map<Symbol, Integer> getScoreRequirements();

    Map<Symbol, Integer> getPlacementRequirements();

    public int getPlacementTurn();
}
