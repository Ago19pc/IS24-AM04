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
}
