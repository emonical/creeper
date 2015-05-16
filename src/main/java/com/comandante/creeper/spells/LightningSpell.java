package com.comandante.creeper.spells;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.comandante.creeper.server.Color.BOLD_ON;

public class LightningSpell extends Spell {

    private final static String NAME = BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + " bolt";
    private final static String DESCRIPTION = "A powerful bolt of lightning.";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"lightning", "lightning bolt", NAME}
    ));
    private final static Stats attackStats = new StatsBuilder()
            .setStrength(180)
            .setWeaponRatingMax(60)
            .setWeaponRatingMin(50)
            .setNumberOfWeaponRolls(4)
            .createStats();

    private final static List<String> attackMessages = Lists.newArrayList("a broad stroke of " + BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + " bolts across the sky");
    private static int manaCost = 60;

    public LightningSpell(GameManager gameManager) {
        super(gameManager, validTriggers, manaCost, attackStats, attackMessages, DESCRIPTION, NAME);
    }
}