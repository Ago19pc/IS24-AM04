package Interface;

import Server.Enums.Color;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

/**
 * This class is responsible for the logic of the scene where the player chooses the color and sets ready.
 */
public class ColorReadySceneController extends  SceneController{

    public Pane loginScene;
    public Label label_ready;
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


    /**
     * This method is called when the players whants to choose the green color
     */
    public void askColorGreen() {
        green_Button.setStyle("-fx-background-color: #00FF00;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        blue_Button.setStyle("-fx-background-color: #eae1b8;"+ "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        yellow_Button.setStyle("-fx-background-color: #eae1b8;"+ "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        red_Button.setStyle("-fx-background-color: #eae1b8;"+ "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        color = Color.GREEN;
        confirm_Color_Button.setOpacity(1);
        confirm_Color_Button.setDisable(false);
    }

    /**
     * This method is called when the players whants to choose the blue color
     */
    public void askColorBlue() {
        green_Button.setStyle("-fx-background-color: #eae1b8;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        blue_Button.setStyle("-fx-background-color: #0000FF;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        yellow_Button.setStyle("-fx-background-color: #eae1b8;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        red_Button.setStyle("-fx-background-color: #eae1b8;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        color = Color.BLUE;
        confirm_Color_Button.setOpacity(1);
        confirm_Color_Button.setDisable(false);
    }

    /**
     * This method is called when the players whants to choose the yellow color
     */
    public void askColorYellow() {
        green_Button.setStyle("-fx-background-color: #eae1b8;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        blue_Button.setStyle("-fx-background-color: #eae1b8;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        yellow_Button.setStyle("-fx-background-color: #FFFF00;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        red_Button.setStyle("-fx-background-color: #eae1b8;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        color = Color.YELLOW;
        confirm_Color_Button.setOpacity(1);
        confirm_Color_Button.setDisable(false);
    }

    /**
     * This method is called when the players want to choose the red color
     */
    public void askColorRed() {
        green_Button.setStyle("-fx-background-color: #eae1b8;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        blue_Button.setStyle("-fx-background-color: #eae1b8;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        yellow_Button.setStyle("-fx-background-color: #eae1b8;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        red_Button.setStyle("-fx-background-color: #FF0000;" + "-fx-border-color: #000000;" + "-fx-border-radius: 3px;");
        confirm_Color_Button.setOpacity(1);
        confirm_Color_Button.setDisable(false);
        color = Color.RED;

    }

    /**
     * This method is called when the players want to set ready
     */
    public void askSetReady(ActionEvent actionEvent) {
        controller.setReady();
        label_ready.setOpacity(1);
        readyButton.setDisable(true);
        readyButton.setOpacity(0);
    }

    /**
     * This method is called when the server accepts the color request
     */
    public void setColorConfirm() {
        controller.askSetColor(color);
    }



}
