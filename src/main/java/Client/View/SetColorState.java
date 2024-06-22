package Client.View;

import Client.Controller.ClientController;
import Server.Enums.Color;

/**
 * This class represents the state of the game where the player has to set his color and ready status.
 */
public class SetColorState implements CLIState{
    private CLI cli;
    private ClientController controller;
    public SetColorState(CLI cli, ClientController controller) {
        this.cli = cli;
        this.controller = controller;
        askColor();
    }
    /**
     * Asks the user to set his color
     */
    private void askColor() {
        cli.printOnNewLine("Scegli il tuo colore: ");
        cli.printOnNewLine("  1 - Rosso");
        cli.printOnNewLine("  2 - Blu");
        cli.printOnNewLine("  3 - Verde");
        cli.printOnNewLine("  4 - Giallo");
        cli.printPromptLine();
    }

    @Override
    public void decode(String[] args) {
        if (args.length != 1) {
            askColor();
        } else {
            switch (args[0]) {
                case "1":
                    controller.askSetColor(Color.RED);
                    break;
                case "2":
                    controller.askSetColor(Color.BLUE);
                    break;
                case "3":
                    controller.askSetColor(Color.GREEN);
                    break;
                case "4":
                    controller.askSetColor(Color.YELLOW);
                    break;
                case "pronto":
                    controller.setReady();
                    break;
                default:
                    askColor();
                    break;
            }
        }
    }
}
