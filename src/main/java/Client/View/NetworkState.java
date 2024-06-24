package Client.View;

/**
 * This state is used to ask the user to choose the network mode
 */
public class NetworkState implements CLIState{
    private final CLI cli;

    /**
     * Asks the user to choose the network mode
     * @param cli the CLI
     */
    public NetworkState(CLI cli){
        this.cli = cli;
        cli.askConnectionMode();
    }

    @Override
    public void decode(String[] args){
        if(args.length != 1){
            cli.askConnectionMode();
        }
        else if(args[0].equalsIgnoreCase("RMI")) {
            cli.setRMIMode(true);
        } else if(args[0].equalsIgnoreCase("SOCKET")) {
            cli.setRMIMode(false);
        } else {
            cli.askConnectionMode();
        }
    }
}
