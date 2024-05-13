package Client.SceneController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    public Label label_officialName;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void askColorGreen(ActionEvent actionEvent) {
        green_Button.setStyle("-fx-background-color: #00FF00");
        blue_Button.setStyle("-fx-background-color: #FFFFFF");
        yellow_Button.setStyle("-fx-background-color: #FFFFFF");
        red_Button.setStyle("-fx-background-color: #FFFFFF");
        color = "green";
        readyButton.setOpacity(1);
    }

    public void askColorBlue(ActionEvent actionEvent) {
        green_Button.setStyle("-fx-background-color: #FFFFFF");
        blue_Button.setStyle("-fx-background-color: #0000FF");
        yellow_Button.setStyle("-fx-background-color: #FFFFFF");
        red_Button.setStyle("-fx-background-color: #FFFFFF");
        color = "blue";
        readyButton.setOpacity(1);
    }

    public void askColorYellow(ActionEvent actionEvent) {
        green_Button.setStyle("-fx-background-color: #FFFFFF");
        blue_Button.setStyle("-fx-background-color: #FFFFFF");
        yellow_Button.setStyle("-fx-background-color: #FFFF00");
        red_Button.setStyle("-fx-background-color: #FFFFFF");
        color = "yellow";
        readyButton.setOpacity(1);
    }

    public void askColorRed(ActionEvent actionEvent) {
        green_Button.setStyle("-fx-background-color: #FFFFFF");
        blue_Button.setStyle("-fx-background-color: #FFFFFF");
        yellow_Button.setStyle("-fx-background-color: #FFFFFF");
        red_Button.setStyle("-fx-background-color: #FF0000");
        color = "red";
        readyButton.setOpacity(1);
    }

    public void askSetName(ActionEvent actionEvent) {
        name = possible_Name.getText();
        controller.askSetName(name);
        green_Button.setOpacity(1);
        blue_Button.setOpacity(1);
        yellow_Button.setOpacity(1);
        red_Button.setOpacity(1);
        label_color.setOpacity(1);
        confirm_Button.setOpacity(0);
        label_Username.setOpacity(0);
        possible_Name.setDisable(true);
        possible_Name.setText("You are " + name);
        label_officialName.setOpacity(1);
    }

    public void askSetReady(ActionEvent actionEvent) {
        controller.askSetColor(color);
        controller.setReady();
        readyButton.setDisable(true);
        readyButton.setText("You are Ready!");
    }



}