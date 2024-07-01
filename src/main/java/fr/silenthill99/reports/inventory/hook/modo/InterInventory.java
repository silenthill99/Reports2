package fr.silenthill99.reports.inventory.hook.modo;

import fr.silenthill99.reports.ItemBuilder;
import fr.silenthill99.reports.Main;
import fr.silenthill99.reports.inventory.AbstractInventory;
import fr.silenthill99.reports.inventory.holder.modo.InterHolder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InterInventory extends AbstractInventory<InterHolder> {
    private final Main main;
    public InterInventory() {
        super(InterHolder.class);
        main = Main.getInstance();
    }

    @Override
    public void openInventory(Player player, Object... args) {
        int inter = (int) args[0];
        OfflinePlayer plaignant = (OfflinePlayer) args[1];
        OfflinePlayer accuse = (OfflinePlayer) args[2];

        InterHolder holder = new InterHolder(inter, plaignant, accuse);

        Inventory inv = createInventory(holder, 27, "Inter : " + inter);
        int slot = 0;
        for (Salles salles : Salles.values()) {
            holder.salles.put(slot, salles);
            inv.setItem(slot++, new ItemBuilder(Material.GREEN_TERRACOTTA).setName(ChatColor.GREEN + salles.getName())
                    .toItemStack());
        }
        player.openInventory(inv);
    }

    @Override
    public void manageInventory(InventoryClickEvent event, ItemStack current, Player player, InterHolder holder) {
        int inter = holder.getInter();
        OfflinePlayer plaignant = holder.getPlaignant();
        OfflinePlayer accuse = holder.getAccuse();

        Salles salles = holder.salles.get(event.getSlot());

        switch (current.getType()) {
            case GREEN_TERRACOTTA: {
                player.closeInventory();
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.hasPermission("reports.inter")) {
                        players.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " s'occupe de " +
                                "l'interadmin n°" + inter);
                    }
                }
                player.teleport(salles.getLoc());
                if (plaignant.isOnline()) {
                    Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                        main.getLocManager().addLocation(plaignant.getPlayer());
                        Bukkit.getScheduler().runTask(main, () -> plaignant.getPlayer().teleport(salles.getLoc()));
                    });
                }

                if (accuse.isOnline()) {
                    Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                        main.getLocManager().addLocation(accuse.getPlayer());
                        Bukkit.getScheduler().runTask(main, () -> accuse.getPlayer().teleport(salles.getLoc()));
                    });
                }
                break;
            }
        }
    }

    public enum Salles {
        SALLE_1("Salle 1", new Location(Bukkit.getWorld("world"), -35, 70, 191)),
        SALLE_2("Salle 2", new Location(Bukkit.getWorld("world"), -21, 70, 212)),
        SALLE_3("Salle 3", new Location(Bukkit.getWorld("world"), -66, 70,199));

        private final String name;
        private final Location loc;

        Salles(String name, Location loc) {
            this.name = name;
            this.loc = loc;
        }

        public String getName() {
            return name;
        }

        public Location getLoc() {
            return loc;
        }
    }
}
