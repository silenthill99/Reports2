package fr.silenthill99.reports.managers;

import fr.silenthill99.reports.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationManager {
    private final Main main;
    private final Connection connection;

    public LocationManager() {
        this.main = Main.getInstance();
        try {
            this.connection = main.getManager().getDbConnection().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLocation(Player player) {
        try {
            PreparedStatement sts = connection.prepareStatement("INSERT INTO localisation (pseudo, world_name, pos_x," +
                    " pos_y, pos_z) VALUES (?, ?, ?, ?, ?)");
            sts.setString(1, player.getName());
            sts.setString(2, player.getWorld().getName());
            sts.setDouble(3, player.getLocation().getX());
            sts.setDouble(4, player.getLocation().getY());
            sts.setDouble(5, player.getLocation().getZ());
            sts.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Location getTeleport(Player player) {
        try {
            PreparedStatement sts = connection.prepareStatement("SELECT * FROM localisation WHERE pseudo = ?");
            sts.setString(1, player.getName());
            ResultSet rs = sts.executeQuery();
            if (rs.next()) {
                return new Location(Bukkit.getWorld(rs.getString("world_name")),
                        rs.getDouble("pos_x"), rs.getDouble("pos_y"),
                        rs.getDouble("pos_z"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Location(player.getWorld(), 0, 0, 0);
    }

    public void removeValue(Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM localisation WHERE pseudo = ?");
            statement.setString(1, player.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
