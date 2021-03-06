package com.comandante.creeper.command;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.CoolDownType;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerMovement;
import com.comandante.creeper.player.PlayerRole;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.world.Room;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.*;

public class ShowCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("show");
    final static String description = "Show an item in your inventory.";
    final static String correctUsage = "show <item_name>";

    public ShowCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() <= 1) {
                return;
            }
            originalMessageParts.remove(0);
            String target = Joiner.on(" ").join(originalMessageParts);
            for (Item next : player.getInventory()) {
                for (String s : next.getItemTriggers()) {
                    if (s.equalsIgnoreCase(target)) {
                        writeToRoom(player.getPlayerName() + " whips out " + next.getItemName() + ".\r\n");
                        return;
                    }
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}