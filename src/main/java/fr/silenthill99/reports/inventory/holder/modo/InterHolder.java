package fr.silenthill99.reports.inventory.holder.modo;

import fr.silenthill99.reports.inventory.SilenthillHolder;
import fr.silenthill99.reports.inventory.hook.modo.InterInventory.Salles;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

public class InterHolder extends SilenthillHolder {

    private final int inter;
    private final OfflinePlayer plaignant;
    private final OfflinePlayer accuse;

    public InterHolder(int inter, OfflinePlayer plaignant, OfflinePlayer accuse) {
        this.inter = inter;
        this.plaignant = plaignant;
        this.accuse = accuse;
    }

    public int getInter() {
        return inter;
    }

    public OfflinePlayer getPlaignant() {
        return plaignant;
    }

    public OfflinePlayer getAccuse() {
        return accuse;
    }

    public HashMap<Integer, Salles> salles = new HashMap<>();
}
