package main.java.Messages;

import main.java.Enums.Color;
import main.java.Player.Player;

public class ColorMessage extends GeneralMessage {

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
