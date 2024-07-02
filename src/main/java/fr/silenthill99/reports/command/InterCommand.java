package fr.silenthill99.reports.command;

import fr.silenthill99.reports.Main;
import fr.silenthill99.reports.inventory.InventoryManager;
import fr.silenthill99.reports.inventory.InventoryType;
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

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            int id = Integer.parseInt(args[0]);
            Player plaignant = main.getReportManager().getPlaignant(id);
            Player accuse = main.getReportManager().getAccuse(id);
            if (!main.getReportManager().isExist(id)) {
                player.sendMessage(ChatColor.RED + "L'interadmin n°" + id + " n'existe pas.");
                return;
            }
            if(args.length == 2 && args[1].equalsIgnoreCase("close")) {
                main.getReportManager().removeValue(player);
                Bukkit.getScheduler().runTask(main, () -> {
                    plaignant.teleport(main.getLocManager().getTeleport(plaignant));
                    accuse.teleport(main.getLocManager().getTeleport(accuse));
                    Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                        main.getLocManager().removeValue(plaignant);
                        main.getLocManager().removeValue(accuse);
                    });
                });
                return;
            } else if (args.length == 2) {
                player.sendMessage(ChatColor.RED + "\""+ args[1] + "\" n'est pas un argument valable !");
                return;
            }
            Bukkit.getScheduler().runTask(main, () -> InventoryManager.openInventory(player, InventoryType.INTER, id,
                    plaignant, accuse));
        });
        return true;
    }
}
