package fr.silenthill99.reports.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("DataFlowIssue")
public class SilenthillHolder implements InventoryHolder {
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
