package run;

import Server.Enums.Color;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ColorReadySceneController extends  SceneController{

    public Pane loginScene;
    public Label label_ready;
    public Label label_title;
    public Button red_Button;
    public Button green_Button;
    public Button blue_Button;
    public Button yellow_Button;
    public Label label_color;
    public Button readyButton;
    public Color color;
    public ListView<String> list_Player;
    public Button confirm_Color_Button;
    public ListView<String> chat_messages;
    public TextField messageToSend;



    public void askColorGreen(ActionEvent actionEvent) {
        green_Button.setStyle("-fx-background-color: #00FF00");
        blue_Button.setStyle("-fx-background-color: #FFFFFF");
        yellow_Button.setStyle("-fx-background-color: #FFFFFF");
        red_Button.setStyle("-fx-background-color: #FFFFFF");
        color = Color.GREEN;
    }

    public void askColorBlue(ActionEvent actionEvent) {
        green_Button.setStyle("-fx-background-color: #FFFFFF");
        blue_Button.setStyle("-fx-background-color: #0000FF");
        yellow_Button.setStyle("-fx-background-color: #FFFFFF");
        red_Button.setStyle("-fx-background-color: #FFFFFF");
        color = Color.BLUE;
    }

    public void askColorYellow(ActionEvent actionEvent) {
        green_Button.setStyle("-fx-background-color: #FFFFFF");
        blue_Button.setStyle("-fx-background-color: #FFFFFF");
        yellow_Button.setStyle("-fx-background-color: #FFFF00");
        red_Button.setStyle("-fx-background-color: #FFFFFF");
        color = Color.YELLOW;

    }

    public void askColorRed(ActionEvent actionEvent) {
        green_Button.setStyle("-fx-background-color: #FFFFFF");
        blue_Button.setStyle("-fx-background-color: #FFFFFF");
        yellow_Button.setStyle("-fx-background-color: #FFFFFF");
        red_Button.setStyle("-fx-background-color: #FF0000");
        color = Color.RED;

    }

    public void askSetReady(ActionEvent actionEvent) {
        controller.setReady();
        label_ready.setOpacity(1);
        readyButton.setDisable(true);
        readyButton.setOpacity(0);


    }

    public void setColorConfirm(ActionEvent actionEvent) {
        controller.askSetColor(color.toString());
    }

    public void sendMessages(ActionEvent actionEvent) {
        controller.sendChatMessage(messageToSend.getText());
    }
}
