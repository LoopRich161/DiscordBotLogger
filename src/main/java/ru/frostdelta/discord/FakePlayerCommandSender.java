package ru.frostdelta.discord;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class FakePlayerCommandSender implements CommandSender {

    private OfflinePlayer offlinePlayer;
    private Player player;
    private boolean isOnline;

    public FakePlayerCommandSender(String name){
        offlinePlayer = Bukkit.getOfflinePlayer(name);
        player = offlinePlayer.getPlayer();
        isOnline = player != null;
    }

    @Override
    public void sendMessage(String message) {
        if(isOnline){
            player.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        if(isOnline){
            for(String message : messages){
                player.sendMessage(message);
            }
        }
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public String getName() {
        if(isOnline){
            return player.getName();
        }
        return offlinePlayer.getName();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return false;
    }

    @Override
    public boolean hasPermission(String name) {
        return false;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return false;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {

    }

    @Override
    public void recalculatePermissions() {

    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public boolean isOp() {
        if(isOnline){
            return player.isOp();
        }else return offlinePlayer.isOp();
    }

    @Override
    public void setOp(boolean value) {
        if(isOnline){
            player.setOp(value);
        }else offlinePlayer.setOp(value);
    }
}
