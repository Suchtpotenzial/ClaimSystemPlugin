package me.suchtpotenzial.claimsystem.claim;

import me.suchtpotenzial.claimsystem.ClaimSystem;
import me.suchtpotenzial.claimsystem.player.ClaimPlayer;
import me.suchtpotenzial.claimsystem.util.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClaimMonitor implements Listener {

    /*
     * -------------
     * --C---S---H--
     * -------------
     * C: Current Chunks
     * B: Shop
     * H: Help
     */

    private ClaimSystem plugin;
    private Inventory monitor;

    public ClaimMonitor(ClaimSystem plugin) {
        this.plugin = plugin;
        this.monitor = Bukkit.createInventory(null,3*9,"§3Claim Monitor");

        //TODO Lores einfügen
        monitor.setItem(11, ItemStackManager.create(Material.GOLDEN_SHOVEL,"§6Deine Claims",1,"TODO"));
        monitor.setItem(13, ItemStackManager.create(Material.GOLDEN_APPLE,"§6Shop",1,"TODO"));
        monitor.setItem(15, ItemStackManager.create(Material.BOOK,"§6Help",1,"TODO"));
    }

    public void open(Player p) {
        p.openInventory(monitor);
        return;
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if(event.getView().getTitle().equals("§3Claim Monitor")) {
            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();

            if (!event.getCurrentItem().hasItemMeta() || event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§6Deine Claims")) {
                Inventory claimInventory = Bukkit.createInventory(null,2*9,"§6"+event.getWhoClicked().getName()+"'s Claims");

                int i = 0;
                for (Claim claim : plugin.getClaimManager().getClaimsOfPlayer(p)) {
                    claimInventory.setItem(i,ItemStackManager.create(Material.PAPER,"Claim Nr."+(i+1),1,"Owner: "+claim.getOwnerName()+";"+claim.getCuboid().toString()+";Freunde: "+claim.getTrustetPlayerNameString()));
                    i++;
                }
                p.openInventory(claimInventory);
                return;
            }
            else if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§6Shop")) {
                //TODO Shop GUI
                return;
            }
            else if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§6Help")) {
                //TODO Help GUI
                return;
            }
            InventoryClickEvent.getHandlerList().unregister(this);
            event.getWhoClicked().closeInventory();
        }
    }

}
