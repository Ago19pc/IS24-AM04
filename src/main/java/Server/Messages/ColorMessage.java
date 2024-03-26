package main.java.Server.Messages;

import main.java.Server.Enums.Color;
import main.java.Server.Player.Player;

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
