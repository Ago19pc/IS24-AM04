package Payloads;

public class PlayerNamePayload implements GeneralPayload {
    private final String playerName;

    public PlayerNamePayload(String playerName){
        this.playerName = playerName;
    }

    public void printData(){
        System.out.println("Player Name: " + playerName);
    }

    public String getPlayerName(){
        return playerName;
    }
}
