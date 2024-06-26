package Client.View;

import Client.Controller.ClientController;

import java.util.LinkedHashMap;

/**
 * CLI state for the leaderboard
 */
public class LeaderboardState implements CLIState{
    private final CLI cli;
    private final ClientController controller;
    /**
     * The points of the players in an ordered map
     * The key is the player name, the value is the points
     * The map is ordered by win order, from the highest to the lowest
     */
    LinkedHashMap<String, Integer> playerPoints;

    /**
     * Constructor for the leaderboard state. Displays the leaderboard
     * @param cli the CLI
     * @param playerPoints the points of the players in an ordered map
     * @param controller the client controller
     */
    public LeaderboardState(CLI cli, LinkedHashMap<String, Integer> playerPoints, ClientController controller){
        this.cli = cli;
        this.playerPoints = playerPoints;
        this.controller = controller;
        displayLeaderboard();
    }

    private void displayLeaderboard(){
        cli.printOnNewLine("Classifica: \n");
        int i = 1;
        for(String player: playerPoints.keySet()){
            System.out.println("    Il giocatore n." + i++ + "è " + player + ": " + playerPoints.get(player) + " punti");
        }
        cli.printOnNewLine("Digita 'lobby' per tornare alla lobby\n");
        cli.printPromptLine();
        controller.clear();
    }

    @Override
    public void decode(String[] args) {
        if(args.length != 1 || !args[0].equals("lobby")) {
            cli.printOnNewLine("Comando non valido");
            displayLeaderboard();
        } else {
            cli.backToLobby();
        }
    }
}
