package me.suchtpotenzial.claimsystem;

import me.suchtpotenzial.claimsystem.claim.ClaimManager;
import me.suchtpotenzial.claimsystem.claim.ClaimMonitor;
import me.suchtpotenzial.claimsystem.cmd.ClaimCMD;
import me.suchtpotenzial.claimsystem.player.PlayerListener;
import me.suchtpotenzial.claimsystem.player.PlayerManager;
import me.suchtpotenzial.claimsystem.util.ConfigHandler;
import me.suchtpotenzial.claimsystem.util.Econ;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ClaimSystem extends JavaPlugin {

    private static ClaimSystem plugin;
    public static final String PREFIX = "§bClaim §8| §7";

    private final Logger log = Logger.getLogger("Minecraft");

    private ClaimManager claimManager;
    private PlayerManager playerManager;
    //private Econ econ;
    private ClaimMonitor claimMonitor;

    @Override
    public void onEnable() {
        this.plugin = this;
        this.claimManager = new ClaimManager(this);
        this.playerManager = new PlayerManager(this);
        //this.econ = new Econ();
        //this.claimMonitor = new ClaimMonitor(this);

        ConfigHandler.saveDefaults(this);
        ConfigHandler.loadEverything(this);
        init();

        /*if (!econ.setupEconomy(this)) {
            System.out.println(PREFIX + "Failed to setup Economy!");
            Bukkit.getServer().shutdown();
            return;
        }*/
    }

    @Override
    public void onDisable() {
        ConfigHandler.writeAllClaimsToFile();
    }

    public void init() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);

        getCommand("claim").setExecutor(new ClaimCMD(this));
    }

    public static ClaimSystem getPlugin() {
        return plugin;
    }

    /*public Econ getEcon() {
        return econ;
    }*/

    public Logger getLog() {
        return log;
    }

    public ClaimManager getClaimManager() {
        return claimManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ClaimMonitor getClaimMonitor() {
        return claimMonitor;
    }
}
