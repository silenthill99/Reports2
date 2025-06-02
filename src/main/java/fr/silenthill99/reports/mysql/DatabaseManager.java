package fr.silenthill99.reports.mysql;

import java.sql.SQLException;

public class DatabaseManager {
    private final DbConnection dbConnection;

    public DatabaseManager() {
        this.dbConnection = new DbConnection(new DbCredentials("minecraft3080.omgserv.com",
                "minecraft_235640", "Mylene.10", "minecraft_235640", 3306));
    }

    public DbConnection getDbConnection() {
        return dbConnection;
    }

    public void close() {
        try {
            dbConnection.disconect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
