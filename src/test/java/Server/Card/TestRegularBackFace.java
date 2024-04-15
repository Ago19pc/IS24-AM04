package Server.Card;

import Server.Enums.Symbol;
import org.junit.jupiter.api.Test;


import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRegularBackFace {
    @Test
    public void testGetCenterSymbols() {
        RegularBackFace regularBackFace = new RegularBackFace("image1.jpg", List.of(Symbol.NONE, Symbol.NONE, Symbol.NONE, Symbol.NONE), Symbol.FUNGUS);
        assertEquals(List.of(Symbol.NONE, Symbol.NONE, Symbol.NONE, Symbol.NONE), regularBackFace.getCenterSymbols());
    }
}