package me.suchtpotenzial.claimsystem.claim;

import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class Claim {

    private Cuboid cuboid;
    private UUID owner;
    private List<UUID> trustedPlayeres;

    public Claim(Cuboid cuboid, UUID owner, List<UUID> trustedPlayeres) {
        this.cuboid = cuboid;
        this.owner = owner;
        this.trustedPlayeres = trustedPlayeres;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getOwnerName() {
        try {
            return Bukkit.getPlayer(owner).getName();
        } catch (NullPointerException e) {

        }
        return null;
    }

    public List<UUID> getTrustedPlayeres() {
        return trustedPlayeres;
    }

    public String getTrustetPlayerNameString() {
        String s = "";

        if (trustedPlayeres.size() == 0) {
            return "Keine Freunde D:";
        }

        for (int i = 0 ; i < trustedPlayeres.size() ; i++) {
            if (i == 0) {
                s += Bukkit.getPlayer(trustedPlayeres.get(i)).getName();
            } else {
                s += "," + Bukkit.getPlayer(trustedPlayeres.get(i)).getName();
            }
        }
        return s;
    }

    public String getTrustetPlayerString() {
        String s = "";

        if (trustedPlayeres.size() == 0) {
            return "no friends";
        }

        for (int i = 0 ; i < trustedPlayeres.size() ; i++) {
            if (i == 0) {
                s += trustedPlayeres.get(i);
            } else {
                s += "," + trustedPlayeres.get(i);
            }
        }
        return s;
    }
}
