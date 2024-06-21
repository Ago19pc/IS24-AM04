package Client.View;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for the visualization of the other player's manuscript.
 */
public class OtherPlayerTab {
    /**
     * Tab of the other player
     */
    public final Tab tab;
    /**
     * Scrollable pane of the images on the tab
     */
    public final ScrollPane scrollPane;
    /**
     * Group of the images on the tab
     */
    public final Group group;
    /**
     * List of cards on the manuscript
     */
    public final List<OnBoardCard> onBoardCardList = new ArrayList<>();

    /**
     * Constructor
     * @param playerName the name of the player
     */
    public OtherPlayerTab(String playerName){
        tab = new Tab(playerName);
        group = new Group();
        scrollPane = new ScrollPane(group);
        tab.setContent(scrollPane);
    }

    /**
     * This method should be called when you want to place a card on the manuscript of another player
     * @param onBoardCard the card to place
     */
    public void placeCard(OnBoardCard onBoardCard){
        onBoardCardList.add(onBoardCard);
        group.getChildren().add(onBoardCard.image);
    }
}
