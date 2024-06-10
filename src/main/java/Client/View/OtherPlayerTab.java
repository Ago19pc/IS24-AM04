package Client.View;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.List;

public class OtherPlayerTab {
    public Tab tab;
    public ScrollPane scrollPane;
    public Group group;
    public List<OnBoardCard> onBoardCardList = new ArrayList<>();


    public OtherPlayerTab(String playerName){
        tab = new Tab(playerName);
        group = new Group();
        scrollPane = new ScrollPane(group);
        tab.setContent(scrollPane);
    }

    /**
     * This method should be called when you want to place a card on the manuscript of another player
     * @param onBoardCard
     */
    public void placeCard(OnBoardCard onBoardCard){
        onBoardCardList.add(onBoardCard);
        group.getChildren().add(onBoardCard.image);
    }
}
