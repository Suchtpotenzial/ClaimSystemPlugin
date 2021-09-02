package me.suchtpotenzial.claimsystem.player;

import me.suchtpotenzial.claimsystem.ClaimSystem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private ClaimSystem plugin;

    private List<ClaimPlayer> claimPlayers;
    private List<ClaimPlayer> adminPlayers;
    private List<ClaimPlayer> visualizingAuswahl;

    public PlayerManager(ClaimSystem plugin) {
        this.plugin = plugin;
        this.claimPlayers = new ArrayList<>();
        this.adminPlayers = new ArrayList<>();
        this.visualizingAuswahl = new ArrayList<>();
    }

    public ItemStack getPlayerTool(Player p) {
        for (ItemStack all : p.getInventory().getContents()) {
            if (all.getItemMeta().getDisplayName().equals("§5Claim Tool")) return all;
        }
        return null;
    }

    public void changePlayerAdminMode(ClaimPlayer claimPlayer) {
        if (adminPlayers.contains(claimPlayer)) {
            adminPlayers.remove(claimPlayer);
            claimPlayer.getPlayer().sendMessage(ClaimSystem.PREFIX+"Du bist nun nicht mehr im Admin-Modus!");
            return;
        }
        adminPlayers.add(claimPlayer);
        claimPlayer.getPlayer().sendMessage(ClaimSystem.PREFIX+"Du bist nun im Admin-Modus!");
    }

    public void resetClaimPlayerAuswahl(ClaimPlayer claimPlayer) {
        claimPlayer.l1 = null;
        claimPlayer.l2 = null;
    }

    public boolean hasValidAuswahl(ClaimPlayer claimPlayer) {
        if (claimPlayer.getL1() == null || claimPlayer.getL2() == null) return false;
        return true;
    }

    public ClaimPlayer getClaimPlayerByPlayer(Player p) {
        for (ClaimPlayer all : claimPlayers) {
            if (all.getPlayer().equals(p)) return all;
        }
        return null;
    }

    public void setVisualizingAuswahl(ClaimPlayer claimPlayer) {
        this.visualizingAuswahl.add(claimPlayer);
    }

    public void removeVisualizingAuswahl(ClaimPlayer claimPlayer) {
        if (this.visualizingAuswahl.contains(claimPlayer)) {
            this.visualizingAuswahl.remove(claimPlayer);
        }
    }

    public List<ClaimPlayer> getClaimPlayers() {
        return claimPlayers;
    }

    public List<ClaimPlayer> getAdminPlayers() {
        return adminPlayers;
    }

    public List<ClaimPlayer> getVisualizingAuswahl() {
        return visualizingAuswahl;
    }
}
