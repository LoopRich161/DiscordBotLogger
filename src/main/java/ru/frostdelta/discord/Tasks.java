package ru.frostdelta.discord;

import java.util.HashMap;

public enum  Tasks  {
    COMMAND("Command"), SPAWN("Spawn"), UNKNOWN("Unknown action");

    private static final HashMap<String, Tasks> actions = new HashMap<>();

    static {
        for (Tasks ac : Tasks.values()) {
            actions.put(ac.action, ac);
        }
    }

    private final String action;

    private Tasks(String action) {
        this.action = action;
    }

    public String getActionName() {
        return action;
    }

    public static Tasks getAction(String name) {
        return actions.getOrDefault(name, Tasks.UNKNOWN);
    }

    public static boolean contains(String name) {
        return actions.containsKey(name);
    }
}