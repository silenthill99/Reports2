package fr.silenthill99.reports;

import fr.silenthill99.reports.command.InterCommand;
import fr.silenthill99.reports.command.ReportCommand;
import fr.silenthill99.reports.inventory.InventoryManager;
import fr.silenthill99.reports.managers.LocationManager;
import fr.silenthill99.reports.managers.ReportManager;
import fr.silenthill99.reports.mysq.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }
    private DatabaseManager manager;
    private ReportManager reportManager;
    private LocationManager locManager;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Le plugin est opérationnel !");
        manager = new DatabaseManager();
        commands();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryManager(), this);
        reportManager = new ReportManager();
        locManager = new LocationManager();
    }

    private void commands() {
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("inter").setExecutor(new InterCommand());
    }

    @Override
    public void onDisable() {
        manager.close();
    }

    public DatabaseManager getManager() {
        return manager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public LocationManager getLocManager() {
        return locManager;
    }
}
