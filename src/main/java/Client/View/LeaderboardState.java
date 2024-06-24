package Client.View;

import Client.Controller.ClientController;

import java.util.LinkedHashMap;

public class LeaderboardState implements CLIState{
    private final CLI cli;
    private final ClientController controller;
    LinkedHashMap<String, Integer> playerPoints;

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
