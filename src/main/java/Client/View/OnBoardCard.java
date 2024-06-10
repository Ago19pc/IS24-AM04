package Client.View;

import Server.Enums.CardCorners;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import run.MainBoardSceneController;

import java.util.ArrayList;
import java.util.List;


public class OnBoardCard {
    @FXML
    public ImageView image;
    @FXML
    public Button TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

    public static List<OnBoardCard> onBoardCards = new ArrayList<>();
    public int x,y;

    private MainBoardSceneController sceneController;

    public OnBoardCard (Image image, int x, int y, MainBoardSceneController sceneController) {
        this.sceneController = sceneController;
        this.x = x;
        this.y = y;
        TOP_LEFT = new Button();
        TOP_RIGHT = new Button();
        BOTTOM_LEFT = new Button();
        BOTTOM_RIGHT = new Button();

        TOP_LEFT.setOnMouseClicked(e -> playCardTopLeft());
        TOP_RIGHT.setOnMouseClicked(e -> playCardTopRight());
        BOTTOM_LEFT.setOnMouseClicked(e -> playCardBottomLeft());
        BOTTOM_RIGHT.setOnMouseClicked(e -> playCardBottomRight());

        TOP_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: transparent;");
        TOP_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: transparent;");
        BOTTOM_LEFT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: transparent;");
        BOTTOM_RIGHT.setStyle("-fx-background-color: transparent; -fx-border-width: 2; -fx-border-color: transparent;");

        this.image = new ImageView(image);
        this.image.setFitHeight(100);
        this.image.setFitWidth(143);
        this.image.setLayoutX(x * 143 - x * 35);
        this.image.setLayoutY(y * 100 - y * 159);


        TOP_LEFT.setPrefHeight(41);
        TOP_LEFT.setPrefWidth(35);

        TOP_RIGHT.setPrefHeight(41);
        TOP_RIGHT.setPrefWidth(35);

        BOTTOM_LEFT.setPrefHeight(41);
        BOTTOM_LEFT.setPrefWidth(35);

        BOTTOM_RIGHT.setPrefHeight(41);
        BOTTOM_RIGHT.setPrefWidth(35);


        TOP_LEFT.setLayoutX(this.image.getLayoutX());
        TOP_LEFT.setLayoutY(this.image.getLayoutY());
        TOP_LEFT.setDisable(true);

        TOP_RIGHT.setLayoutX(this.image.getLayoutX() + 108);
        TOP_RIGHT.setLayoutY(this.image.getLayoutY());
        TOP_RIGHT.setDisable(true);

        BOTTOM_LEFT.setLayoutX(this.image.getLayoutX());
        BOTTOM_LEFT.setLayoutY(this.image.getLayoutY() + 59);
        BOTTOM_LEFT.setDisable(true);

        BOTTOM_RIGHT.setLayoutX(this.image.getLayoutX() + 108);
        BOTTOM_RIGHT.setLayoutY(this.image.getLayoutY() + 59);
        BOTTOM_RIGHT.setDisable(true);

    }

    public void place(Group yourManuscript) {
        yourManuscript.getChildren().add(image);
        yourManuscript.getChildren().add(TOP_LEFT);
        yourManuscript.getChildren().add(TOP_RIGHT);
        yourManuscript.getChildren().add(BOTTOM_LEFT);
        yourManuscript.getChildren().add(BOTTOM_RIGHT);
        onBoardCards.add(this);

    }

    void playCardTopLeft() {
        sceneController.placeCard(x - 1, y + 1);
    }

    void playCardTopRight() {
        sceneController.placeCard(x + 1, y + 1);
    }

    void playCardBottomLeft() {
        sceneController.placeCard(x - 1, y - 1);
    }

    void playCardBottomRight() {
        sceneController.placeCard(x + 1, y - 1);
    }



}
