package run;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class NameColorReadySceneController extends SceneController{
    public TextField possible_Name;
    public String name;
    public Label label_Username;
    public Label label_title;
    public Pane loginScene;

    public Button green_Button;
    public Button blue_Button;
    public Button yellow_Button;
    public Button red_Button;
    public Label label_color;
    public Button confirm_Button;
    public Button readyButton;
    public String color;
    public Pane user_pane;
    public Label label_ready;



    /*public void askSetName(ActionEvent actionEvent) {
        name = possible_Name.getText();
        controller.askSetName(name);
        blue_Button.setOpacity(1);
        yellow_Button.setOpacity(1);
        red_Button.setOpacity(1);
        green_Button.setOpacity(1);
        label_color.setOpacity(1);
        confirm_Button.setOpacity(0);
        label_Username.setOpacity(0);
        possible_Name.setDisable(true);
        //label_officialName.setOpacity(1);
    }*/

    public void askSetReady(ActionEvent actionEvent) {
        controller.askSetColor(color);
        controller.setReady();
        readyButton.setDisable(true);
        green_Button.setDisable(true);
        blue_Button.setDisable(true);
        yellow_Button.setDisable(true);
        red_Button.setDisable(true);
        readyButton.setOpacity(0);
        label_ready.setOpacity(1);
    }

}
