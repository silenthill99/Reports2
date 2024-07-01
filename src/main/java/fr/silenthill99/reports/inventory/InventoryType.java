package fr.silenthill99.reports.inventory;

import fr.silenthill99.reports.inventory.hook.modo.InterInventory;

import java.util.Arrays;
import java.util.List;

public enum InventoryType {
    INTER(new InterInventory())
    ;
    private final AbstractInventory<?> inv;

    InventoryType(AbstractInventory<?> inv) {
        this.inv = inv;
    }

    public AbstractInventory<?> getInv() {
        return this.inv;
    }

    public static List<InventoryType> toArrayList() {
        return Arrays.asList(values());
    }
}
