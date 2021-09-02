package me.suchtpotenzial.claimsystem.claim;

import me.suchtpotenzial.claimsystem.ClaimSystem;
import me.suchtpotenzial.claimsystem.player.ClaimPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClaimManager {

    private ClaimSystem plugin;

    public List<Claim> claims;

    public ClaimManager(ClaimSystem plugin) {
        this.plugin = plugin;
        this.claims = new ArrayList<>();
    }

    public void visualizeClaim(Claim claim, Player p) {
        for (Location pos : claim.getCuboid().getCornerBlocks()) {
           p.sendBlockChange(pos, Material.SEA_LANTERN.createBlockData());
        }


        //Block wieder verschwinden lassen
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Location pos : claim.getCuboid().getCornerBlocks()) {
                pos.getBlock().getState().update();
            }
        },20*10 /*5 kann geändert werden*/);
    }

    public void visualizeClaimPlayerAuswahl(ClaimPlayer claimPlayer) {
        Cuboid c = new Cuboid(claimPlayer.getL1(),claimPlayer.getL2());

        for (Location pos : c.getCornerBlocks()) {
            claimPlayer.getPlayer().sendBlockChange(pos, Material.EMERALD_BLOCK.createBlockData());
        }


        //Block wieder verschwinden lassen
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (claimPlayer.getPlayer().isSneaking()) {
                visualizeClaimPlayerAuswahl(claimPlayer);
                return;
            }
            for (Location pos : c.getCornerBlocks()) {
                pos.getBlock().getState().update();
                plugin.getPlayerManager().removeVisualizingAuswahl(claimPlayer);
            }
            },20*10 /*5 kann geändert werden*/);
    }

    public List<Claim> getClaimsNearPlayer(Player p) {
        List<Claim> claims = new ArrayList<>();
        Location location = p.getLocation();
        int radius = 20;

        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                Claim claim = plugin.getClaimManager().getClaimByLocation(new Location(location.getWorld(),x,location.getY(),z));
                if (claim != null && !claims.contains(claim)) claims.add(claim);
            }
        }
        return claims;
    }

    public Claim getClaimByLocation(Location l) {
        for (Claim all : claims) {
            if (all.getCuboid().contains(l)) return all;
        }
        return null;
    }

    public boolean isCuboidOverlapingWithOtherOne(Cuboid cuboid) {
        for (Claim all : claims) {
            if (cuboid.overlaps(all.getCuboid()) || cuboid.contains(all.getCuboid())) {
                return true;
            }
        }
        return false;
    }

    public void removeAllClaimsOfPlayer(Player p) {
        List<Claim> toRemove = new ArrayList<>();
        for (Claim all : claims) {
            if (all.getOwner().equals(p.getUniqueId())) {
                toRemove.add(all);
            }
        }
        for (Claim claim : toRemove) {
            claims.remove(claim);
        }
    }

    public List<Claim> getClaimsOfPlayer(Player p) {
        List<Claim> playerClaims = new ArrayList<>();
        for (Claim claim : claims) {
            if (claim.getOwner().equals(p.getUniqueId())) playerClaims.add(claim);
        }
        return playerClaims;
    }

    public void trustPlayer(Claim claim, UUID uuid) {
        claim.getTrustedPlayeres().add(uuid);
    }

    public void untrustPlayer(Claim claim, UUID uuid) {
        claim.getTrustedPlayeres().remove(uuid);
    }

    public void newClaim(Player p, Cuboid cuboid) {
        claims.add(new Claim(cuboid,p.getUniqueId(),new ArrayList<>()));
    }

    public void removeClaim(Claim claim) {
        claims.remove(claim);
    }

    public List<Claim> getClaims() {
        return claims;
    }

}
