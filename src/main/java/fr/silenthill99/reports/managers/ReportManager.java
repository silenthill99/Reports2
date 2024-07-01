package fr.silenthill99.reports.managers;

import fr.silenthill99.reports.Main;
import fr.silenthill99.reports.inventory.InventoryManager;
import fr.silenthill99.reports.inventory.InventoryType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportManager {
    private final Main main;
    private final Connection conn;

    public ReportManager() {
        this.main = Main.getInstance();
        try {
            this.conn = main.getManager().getDbConnection().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addReport(Player player, Player target, String raison) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO reports (plaignant, " +
                    "accuse, raison) VALUES (?, ?, ?)");
            statement.setString(1, player.getName());
            statement.setString(2, target.getName());
            statement.setString(3, raison);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showReport(Player player, Player target) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM reports WHERE accuse = ? ORDER BY inter_id ASC");
            statement.setString(1, target.getName());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                List<Player> modos = new ArrayList<>();
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.hasPermission("reports.inter")) {
                        modos.add(players);
                        Player modo = modos.get(new Random().nextInt(modos.size()));
                        players.sendMessage(ChatColor.GOLD + "-----------------Inter n°" + rs.getInt("inter_id") + "------------------");
                        players.sendMessage(ChatColor.GOLD + "Plaignant : " + player.getName());
                        players.sendMessage(ChatColor.GOLD + "Accusé : " + target.getName());
                        players.sendMessage(ChatColor.GOLD + "Raison : " + rs.getString("raison"));
                        players.sendMessage(ChatColor.GOLD + "Staff : " + modo.getName());
                        players.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void openInter(Player player, int id, String[] args) {
        try {
            PreparedStatement sts = conn.prepareStatement("SELECT * FROM reports WHERE inter_id = ?");
            sts.setInt(1, id);
            ResultSet rs = sts.executeQuery();
            if (rs.next()) {
                OfflinePlayer plaignant = getPlaignant(id);
                OfflinePlayer accuse = getAccuse(id);
                if (args.length == 1) {
                    Bukkit.getScheduler().runTask(main, () -> {
                        try {
                            InventoryManager.openInventory(player, InventoryType.INTER, rs.getInt("inter_id"),
                                    plaignant, accuse);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isReported(Player player) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM reports WHERE accuse = ?");
            statement.setString(1, player.getName());
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeValue(Player player) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM reports WHERE plaignant = ?");
            statement.setString(1, player.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isExist(int id) {
        try {
            PreparedStatement sts = conn.prepareStatement("SELECT * FROM reports WHERE inter_id = ?");
            sts.setInt(1, id);
            ResultSet rs = sts.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Player getPlaignant(int id) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM reports WHERE inter_id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Bukkit.getPlayer(rs.getString("plaignant"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Player getAccuse(int id) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM reports WHERE inter_id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Bukkit.getPlayer(rs.getString("accuse"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
