package ru.looprich.discordlogger;

import net.dv8tion.jda.core.entities.User;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.logging.Logger;

public class Network {

    private static final long autoReconnect = 28700L;

    private final String INSERT_QUERY, SELECT_QUERY_PLAYER, SELECT_QUERY_USER, CREATE_DB;
    private final String url, user, password;
    private Connection connection;
    private long lastExecute = 0L;
    private Logger logger;

    Network(Logger logger, String url, String user, String password) {
        this.logger = logger;
        this.url = url;
        this.user = user;
        this.password = password;

        this.CREATE_DB = "CREATE TABLE IF NOT EXISTS `DiscordLogger` (player varchar(200),playerUUID varchar(200),  discord varchar(200), code varchar(200)) CHARACTER SET utf8 COLLATE utf8_general_ci;";
        this.INSERT_QUERY = "INSERT INTO `DiscordLogger` (player, playerUUID, discord, code) VALUES (?, ?, ?, ?);";
        this.SELECT_QUERY_PLAYER = "SELECT * FROM `DiscordLogger` WHERE `player` = ? ;";
        this.SELECT_QUERY_USER = "SELECT * FROM `DiscordLogger` WHERE `discord` = ? ;";
    }


    public void createDB() {
        this.connection = openConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(CREATE_DB)) {
                preparedStatement.executeUpdate();
                flushLastExecute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void verifyPlayer(Player player, User user, String code) {
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
                logger.severe("Error verifyPlayer.");
                e.printStackTrace();
            }
        }
    }

    public boolean existPlayer(Player player) {
        this.connection = getConnection();
        if (this.connection != null) {
            try (PreparedStatement preparedStatement = this.connection.prepareStatement(SELECT_QUERY_PLAYER)) {
                preparedStatement.setString(1, player.getName());
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        String code = rs.getString("code");
                        if (code != null) return true;
                    }
                }
            } catch (SQLException ex) {
                logger.severe("Error existPlayer.");
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
                        String code = rs.getString("code");
                        if (code != null) return true;
                    }
                }
            } catch (SQLException ex) {
                logger.severe("Error existUser.");
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

