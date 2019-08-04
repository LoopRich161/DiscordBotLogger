package ru.looprich.discordlogger;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.logging.Logger;

public class Network {

    private static final long autoReconnect = 28700L;

    private final String INSERT_QUERY, SELECT_QUERY_PLAYER, SELECT_QUERY_USER, CREATE_DB, DELETE_QUERY;
    private final String url, user, password;
    private Connection connection;
    private long lastExecute = 0L;
    private Logger logger;

    Network(Logger logger, String url, String user, String password) {
        this.logger = logger;
        this.url = url;
        this.user = user;
        this.password = password;

        this.CREATE_DB = "CREATE TABLE IF NOT EXISTS `DiscordLogger` (player varchar(200), playerUUID varchar(200),  discord varchar(200), code varchar(200)) CHARACTER SET utf8 COLLATE utf8_general_ci;";
        this.INSERT_QUERY = "INSERT INTO `DiscordLogger` (player, playerUUID, discord, code) VALUES (?, ?, ?, ?);";
        this.SELECT_QUERY_PLAYER = "SELECT * FROM `DiscordLogger` WHERE `player` = ? ;";
        this.SELECT_QUERY_USER = "SELECT * FROM `DiscordLogger` WHERE `discord` = ? ;";
        this.DELETE_QUERY = "DELETE FROM `DiscordLogger` WHERE `player` = ? ;";
    }


    public void createDB() {
        this.connection = openConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(CREATE_DB)) {
                preparedStatement.executeUpdate();
                flushLastExecute();
            } catch (SQLException e) {
                logger.severe("Error createBD");
                e.printStackTrace();
            }
        }

    }


    public String getAccountMinecraftName(User user) {
        this.connection = getConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(SELECT_QUERY_USER)) {
                preparedStatement.setString(1, user.getAsTag());
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("player");
                    }
                }
            } catch (SQLException ex) {
                logger.severe("Error getAccountMinecraftName");
                ex.printStackTrace();
            }
        }
        flushLastExecute();
        return null;
    }

    public void authentication(Player player, User user, String code) {
        this.connection = getConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(INSERT_QUERY)) {
                preparedStatement.setString(1, player.getName());
                preparedStatement.setString(2, player.getUniqueId().toString());
                preparedStatement.setString(3, user.getAsTag());
                preparedStatement.setString(4, code);
                preparedStatement.executeUpdate();
                flushLastExecute();
            } catch (SQLException e) {
                logger.severe("Error authentication");
                e.printStackTrace();
            }
        }
    }

    public void deauthentication(Player player) {
        this.connection = getConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(DELETE_QUERY)) {
                preparedStatement.setString(1, player.getName());
                preparedStatement.executeUpdate();
                flushLastExecute();
            } catch (SQLException e) {
                logger.severe("Error deauthentication");
                e.printStackTrace();
            }
        }
    }

    public boolean existPlayer(Player player, User user) {
        this.connection = getConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(SELECT_QUERY_PLAYER)) {
                preparedStatement.setString(1, player.getName());
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        String userName = rs.getString("discord");
                        if (userName.equalsIgnoreCase(user.getAsTag())) return true;
                    }
                }
            } catch (SQLException ex) {
                logger.severe("Error existPlayer");
                ex.printStackTrace();
            }
        }
        flushLastExecute();
        return false;
    }

    public boolean existPlayer(Player player) {
        this.connection = getConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(SELECT_QUERY_PLAYER)) {
                preparedStatement.setString(1, player.getName());
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        String userName = rs.getString("discord");
                        if (userName != null) return true;
                    }
                }
            } catch (SQLException ex) {
                logger.severe("Error existPlayer");
                ex.printStackTrace();
            }
        }
        flushLastExecute();
        return false;
    }

    public boolean existUser(User user, Player player) {
        this.connection = getConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(SELECT_QUERY_USER)) {
                preparedStatement.setString(1, user.getAsTag());
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        String playerName = rs.getString("player");
                        if (playerName.equalsIgnoreCase(player.getName())) return true;
                    }
                }
            } catch (SQLException ex) {
                logger.severe("Error existUser");
                ex.printStackTrace();
            }
        }
        flushLastExecute();
        return false;
    }

    public boolean existUser(User user) {
        this.connection = getConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(SELECT_QUERY_USER)) {
                preparedStatement.setString(1, user.getAsTag());
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        String playerName = rs.getString("player");
                        if (playerName != null) return true;
                    }
                }
            } catch (SQLException ex) {
                logger.severe("Error existUser");
                ex.printStackTrace();
            }
        }
        flushLastExecute();
        return false;
    }

    //others
    public boolean init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            this.logger.severe("Could not found mysql driver");
            return false;
        }

        logger.info("Testing connection...");
        if (testConnection()) {
            logger.info("> SUCCESS");
            return true;
        } else {
            logger.info("> FAILED");
            return false;
        }
    }

    private boolean testConnection() {
        this.connection = getConnection();
        try (Statement statement = this.connection.createStatement()) {
            statement.execute("SELECT version();");
            statement.close();
            flushLastExecute();
            return true;
        } catch (SQLException ex) {
            this.logger.severe("Error test connection to database.");
            ex.printStackTrace();
            return false;
        }
    }

    private Connection getConnection() {
        long timeStamp = System.currentTimeMillis();
        try {
            if ((this.connection == null) || (this.connection.isClosed()) || (timeStamp - this.lastExecute >= autoReconnect)) {
                reconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    private Connection openConnection() {
        try {
            return DriverManager.getConnection(this.url + "?useUnicode=true&characterEncoding=UTF-8", this.user, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void reconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.connection = openConnection();
        flushLastExecute();
    }

    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                this.connection = null;
            }
        }
    }

    private void flushLastExecute() {
        this.lastExecute = System.currentTimeMillis();
    }

}

