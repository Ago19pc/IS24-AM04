package Client.View;

import Client.Controller.ClientController;
import Server.Enums.Face;

/**
 * This class represents the state of the game where the player has to choose the starting card.
 */
public class StartingCardChoiceState implements CLIState{
    private CLI cli;
    private ClientController controller;

    public StartingCardChoiceState(CLI cli, ClientController controller) {
        this.cli = cli;
        this.controller = controller;
        askStartingCard();
    }
    /**
     * Asks the user to choose the starting card.
     */
    private void askStartingCard() {
        cli.printOnNewLine("Scegli la faccia della carta iniziale: ");
        cli.printOnNewLine("  1 - Faccia Anteriore");
        cli.printOnNewLine("  2 - Faccia Posteriore");
        cli.printPromptLine();
    }

    @Override
    public void decode(String[] args) {
        if (args.length != 1) {
            askStartingCard();
        } else {
            switch (args[0]) {
                case "1":
                    controller.chooseStartingCardFace(Face.FRONT);
                    break;
                case "2":
                    controller.chooseStartingCardFace(Face.BACK);
                    break;
                default:
                    askStartingCard();
                    break;
            }
        }
    }
}
