package me.suchtpotenzial.claimsystem.util;

import me.suchtpotenzial.claimsystem.ClaimSystem;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class Econ {

    private Economy econ;
    private ClaimSystem plugin;

    public boolean setupEconomy(ClaimSystem plugin) {
        this.plugin = plugin;

        // Check if Vault is present
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) return false;

        // Get the Vault service if it is present
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        // Check if the service is valid
        if (rsp == null) return false;

        // Update current economy handler
        econ = rsp.getProvider();

        // Success
        return true;
    }

    public double getMoney(UUID player) {
        Player p = getPlayer(player);
        // If the player has joined the server before, return their balance
        if (p != null) {
            return econ.getBalance(p);
        }
        return -1.0d;
    }

    @SuppressWarnings("UnusedReturnValue")
    public EconomyResponse addMoney(UUID player, double amt) {
        Player p = getPlayer(player);
        if (p != null) {
            // Add the (safe) balance to the player
            return econ.depositPlayer(p, Math.abs(amt));
        }
        return null;
    }

    @SuppressWarnings("UnusedReturnValue")
    public EconomyResponse takeMoney(UUID player, double amt) {
        Player p = getPlayer(player);
        if (p != null) {
            // Remove the money from the player's balance
            return econ.withdrawPlayer(p, Math.abs(amt));
        }
        return null;
    }

    /**
     * Take money from the player.
     *
     * @param p  Player purchasing.
     * @param cost The cost of the purchase.
     * @return Whether or not the transaction was successful.
     */
    public boolean buy(UUID p, double cost) {
        if (getMoney(p) >= cost) {
            EconomyResponse response = takeMoney(p, cost);
            // Return whether the transaction was completed successfully
            return response != null && response.type == EconomyResponse.ResponseType.SUCCESS;
        }
        return false;
    }

    public String format(double amt) {
        return econ.format(amt);
    }

    public Player getPlayer(UUID id) {
        if (plugin == null) {
            return null;
        }
        return plugin.getServer().getPlayer(id);
    }

}
