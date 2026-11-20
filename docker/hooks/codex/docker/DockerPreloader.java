package codex.docker;

import javafx.application.Preloader;
import javafx.stage.Stage;

/**
 * Docker-only JavaFX hooks, enabled by the entrypoint with
 * {@code -Djavafx.preloader=codex.docker.DockerPreloader}. The game's own code is untouched.
 */
public class DockerPreloader extends Preloader {

    @Override
    public void start(Stage preloaderStage) {
        FixedSizeWindows.install();
        AutoConnect.installFromEnvironment();
    }
}
