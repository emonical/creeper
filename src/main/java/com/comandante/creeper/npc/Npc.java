package com.comandante.creeper.npc;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.model.CreeperEntity;
import com.comandante.creeper.model.Stats;

import static com.comandante.creeper.model.Color.RED;
import static com.comandante.creeper.model.Color.RESET;


public abstract class Npc extends CreeperEntity {

    public void setLastPhraseTimestamp(long lastPhraseTimestamp) {
        this.lastPhraseTimestamp = lastPhraseTimestamp;
    }

    private long lastPhraseTimestamp;
    private final GameManager gameManager;
    private final Integer roomId;
    private final String name;
    private final String colorName;
    private final Stats stats;

    @Override
    public void run() {
        //System.out.println(getName() + " tick...");
    }

    public String getColorName() {
        return colorName;
    }

    protected Npc(GameManager gameManager, Integer roomId, String name, String colorName, long lastPhraseTimestamp, Stats stats) {
        this.gameManager = gameManager;
        this.roomId = roomId;
        this.name = name;
        this.colorName = colorName;
        this.lastPhraseTimestamp = lastPhraseTimestamp;
        this.stats = stats;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public long getLastPhraseTimestamp() {
        return lastPhraseTimestamp;
    }

    public String getName() {
        return name;
    }

    public void npcSay(Integer roomId, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(RED);
        sb.append(name).append(": ").append(message);
        sb.append(RESET);
    }

}
