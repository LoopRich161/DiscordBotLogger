package ru.frostdelta.discord.fake;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.frostdelta.discord.Util;
import ru.looprich.discordlogger.module.DiscordBot;

import java.util.Set;

public class FakeConsoleSender implements ConsoleCommandSender {

    @Override
    public void sendMessage(@NotNull String s) {
        DiscordBot.sendServerResponse(Util.removeCodeColors(s));
    }

    @Override
    public void sendMessage(@NotNull String[] strings) {
        DiscordBot.sendServerResponse(Util.removeCodeColors(strings));
    }

    @Override
    public @NotNull Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public @NotNull String getName() {
        return Bukkit.getConsoleSender().getName();
    }

    @Override
    public boolean isConversing() {
        return Bukkit.getConsoleSender().isConversing();
    }

    @Override
    public void acceptConversationInput(@NotNull String s) {
        Bukkit.getConsoleSender().acceptConversationInput(s);
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        return  Bukkit.getConsoleSender().beginConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
        Bukkit.getConsoleSender().abandonConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
        Bukkit.getConsoleSender().abandonConversation(conversation, conversationAbandonedEvent);
    }

    @Override
    public void sendRawMessage(@NotNull String s) {
        Bukkit.getConsoleSender().sendRawMessage(s);
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        return  Bukkit.getConsoleSender().isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        return  Bukkit.getConsoleSender().isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(@NotNull String s) {
        return  Bukkit.getConsoleSender().hasPermission(s);
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return  Bukkit.getConsoleSender().hasPermission(permission);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
        return  Bukkit.getConsoleSender().addAttachment(plugin, s, b);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return  Bukkit.getConsoleSender().addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        return  Bukkit.getConsoleSender().addAttachment(plugin, s, b, i);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
        return  Bukkit.getConsoleSender().addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
        Bukkit.getConsoleSender().removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        Bukkit.getConsoleSender().recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public boolean isOp() {
        return  Bukkit.getConsoleSender().isOp();
    }

    @Override
    public void setOp(boolean b) {
        Bukkit.getConsoleSender().setOp(b);
    }
}
