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
import ru.looprich.discordlogger.modules.DiscordBot;

import java.util.Set;

public class FakePlayerCommandSender implements CommandSender {

    private OfflinePlayer offlinePlayer;
    private Player player;
    private boolean isOnline;
    private net.milkbowl.vault.permission.Permission permission;

    public FakePlayerCommandSender(String name){
        offlinePlayer = Bukkit.getOfflinePlayer(name);
        player = offlinePlayer.getPlayer();
        isOnline = player != null;
        permission = FakePlayerPermissionManager.getFakePlayerPermissions();
        //Bukkit.broadcastMessage(String.valueOf(npc.isSpawned()));
        //Bukkit.broadcastMessage(Util.getFakePlayerNPC(name).getEntity().toString());
    }

    @Override
    public void sendMessage(String message) {
        if(isOnline){
            player.sendMessage(message);
        }else DiscordBot.sendServerResponse(Util.removeCodeColors(message));
    }

    @Override
    public void sendMessage(String[] messages) {
        if(isOnline){
            for(String message : messages){
                player.sendMessage(message);
            }
        }else DiscordBot.sendServerResponse(Util.removeCodeColors(messages));
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
        if(isOnline){
            return player.hasPermission(name);
        }else return permission.playerHas(Bukkit.getServer().getWorlds().get(0).getName(), offlinePlayer, name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        if(isOnline){
            return player.hasPermission(perm.getName());
        }else return permission.playerHas(Bukkit.getServer().getWorlds().get(0).getName(), offlinePlayer, perm.getName());
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
       //Empty
    }

    @Override
    public void recalculatePermissions() {
        //Empty
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        if(isOnline){
            return player.getEffectivePermissions();
        }else return null;
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

    public boolean isOnline() {
        return isOnline;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    public Player getPlayer(){
        return player;
    }

}
