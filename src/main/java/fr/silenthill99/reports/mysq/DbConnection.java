package fr.silenthill99.reports.mysq;

import fr.silenthill99.reports.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private final DbCredentials dbCredentials;
    private Connection conn;
    private final Main main = Main.getInstance();

    public DbConnection(DbCredentials dbCredentials) {
        this.dbCredentials = dbCredentials;
        connect();
    }

    private void connect() {
        try {
            conn = DriverManager.getConnection(dbCredentials.toURI(), dbCredentials.getUser(), dbCredentials.getPassword());
            main.getLogger().info("Connection établie avec la BDD !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconect() throws SQLException {
        if (this.conn != null) {
            if (!this.conn.isClosed()) {
                this.conn.close();
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.conn != null) {
            if (!this.conn.isClosed()) {
                return this.conn;
            }
        }
        connect();
        return this.conn;
    }
}
