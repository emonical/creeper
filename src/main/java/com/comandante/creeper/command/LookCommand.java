
package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LookCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("l", "look");
    final static String description = "Examine your surroundings.";

    public LookCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() == 1) {
                currentRoomLogic();
                return;
            }
            originalMessageParts.remove(0);
            String target = Joiner.on(" ").join(originalMessageParts);
            //Players
            Set<String> presentPlayerIds = currentRoom.getPresentPlayerIds();
            for (String presentPlayerId : presentPlayerIds) {
                Player presentPlayer = gameManager.getPlayerManager().getPlayer(presentPlayerId);
                if (presentPlayer.getPlayerName().equals(target)) {
                    write(gameManager.getLookString(presentPlayer) + "\r\n");
                }
            }
            Set<String> npcIds = currentRoom.getNpcIds();
            for (String npcId : npcIds) {
                Npc currentNpc = gameManager.getEntityManager().getNpcEntity(npcId);
                if (currentNpc.getValidTriggers().contains(target)) {
                    write(gameManager.getLookString(currentNpc) + "\r\n");
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}