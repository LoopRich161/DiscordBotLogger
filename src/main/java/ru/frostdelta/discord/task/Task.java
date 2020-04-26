package ru.frostdelta.discord.task;

import java.util.HashMap;

public enum Task {
    COMMAND("Command"), SPAWN("Spawn"), CHAT("Chat"), DISPATCH("Dispatch command"), UNKNOWN("Unknown action");

    private static final HashMap<String, Task> actions = new HashMap<>();

    static {
        for (Task ac : Task.values()) {
            actions.put(ac.action, ac);
        }
    }

    private final String action;

    Task(String action) {
        this.action = action;
    }

    public String getActionName() {
        return action;
    }

    public static Task getAction(String name) {
        return actions.getOrDefault(name, Task.UNKNOWN);
    }

    public static boolean contains(String name) {
        return actions.containsKey(name);
    }
}