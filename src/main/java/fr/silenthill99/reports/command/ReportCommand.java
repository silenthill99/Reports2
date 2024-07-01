package fr.silenthill99.reports.command;

import fr.silenthill99.reports.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportCommand implements CommandExecutor {
    private final Main main;
    private final Connection conn;

    public ReportCommand() {
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
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(args.length < 2) {
            player.sendMessage(ChatColor.RED + "/report <joueur> <message>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Component.text(ChatColor.RED + "Ce joueur n'est pas connecté ou n'existe pas !"));
            return false;
        }

        StringBuilder raison = new StringBuilder();

        for(int i = 1; i < args.length; i++) {
            raison.append(args[i]).append(" ");
        }

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (main.getReportManager().isReported(target)) {
                player.sendMessage(ChatColor.RED + "Ce joueur est déjà signalé pour une autre infraction. Merci de " +
                        "réessayer plus tard ou d'ouvrir un ticket sur le discord !");
                return;
            }
            player.sendMessage(ChatColor.AQUA+ "Joueur signalé avec succès !");
            main.getReportManager().addReport(player, target, raison.toString());
            main.getReportManager().showReport(player, target);
        });
        return false;
    }

}
