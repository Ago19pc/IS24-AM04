package codex.docker;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Fills in and confirms the game's two connection screens (RMI or Socket, then server address
 * and port) with the values chosen by the entrypoint, so the player lands on the name screen.
 * Each screen is handled once: if joining fails, the player can still retry by hand.
 * <p>
 * Settings: {@code CODEX_CONNECTION} ({@code SOCKET} or {@code RMI}; anything else turns this
 * off), {@code CODEX_SERVER_HOST}, {@code CODEX_SERVER_PORT}.
 */
final class AutoConnect {

    private final String connection;
    private final String host;
    private final String port;
    private boolean methodChosen;
    private boolean joinRequested;
    private boolean nameScreenSeen;

    private AutoConnect(String connection, String host, String port) {
        this.connection = connection;
        this.host = host;
        this.port = port;
    }

    static void installFromEnvironment() {
        String connection = System.getenv().getOrDefault("CODEX_CONNECTION", "");
        if (!connection.equals("SOCKET") && !connection.equals("RMI")) {
            return;
        }
        String defaultPort = connection.equals("RMI") ? "1099" : "1234";
        AutoConnect auto = new AutoConnect(connection,
                System.getenv().getOrDefault("CODEX_SERVER_HOST", "server"),
                System.getenv().getOrDefault("CODEX_SERVER_PORT", defaultPort));
        Window.getWindows().addListener((ListChangeListener<Window>) change -> {
            while (change.next()) {
                for (Window window : change.getAddedSubList()) {
                    if (window instanceof Stage stage) {
                        stage.sceneProperty().addListener((observable, before, scene) -> auto.later(scene));
                        auto.later(stage.getScene());
                    }
                }
            }
        });
    }

    /** Acts after the game has finished setting up the scene. */
    private void later(Scene scene) {
        if (scene != null) {
            Platform.runLater(() -> handle(scene));
        }
    }

    private void handle(Scene scene) {
        Node radio = scene.lookup(connection.equals("RMI") ? "#rmi_radio_button" : "#socket_radio_button");
        Node next = scene.lookup("#next_button");
        if (!methodChosen && radio instanceof RadioButton choice && next instanceof Button nextButton) {
            methodChosen = true;
            choice.fire();      // setRMI() / setSocket(): selects it and enables Next
            nextButton.fire();  // on to the server screen
            return;
        }
        Node ip = scene.lookup("#text_ip");
        Node portField = scene.lookup("#text_Port");
        Node join = scene.lookup("#join_Button");
        if (!joinRequested && ip instanceof TextField ipText && portField instanceof TextField portText
                && join instanceof Button joinButton) {
            joinRequested = true;
            ipText.setText(host);
            portText.setText(port);
            System.out.println("[codex] collegamento automatico: " + connection + " " + host + ":" + port);
            joinButton.fire();
            return;
        }
        if (!nameScreenSeen && scene.lookup("#possible_Name") != null) {
            nameScreenSeen = true;
            System.out.println("[codex] scelta del nome");
        }
    }
}
