package Client.View;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Interface.MainBoardSceneController;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for the visualization of the card on the screen.
 * It's just a simpler way to handle images and buttons all together.
 */
public class OnBoardCard {
    /**
     * Image of the card
     */
    @FXML
    public final ImageView image;
    /**
     * Button to select the top left corner of the card
     */
    @FXML
    public final Button TOP_LEFT;
    /**
     * Button to select the top right corner of the card
     */
    @FXML
    public final Button TOP_RIGHT;
    /**
     * Button to select the bottom left corner of the card
     */
    @FXML
    public final Button BOTTOM_LEFT;
    /**
     * Button to select the bottom right corner of the card
     */
    @FXML
    public final Button BOTTOM_RIGHT;
    /**
     * List of cards on the manuscript
     */
    public static final List<OnBoardCard> onBoardCards = new ArrayList<>();
    /**
     * The x coordinate of the card
     */
    public final int x;
    /**
     * The y coordinate of the card
     */
    public final int y;

    private final MainBoardSceneController sceneController;
    /**
     * Constructor
     * @param image the image of the card
     * @param x the x coordinate of the card
     * @param y the y coordinate of the card
     * @param sceneController the controller of the scene
     */
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

    /**
     * This method places the card on the board.
     * This is to be used only to place on the board of the player who is running the application.
     * @param yourManuscript the group where the card will be placed
     */
    public void place(Group yourManuscript) {
        yourManuscript.getChildren().add(image);
        yourManuscript.getChildren().add(TOP_LEFT);
        yourManuscript.getChildren().add(TOP_RIGHT);
        yourManuscript.getChildren().add(BOTTOM_LEFT);
        yourManuscript.getChildren().add(BOTTOM_RIGHT);
        onBoardCards.add(this);

    }

    /**
     * Places the selected card on the topleft corner of this cards
     */
    void playCardTopLeft() {
        sceneController.placeCard(x - 1, y + 1);
    }

    /**
     * Places the selected card on the topright corner of this cards
     */
    void playCardTopRight() {
        sceneController.placeCard(x + 1, y + 1);
    }

    /**
     * Places the selected card on the bottomleft corner of this cards
     */
    void playCardBottomLeft() {
        sceneController.placeCard(x - 1, y - 1);
    }

    /**
     * Places the selected card on the bottomright corner of this cards
     */
    void playCardBottomRight() {
        sceneController.placeCard(x + 1, y - 1);
    }



}
