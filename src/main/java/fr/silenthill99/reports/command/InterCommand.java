package fr.silenthill99.reports.command;

import fr.silenthill99.reports.Main;
import fr.silenthill99.reports.inventory.InventoryManager;
import fr.silenthill99.reports.inventory.InventoryType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InterCommand implements CommandExecutor {

    private final Main main;
    private final Connection conn;

    public InterCommand() {
        this.main = Main.getInstance();
        try {
            this.conn = main.getManager().getDbConnection().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s,
                             @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (args.length == 0 || args.length > 2) {
            player.sendMessage(ChatColor.RED + "/inter <numéro> [close]");
            return false;
        }
        int id = Integer.parseInt(args[0]);

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (!main.getReportManager().isExist(id)) {
                player.sendMessage(ChatColor.RED + "L'interadmin n°" + id + " n'existe pas.");
            }
        });

        if(args.length == 2 && args[1].equalsIgnoreCase("close")) {
            Player plaignant = main.getReportManager().getPlaignant(id);
            Player accuse = main.getReportManager().getAccuse(id);
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                main.getReportManager().removeValue(player);
                Bukkit.getScheduler().runTask(main, () -> {
                    plaignant.teleport(main.getLocManager().getTeleport(plaignant));
                    accuse.teleport(main.getLocManager().getTeleport(accuse));
                    Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                        main.getLocManager().removeValue(plaignant);
                        main.getLocManager().removeValue(accuse);
                    });
                });
            });
            return true;
        } else if (args.length == 2) {
            player.sendMessage(ChatColor.RED + args[1] + " n'est pas un argument valable !");
        }

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> main.getReportManager().openInter(player, id, args));

        return true;
    }
}
