package me.suchtpotenzial.claimsystem.player;

import me.suchtpotenzial.claimsystem.ClaimSystem;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ClaimPlayer {

    private Player player;
    public Location l1;
    public Location l2;
    public int claimBlocks;


    public ClaimPlayer(final Player player) {
        this.player = player;

    }

    public Player getPlayer() {
        return player;
    }

    /*public double getBalance() {
        return ClaimSystem.getPlugin().getEcon().getMoney(player.getUniqueId());
    }*/

    /*public void withdraw(double value) {
        ClaimSystem.getPlugin().getEcon().takeMoney(player.getUniqueId(),value);
    }*/

    public Location getL1() {
        return l1;
    }

    public Location getL2() {
        return l2;
    }

    public int getClaimBlocks() {
        return claimBlocks;
    }
}
