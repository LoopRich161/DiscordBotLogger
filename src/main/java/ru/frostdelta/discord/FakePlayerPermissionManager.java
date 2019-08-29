package ru.frostdelta.discord;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class FakePlayerPermissionManager {

    private static Permission perms = null;
    private static Chat chat = null;

    public static boolean load() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        RegisteredServiceProvider<Permission> rspp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rspp.getProvider();

        return chat != null && perms != null;
    }

    public static Chat getChat() {
        return chat;
    }

    public static Permission getFakePlayerPermissions() {
        return perms;
    }
}
