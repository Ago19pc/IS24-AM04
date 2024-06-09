package Client.View;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

public class OtherPlayerTab {
    public Tab tab;
    public ScrollPane scrollPane;
    public Group group;
    public OtherPlayerTab(String playerName){
        tab = new Tab(playerName);
        scrollPane = new ScrollPane(group);
        group = new Group();
        tab.setContent(scrollPane);
    }
    //public void otherUpdateManuscript()
}
