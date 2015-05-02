package com.comandante.creeper.command;

import com.codahale.metrics.Timer;
import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Main;
import com.comandante.creeper.fight.FightResults;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import static com.codahale.metrics.MetricRegistry.name;

public class PickUpCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("pickup", "pick", "p");
    final static String description = "Pick up an item.";
    final static String correctUsage = "pickup <item name>";

    public PickUpCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description,correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            Set<String> itemIds = currentRoom.getItemIds();
            originalMessageParts.remove(0);
            String desiredPickUpItem = Joiner.on(" ").join(originalMessageParts);
            for (String next : itemIds) {
                Item itemEntity = entityManager.getItemEntity(next);
                if (itemEntity.getItemTriggers().contains(desiredPickUpItem)) {
                    gameManager.acquireItem(player, next);
                    String playerName = player.getPlayerName();
                    gameManager.roomSay(currentRoom.getRoomId(), playerName + " picked up " + itemEntity.getItemName(), playerId);
                    return;
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
