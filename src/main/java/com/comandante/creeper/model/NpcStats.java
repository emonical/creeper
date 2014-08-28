package com.comandante.creeper.model;

public class NpcStats {
    public final static Stats JOE_NPC = new StatsBuilder()
            .setStrength(5)
            .setWillpower(1)
            .setAim(1)
            .setAgile(1)
            .setArmorRating(5)
            .setMeleSkill(5)
            .setHealth(100)
            .setWeaponRatingMin(5)
            .setWeaponRatingMax(10)
            .setNumberweaponOfRolls(1).createStats();

    public final static Stats ANOTHER_DOUCHER = new StatsBuilder()
            .setStrength(5)
            .setWillpower(1)
            .setAim(1)
            .setAgile(1)
            .setArmorRating(5)
            .setMeleSkill(5)
            .setHealth(100)
            .setWeaponRatingMin(5)
            .setWeaponRatingMax(10)
            .setNumberweaponOfRolls(1).createStats();
}