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
    public final Tab tab;
    public final ScrollPane scrollPane;
    public final Group group;
    public final List<OnBoardCard> onBoardCardList = new ArrayList<>();


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
