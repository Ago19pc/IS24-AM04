package Client.View;

import Client.Controller.ClientController;
import Client.Player;

/**
 * This class represents the lobby state of the game.
 */
public class SetNameState implements CLIState {
    private CLI cli;
    private ClientController controller;
    public SetNameState(CLI cli, ClientController controller) {
        this.cli = cli;
        this.controller = controller;
        if(!controller.getPlayers().isEmpty()) {
            if (controller.isSavedGame()) {
                cli.printOnNewLine("E' stata caricata una partita salvata. I giocatori della partita sono: ");
            } else {
                cli.printOnNewLine("I giocatori connessi alla partita sono: ");
            }
            for (Player player : controller.getPlayers()) {
                cli.printOnNewLine("    " + player.getName() + " - ");
                if(player.getColor() != null) {
                    System.out.print(player.getColor() + " - ");
                }
                if (player.isReady()) {
                    System.out.print("Pronto");
                } else {
                    System.out.print("Non pronto");
                }
            }
        }
        askNameOrId();
    }

    /**
     * Asks to set the name of the player or to reconnect using an id
     */
    private void askNameOrId() {
        cli.printOnNewLine("Inserisci il tuo nome per entrare in partita");
        cli.printOnNewLine("oppure riconnettiti alla partita in corso inserendo reconnect <id>");
        cli.printPromptLine();
    }
    @Override
    public void decode(String[] args) {
        if (args.length == 1) {
            if (controller.isSavedGame()) {
                controller.joinSavedGame(args[0]);
            } else {
                controller.askSetName(args[0]);
            }
        } else if (args.length == 2) {
            if (args[0].equals("reconnect")) {
                controller.reconnect(args[1]);
            } else {
                askNameOrId();
            }
        } else {
            askNameOrId();
        }
    }
}
