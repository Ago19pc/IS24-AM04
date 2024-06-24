package Client.View;
import Client.Controller.ClientController;

/**
 * This class represents the state of the game where the player has to choose the secret card.
 */
public class SecretCardChoiceState implements CLIState{
    private final CLI cli;
    private final ClientController controller;
    public SecretCardChoiceState(CLI cli, ClientController controller) {
        this.cli = cli;
        this.controller = controller;
        askSecretCard();
    }
    /**
     * Asks the user to choose the secret card.
     */
    private void askSecretCard(){
        cli.printOnNewLine("Scegli il tuo obiettivo segreto: ");
        cli.printPromptLine();
    }

    @Override
    public void decode(String[] args) {
        if(args.length != 1){
            askSecretCard();
        } else {
            switch (args[0]) {
                case "1":
                    controller.chooseSecretAchievement(0);
                    break;
                case "2":
                    controller.chooseSecretAchievement(1);
                    break;
                default:
                    askSecretCard();
                    break;
            }
        }
    }
}
