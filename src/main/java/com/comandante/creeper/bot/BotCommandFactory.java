package com.comandante.creeper.bot;

import com.comandante.creeper.bot.commands.*;
import com.google.common.collect.Maps;

import java.util.*;

public class BotCommandFactory {

    private final Map<String, BotCommand> botCommandRegistry = Maps.newHashMap();

    public BotCommandFactory(BotCommandManager botCommandManager) {
        addCommand(new WeatherBotCommand(botCommandManager));
        addCommand(new WhoBotCommand(botCommandManager));
        addCommand(new CheckNorrisBotCommand(botCommandManager));
        addCommand(new DictionaryBotCommand(botCommandManager));
        addCommand(new ImdbBotCommand(botCommandManager));
        addCommand(new ForecastCommand(botCommandManager));
    }

    public BotCommand getCommand(String originalFullCmd) {
        List<String> originalMessageParts = new ArrayList<>(Arrays.asList(originalFullCmd.split(" ")));
        if (originalMessageParts.size() == 0) {
            return null;
        }
        String rootCommand = originalMessageParts.get(0);
        BotCommand botCommand = botCommandRegistry.get(rootCommand);
        if (botCommand != null) {
            botCommand.setOriginalFullCommand(originalFullCmd);
        }
        return botCommand;
    }

    private void addCommand(BotCommand botCommand) {
        for (String trigger: botCommand.getTriggers()) {
            botCommandRegistry.put(trigger, botCommand);
        }
    }
}
