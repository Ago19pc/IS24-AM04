package Interface;

import Server.Enums.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * This class is responsible for the logic of the scene where the player chooses the color and sets ready.
 */
public class ColorReadySceneController extends  SceneController{
    /**
     * Constructor
     */
    public ColorReadySceneController() {}

    /**
     * Label to show the player if he is ready
     */
    public Label label_ready;
    /**
     * Button to choose the red color
     */
    public Button red_Button;
    /**
     * Button to choose the green color
     */
    public Button green_Button;
    /**
     * Button to choose the blue color
     */
    public Button blue_Button;
    /**
     * Button to choose the yellow color
     */
    public Button yellow_Button;
    /**
     * Label to show the color
     */
    public Label label_color;
    /**
     * Button to set ready
     */
    public Button readyButton;
    /**
     * The color of the player
     */
    public Color color;
    /**
     * The list of players
     */
    public ListView<String> list_Player;
    /**
     * Button to confirm the color
     */
    public Button confirm_Color_Button;
    /**
     * The messages of the chat
     */
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
    public void askSetReady() {
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
