package ru.frostdelta.discord;

import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import ru.looprich.discordlogger.DiscordLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Util {

    public static String buildCommand(String[] args) {
        StringBuilder cmd = new StringBuilder();
        for (String arg : args) {
            cmd.append(arg).append(" ");
        }
        return cmd.toString();
    }


    public static String getPermission() {
        return DiscordLogger.getInstance().getConfig().getString("admin-permission");
    }

    public static boolean checkContains(String command) {
        for (String cmd : getIgnoredCmds()) {
            if (command.contains(cmd)) return true;
        }
        return false;
    }

    public static List<String> getIgnoredCmds() {
        return DiscordLogger.getInstance().getConfig().getStringList("ignored-commands");
    }

    public static String removeCodeColors(String message) {
        return ChatColor.stripColor(message);
    }

    public static List<String> removeCodeColors(String[] messages) {
        List<String> message = new ArrayList<>();
        for (String msg : messages)
            message.add(ChatColor.stripColor(msg));
        return message;
    }

    public static String cancelFormatMessage(String message) {
        return MarkdownSanitizer.escape(message);
    }

    public static Location getLocationOfflinePlayer(UUID uuid) {
//        File playerData = null;
//        World worldPlayer = null;
//        for (World world : Bukkit.getServer().getWorlds()) {
//            File tempPlayerdata = new File(world.getWorldFolder(), "playerdata");
//            for (File playerd : tempPlayerdata.listFiles()) {
//                if (playerd.getName().startsWith(uuid.toString())) {
//                    playerData = playerd;
//                    worldPlayer = world;
//                    break;
//                }
//            }
//        }
//
//
//        NBTInputStream NBTIStream = null;
//        try {
//            NBTIStream = new NBTInputStream(new FileInputStream(playerData));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        CompoundTag rootCompoundTag = null;
//        try {
//            rootCompoundTag = (CompoundTag) NBTIStream.readTag();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ListTag position = (ListTag) rootCompoundTag.getValue().get("Pos");
//        ListTag rotation = (ListTag) rootCompoundTag.getValue().get("Rotation");
        //юзать вместо Pos и Rotation AABB и брать значения от 0 до 2 для позиции, а яв и питч ставить левые
        //System.out.println("sizePos: "+position.getValue().size()+", array: "+Arrays.toString(position.getValue().toArray()));
        //System.out.println("sizeRotation: "+rotation.getValue().size()+", array: "+Arrays.toString(rotation.getValue().toArray()));
//        double x = (double) position.getValue().get(0).getValue();
//        double y = (double) position.getValue().get(1).getValue();
//        double z = (double) position.getValue().get(2).getValue();
//        float yaw = (float) rotation.getValue().get(0).getValue();
//        float pitch = (float) rotation.getValue().get(1).getValue();
        return new Location(Bukkit.getWorlds().get(0), 9, 12, 23, 65, 123);
    }

    public static Location getLocationOfflinePlayer(String playerName) {
//        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
//        File playerData = null;
//        World worldPlayer = null;
//        for (World world : Bukkit.getWorlds()) {
//            File tempPlayerdata = new File(world.getWorldFolder(), "playerdata");
//            for (File playerd : tempPlayerdata.listFiles()) {
//                if (playerd.getName().startsWith(uuid.toString())) {
//                    playerData = playerd;
//                    worldPlayer = world;
//                    break;
//                }
//            }
//        }
//
//        NBTInputStream NBTIStream = null;
//        try {
//            NBTIStream = new NBTInputStream(new FileInputStream(playerData));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        CompoundTag rootCompoundTag = null;
//        try {
//            rootCompoundTag = (CompoundTag) NBTIStream.readTag();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ListTag position = (ListTag) rootCompoundTag.getValue().get("Pos");
//        ListTag rotation = (ListTag) rootCompoundTag.getValue().get("Rotation");
//
//        double x = (double) position.getValue().get(0).getValue();
//        double y = (double) position.getValue().get(1).getValue();
//        double z = (double) position.getValue().get(2).getValue();
//        float yaw = (float) rotation.getValue().get(0).getValue();
//        float pitch = (float) rotation.getValue().get(1).getValue();
        return new Location(Bukkit.getWorlds().get(0), 9, 12, 23, 65, 123);
    }

    public static boolean isDate(String date) {
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
        try {
            formatForDateNow.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
