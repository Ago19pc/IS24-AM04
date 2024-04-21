package Server.Messages;

import Server.Enums.Color;
import Server.Player.Player;

public class ColorMessage implements GeneralMessage {

    private final Player player;
    private final Color color;
    public ColorMessage(Player player, Color color)
    {
        this.player = player;
        this.color = color;
    }
    @Override
    public void printData() {
        System.out.println(player.getName() + " has chosen the color " + color);
    }

    public Player getPlayer(){
        return player;
    }

    public Color getColor(){
        return color;
    }

    public boolean equals(GeneralMessage other){
        System.out.println("ColorMessage equals still to be implemented.");
        return this.player.equals(((ColorMessage) other).getPlayer()) && this.color.equals(((ColorMessage) other).getColor());
    }


}
