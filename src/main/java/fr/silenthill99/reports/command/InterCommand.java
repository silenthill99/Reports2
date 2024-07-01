package fr.silenthill99.reports.command;

import fr.silenthill99.reports.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InterCommand implements CommandExecutor {

    private final Main main = Main.getInstance();

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
                return;
            }
            if(args.length == 2 && args[1].equalsIgnoreCase("close")) {
                Player plaignant = main.getReportManager().getPlaignant(id);
                Player accuse = main.getReportManager().getAccuse(id);
                main.getReportManager().removeValue(player);
                Bukkit.getScheduler().runTask(main, () -> {
                    plaignant.teleport(main.getLocManager().getTeleport(plaignant));
                    accuse.teleport(main.getLocManager().getTeleport(accuse));
                    Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                        main.getLocManager().removeValue(plaignant);
                        main.getLocManager().removeValue(accuse);
                    });
                });
            } else if (args.length == 2) {
                player.sendMessage(ChatColor.RED + args[1] + " n'est pas un argument valable !");
                return;
            }
            main.getReportManager().openInter(player, id, args);
        });
        return true;
    }
}
