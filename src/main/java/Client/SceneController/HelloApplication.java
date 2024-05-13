package Client.SceneController;

import Client.Controller.ClientController;
import Client.View.CLI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {

    private static ClientController controller;
    private static CLI cli;
    private static Map<SceneName,Scene> sceneMap = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException {
        generateScene("graficap1.fxml", SceneName.JOIN, stage);
        generateScene("login1.fxml", SceneName.NAMECOLORREADY, stage);
        stage.setTitle("Hello!");
        stage.setScene(sceneMap.get(SceneName.JOIN));
        stage.show();
    }

    public static void main(String[] args) throws RemoteException {

        controller = new ClientController();
        cli = new CLI(controller);
        controller.main(cli);
        cli.start();


        launch();
    }
    private void generateScene(String fxmlPath, SceneName sceneName, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JoinSceneController.class.getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load());
        SceneController controller = (SceneController) fxmlLoader.getController();
        controller.setAll(HelloApplication.controller, stage, sceneMap);
        sceneMap.put(sceneName, scene);
    }
}