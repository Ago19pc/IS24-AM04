package codex.docker;

import javafx.application.Preloader;
import javafx.collections.ListChangeListener;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Docker-only hook, enabled by the entrypoint with
 * {@code -Djavafx.preloader=codex.docker.FixedSizeWindows}.
 * <p>
 * Every scene of Codex Naturalis is 1024x768, so a bigger window only adds an empty area.
 * On macOS with XQuartz, drag-resizing a JavaFX window also turns it black for good.
 * Locking the size of the game windows avoids both, without touching the game's code.
 */
public class FixedSizeWindows extends Preloader {

    @Override
    public void start(Stage preloaderStage) {
        Window.getWindows().addListener((ListChangeListener<Window>) change -> {
            while (change.next()) {
                for (Window window : change.getAddedSubList()) {
                    if (window instanceof Stage stage && stage.isResizable()) {
                        // the size is not laid out yet here: the window keeps whatever size the
                        // game gives it, it just cannot be resized by hand any more
                        stage.setResizable(false);
                        System.out.println("[codex] finestra a dimensione fissa: " + stage.getTitle());
                    }
                }
            }
        });
    }
}
