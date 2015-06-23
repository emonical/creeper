package com.comandante.creeper.command;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.CoolDownType;
import com.comandante.creeper.spells.Spell;
import com.comandante.creeper.spells.SpellRegistry;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class CastCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("cast", "c");
    final static String description = "Cast a spell.";
    final static String correctUsage = "cast lightning";

    public CastCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            if (player.isActive(CoolDownType.DEATH)) {
                write("You are dead and can not attack.");
                return;
            }
            if (originalMessageParts.size() == 1) {
                write("Need to specify a spell or optionally, a target player/npc.\r\n");
                return;
            }
            String desiredSpellName = originalMessageParts.get(1);
            Spell spell = SpellRegistry.getSpell(desiredSpellName);
            if (spell == null) {
                write("No spell found with the name: " + desiredSpellName);
                return;
            }
            if (originalMessageParts.size() == 2) {
                if (spell.isAreaSpell()) {
                    spell.attackSpell(currentRoom.getNpcIds(), player);
                    return;
                } else {
                    write("Spell is not an area of attack. Need to specify a target.\r\n");
                    return;
                }
            }
            originalMessageParts.remove(0);
            originalMessageParts.remove(0);
            String target = Joiner.on(" ").join(originalMessageParts);
            for (String npcId : currentRoom.getNpcIds()) {
                Npc npcEntity = entityManager.getNpcEntity(npcId);
                if (npcEntity.getValidTriggers().contains(target)) {
                    spell.attackSpell(Sets.newHashSet(npcId), player);
                    return;
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}