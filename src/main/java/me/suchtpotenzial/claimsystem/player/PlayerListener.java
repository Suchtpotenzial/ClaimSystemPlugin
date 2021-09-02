package me.suchtpotenzial.claimsystem.player;

import me.suchtpotenzial.claimsystem.ClaimSystem;
import me.suchtpotenzial.claimsystem.claim.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

import java.util.UUID;

public class PlayerListener implements Listener {

    private ClaimSystem plugin;

    public PlayerListener(ClaimSystem plugin) {
        this.plugin = plugin;
    }

    //Linksklick -> Position 1; Rechtsklick -> Position 2
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getItem() == null || e.getItem().getType().equals(Material.AIR)) return;
        if (! e.getItem().getItemMeta().getDisplayName().equals("§5Claim Tool")) return;
        ClaimPlayer claimPlayer = plugin.getPlayerManager().getClaimPlayerByPlayer(e.getPlayer());

        if (plugin.getPlayerManager().getVisualizingAuswahl().contains(claimPlayer)) {
            claimPlayer.getPlayer().sendMessage(plugin.PREFIX+"Deine Auswahl wird derzeit visualisiert, weshalb du diese gerade nicht neu setzten kannst!");
            return;
        }

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            claimPlayer.l1 = e.getClickedBlock().getLocation();
            e.getPlayer().sendMessage(ClaimSystem.PREFIX+"Du hast die Ecke 1 der aktuellen Auswahl neugesetzt!");
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            claimPlayer.l2 = e.getClickedBlock().getLocation();
            e.getPlayer().sendMessage(ClaimSystem.PREFIX+"Du hast die Ecke 2 der aktuellen Auswahl neugesetzt!");
            return;
        }
    }

    //Wenn Spieler das Tool in der Hand hält wird die aktuelle Auswahl angezeigt
    //@EventHandler
    public void onHold(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();

        //If the player is sneaking the claims nearby are shown
        if (p.isSneaking()) {
            if (e.getPlayer().getInventory().getItemInMainHand() == null || e.getPlayer().getInventory().getItemInOffHand() == null || !e.getPlayer().getInventory().getItemInMainHand().hasItemMeta() || !e.getPlayer().getInventory().getItemInOffHand().hasItemMeta()) return;
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§5Claim Tool") || e.getPlayer().getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals("§5Claim Tool")) {

                //TODO DEBUG entfernen
                Bukkit.broadcastMessage("Sneaked and Tool held");



                plugin.getClaimManager().visualizeClaimPlayerAuswahl(plugin.getPlayerManager().getClaimPlayerByPlayer(p));
            }
            return;
        }


        //If the player is not sneaking the current auswahl is shown
        //if (plugin.getPlayerManager().getPlayerTool(p) == null) return;
        //if (p.getInventory().getItem(e.getNewSlot()) == null) return;
        //if (p.getInventory().getItem(e.getNewSlot()).getItemMeta().getDisplayName().equals("§5Claim Tool") || p.getInventory().getItem(e.getNewSlot()).getItemMeta().getDisplayName().equals("§5Claim Tool")) {
        //    plugin.getClaimManager().visualizeClaimPlayerAuswahl(plugin.getPlayerManager().getClaimPlayerByPlayer(p));
        //}
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onSneak(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();
        if (event.isSneaking()) {
            if ((p.getInventory().getItemInMainHand().hasItemMeta() && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§5Claim Tool")) || (p.getInventory().getItemInOffHand().hasItemMeta() && p.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals("§5Claim Tool"))) {
                ClaimPlayer claimPlayer = plugin.getPlayerManager().getClaimPlayerByPlayer(p);
                if (!plugin.getPlayerManager().hasValidAuswahl(claimPlayer)) {
                    p.sendMessage(plugin.PREFIX+"Du hast noch keine vollständige Auswahl!");
                    return;
                }
                plugin.getClaimManager().visualizeClaimPlayerAuswahl(claimPlayer);
                plugin.getPlayerManager().setVisualizingAuswahl(claimPlayer);
            }
        }
    }

    //Check if player is placing blocks in someone elses claim
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Claim c = plugin.getClaimManager().getClaimByLocation(e.getBlock().getLocation());
        if (c == null) return;

        if (!c.getOwner().equals(p.getUniqueId()) && !c.getTrustedPlayeres().contains(p.getUniqueId()) && !plugin.getPlayerManager().getAdminPlayers().contains(plugin.getPlayerManager().getClaimPlayerByPlayer(p))) {
            e.setCancelled(true);
            p.sendMessage(ClaimSystem.PREFIX+"Du kannst keine Blöcke in fremden Claims plazieren!");
            return;
        }
    }

    //Check if player is breaking blocks in someone elses claim
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Claim c = plugin.getClaimManager().getClaimByLocation(e.getBlock().getLocation());
        if (c == null) return;

        if (!c.getOwner().equals(p.getUniqueId()) && !c.getTrustedPlayeres().contains(p.getUniqueId()) && !plugin.getPlayerManager().getAdminPlayers().contains(plugin.getPlayerManager().getClaimPlayerByPlayer(p))) {
            e.setCancelled(true);
            p.sendMessage(ClaimSystem.PREFIX+"Du kannst keine Blöcke in fremden Claims abbauen!");
            return;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        //Falls der Spieler noch kein Claimplayer ist -,-
        if (plugin.getPlayerManager().getClaimPlayerByPlayer(e.getPlayer()) == null) {
            plugin.getPlayerManager().getClaimPlayers().add(new ClaimPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

    }
}
