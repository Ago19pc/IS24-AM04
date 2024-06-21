package Client.View;

/**
 * This state is used to ask the user to join a server
 */
public class JoinState implements CLIState{
    private CLI cli;

    public JoinState(CLI cli){
        this.cli = cli;
        askJoin();
    }

    /**
     * Asks the user to join a server
     */
    private void askJoin() {
        cli.printOnNewLine("A che server vuoi connetterti? <ip> <porta>");
        cli.printPromptLine();
    }

    @Override
    public void decode(String[] args) {
        if (args.length != 2) {
            cli.printOnNewLine("Utilizzo corretto: join <ip> <porta>");
            askJoin();
        } else {
            cli.joinServer(args[0], Integer.parseInt(args[1]));
        }
    }
}
