package com.comandante.creeper.world;

import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

public class TimeTracker extends CreeperEntity {

    private static final int NUMBER_OF_MILLISECONDS_IN_A_DAY = 86400000;
    private int currentTick = 0;
    private TimeOfDay currentTimeOfDay = TimeOfDay.MORNING;
    private final GameManager gameManager;

    public TimeTracker(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        TimeOfDay timeOfDay = determineTimeOfDay();
        if (timeOfDay != currentTimeOfDay) {
            announceChange(timeOfDay);
        }
        currentTimeOfDay = timeOfDay;
    }

    private void announceChange(TimeOfDay timeOfDay) {
        Iterator<Map.Entry<String, Player>> players = gameManager.getPlayerManager().getPlayers();
        while (players.hasNext()) {
            Map.Entry<String, Player> next = players.next();
            gameManager.getChannelUtils().write(next.getValue().getPlayerId(), "It is now " + timeOfDay.color + timeOfDay + Color.RESET + ".\r\n", true);
        }
    }

    public TimeOfDay getTimeOfDay() {
        return currentTimeOfDay;
    }

    private TimeOfDay determineTimeOfDay() {
        long milliSecondsSinceMidnight = milliSecondsSinceMidnight();
        if (milliSecondsSinceMidnight <= (NUMBER_OF_MILLISECONDS_IN_A_DAY * .25)) {
            return TimeOfDay.MORNING;
        } else if (milliSecondsSinceMidnight <= (NUMBER_OF_MILLISECONDS_IN_A_DAY * .50)) {
            return TimeOfDay.AFTERNOON;
        } else if (milliSecondsSinceMidnight <= (NUMBER_OF_MILLISECONDS_IN_A_DAY * .75)) {
            return TimeOfDay.EVENING;
        } else {
            return TimeOfDay.NIGHT;
        }
    }

    public enum TimeOfDay {
        MORNING(Color.YELLOW),
        AFTERNOON(Color.GREEN),
        EVENING(Color.RED),
        NIGHT(Color.CYAN);

        public String color;

        TimeOfDay(String color) {
            this.color = color;
        }
    }

    private long milliSecondsSinceMidnight(){
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        return passed;
    }
}
