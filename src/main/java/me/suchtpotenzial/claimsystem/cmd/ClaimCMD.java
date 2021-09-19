package me.suchtpotenzial.claimsystem.cmd;

import me.suchtpotenzial.claimsystem.ClaimSystem;
import me.suchtpotenzial.claimsystem.claim.Claim;
import me.suchtpotenzial.claimsystem.claim.ClaimTool;
import me.suchtpotenzial.claimsystem.claim.Cuboid;
import me.suchtpotenzial.claimsystem.player.ClaimPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCMD implements CommandExecutor {

    private ClaimSystem plugin;

    public ClaimCMD(ClaimSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player)sender;

        //Info Command
        if (args.length == 0) {

            //TODO Hier Info Commands einfügen
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("tool") || args[0].equalsIgnoreCase("t")) {

                ClaimTool.giveTool(p);
                p.sendMessage(ClaimSystem.PREFIX+"Du hast das Claim Tool bekommen!");

                return true;
            }
            if (args[0].equalsIgnoreCase("monitor") || args[0].equalsIgnoreCase("m")) {

                plugin.getClaimMonitor().open(p);
                p.sendMessage(plugin.PREFIX+"Du hast den Claim Monitor geöffnet!");
                return true;
            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("confirm")) {

                ClaimPlayer claimPlayer = plugin.getPlayerManager().getClaimPlayerByPlayer(p);
                if (!plugin.getPlayerManager().hasValidAuswahl(claimPlayer)) {
                    p.sendMessage(ClaimSystem.PREFIX+"Du hast noch keine vollständige Auswahl!");
                    return false;
                }

                Cuboid cuboid = new Cuboid(claimPlayer.getL1(),claimPlayer.getL2());
                if (plugin.getClaimManager().isCuboidOverlapingWithOtherOne(cuboid)) {
                    p.sendMessage(ClaimSystem.PREFIX+"Dieser Bereich ist bereits gelclaimed!");
                    return false;
                }

                if (plugin.getClaimManager().getPlayerClaimCount(p) >= plugin.getClaimManager().max_claims && !(plugin.getPlayerManager().getAdminPlayers().contains(plugin.getPlayerManager().getClaimPlayerByPlayer(p)))) {
                    p.sendMessage(ClaimSystem.PREFIX+"Du hast bereits die maximale Anzahl an Claims erreicht! §8(§e" + plugin.getClaimManager().getPlayerClaimCount(p) + "§8/§e" + plugin.getClaimManager().max_claims + "§8)");
                    return false;
                }

                plugin.getClaimManager().newClaim(p, cuboid);
                plugin.getPlayerManager().resetClaimPlayerAuswahl(claimPlayer);
                p.sendMessage(ClaimSystem.PREFIX+"Du hast deine Auswahl erfolgreich geclaimed! §8(§e" + plugin.getClaimManager().getPlayerClaimCount(p) + "§8/§e" + plugin.getClaimManager().max_claims + "§8)");

                return true;
            }
        }

        //Setzt Spieler in den Admin-Modus
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("admin")) {
                ClaimPlayer claimPlayer = plugin.getPlayerManager().getClaimPlayerByPlayer(p);
                plugin.getPlayerManager().changePlayerAdminMode(claimPlayer);
            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("delete")) {

                ClaimPlayer claimPlayer = plugin.getPlayerManager().getClaimPlayerByPlayer(p);

                Claim claim = plugin.getClaimManager().getClaimByLocation(p.getLocation());
                if (claim == null) {
                    p.sendMessage(ClaimSystem.PREFIX+"An dieser Stelle befindet sich kein Claim");
                    return false;
                }
                if (!claim.getOwner().equals(p.getUniqueId()) && !plugin.getPlayerManager().getAdminPlayers().contains(plugin.getPlayerManager().getClaimPlayerByPlayer(p))) {
                    p.sendMessage(ClaimSystem.PREFIX+"Du bist nicht der Besitzer dieses Claims");
                    return false;
                }

                plugin.getClaimManager().removeClaim(claim);
                p.sendMessage(ClaimSystem.PREFIX+"Du hast dein Claim erfolgreich gelöscht!");

                return true;
            }
        }

        if(args.length == 2) {
            if (args[0].equalsIgnoreCase("trust")) {
                OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
                if (op.hasPlayedBefore()) {

                    Claim claim = plugin.getClaimManager().getClaimByLocation(p.getLocation());
                    if (claim == null) {
                        p.sendMessage(ClaimSystem.PREFIX+"An dieser Stelle befindet sich kein Claim");
                        return false;
                    }
                    if (!claim.getOwner().equals(p.getUniqueId()) && !plugin.getPlayerManager().getAdminPlayers().contains(plugin.getPlayerManager().getClaimPlayerByPlayer(p))) {
                        p.sendMessage(ClaimSystem.PREFIX+"Du bist nicht der Besitzer dieses Claims");
                        return false;
                    }

                    if (claim.getTrustedPlayeres().contains(op.getUniqueId())) {
                        Bukkit.getPlayer(claim.getOwner()).sendMessage(ClaimSystem.PREFIX+"Dieser Spieler steht bereits auf der Trust-Liste!");
                        return false;
                    }

                    plugin.getClaimManager().trustPlayer(claim,op.getUniqueId());
                    p.sendMessage(ClaimSystem.PREFIX+"Du hast den Spieler §b"+op.getName()+" §7erfolgreich zur Trust-Liste hinzugefügt!");
                } else {
                    p.sendMessage(ClaimSystem.PREFIX+"Dieser Spieler war noch nie auf dem Server");
                    return false;
                }
            }
            else if(args[0].equalsIgnoreCase("untrust")) {
                OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
                if (op.hasPlayedBefore()) {

                    Claim claim = plugin.getClaimManager().getClaimByLocation(p.getLocation());
                    if (claim == null) {
                        p.sendMessage(ClaimSystem.PREFIX+"An dieser Stelle befindet sich kein Claim");
                        return false;
                    }
                    if (!claim.getOwner().equals(p.getUniqueId()) && !plugin.getPlayerManager().getAdminPlayers().contains(plugin.getPlayerManager().getClaimPlayerByPlayer(p))) {
                        p.sendMessage(ClaimSystem.PREFIX+"Du bist nicht der Besitzer dieses Claims");
                        return false;
                    }
                    if (!claim.getTrustedPlayeres().contains(op.getUniqueId())) {
                        Bukkit.getPlayer(claim.getOwner()).sendMessage(ClaimSystem.PREFIX+"Dieser Spieler steht nicht auf der Trust-Liste!");
                        return false;
                    }

                    plugin.getClaimManager().untrustPlayer(claim,op.getUniqueId());
                    p.sendMessage(ClaimSystem.PREFIX+"Du hast den Spieler §b"+op.getName()+ " §7erfolgreich von der Trust-Liste gelöscht!");
                } else {
                    p.sendMessage(ClaimSystem.PREFIX+"Dieser Spieler war noch nie auf dem Server");
                    return false;
                }
            }
        }


        //Ab hier nur Debug Commands
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("vc") || args[0].equalsIgnoreCase("visualizeClaim")) {

                Claim claim = plugin.getClaimManager().getClaimByLocation(p.getLocation());
                if (claim == null) {
                    p.sendMessage(ClaimSystem.PREFIX+"An dieser Stelle befindet sich kein Claim");
                    return false;
                }

                plugin.getClaimManager().visualizeClaim(claim,p);
                p.sendMessage(ClaimSystem.PREFIX+"Claim sollte nun sichtbar sein!");
                return true;
            }
            if (args[0].equalsIgnoreCase("vnc") || args[0].equalsIgnoreCase("visualizeNearClaims")) {

                if (plugin.getClaimManager().getClaims() == null) {
                    p.sendMessage(ClaimSystem.PREFIX+"Es gibt derzeit noch keine Claims!");
                    return false;
                }

                for (Claim all : plugin.getClaimManager().getClaimsNearPlayer(p)) {
                    plugin.getClaimManager().visualizeClaim(all,p);
                }
                p.sendMessage(plugin.PREFIX+"Alle Claims in deiner Nähe wurden visualisiert!");
                return true;
            }
            if (args[0].equalsIgnoreCase("ga") || args[0].equalsIgnoreCase("getAuswahl")) {

                ClaimPlayer claimPlayer = plugin.getPlayerManager().getClaimPlayerByPlayer(p);
                if (!plugin.getPlayerManager().hasValidAuswahl(claimPlayer)) {
                    p.sendMessage(ClaimSystem.PREFIX+"Du hast noch keine vollständige Auswahl!");
                    return false;
                }

                p.sendMessage(ClaimSystem.PREFIX+"Aktuelle Auswahl:" + claimPlayer.getL1() + " / " + claimPlayer.getL2());
                return true;
            }
            if (args[0].equalsIgnoreCase("gc") || args[0].equalsIgnoreCase("getClaims")) {

                if (plugin.getClaimManager().getClaims() == null) {
                    p.sendMessage(ClaimSystem.PREFIX+"Es gibt derzeit noch keine Claims!");
                    return false;
                }

                for (Claim all : plugin.getClaimManager().getClaims()) {
                    p.sendMessage(ClaimSystem.PREFIX+ "Claim by: "+ all.getOwner());
                    p.sendMessage(ClaimSystem.PREFIX+ "Friends: "+ all.getTrustetPlayerString());
                    p.sendMessage(ClaimSystem.PREFIX+ "Location: "+ all.getCuboid().toString());
                    p.sendMessage("----------------------------------");
                }

                return true;
            }
            if (args[0].equalsIgnoreCase("gnc") || args[0].equalsIgnoreCase("getNearClaims")) {

                if (plugin.getClaimManager().getClaims() == null) {
                    p.sendMessage(ClaimSystem.PREFIX+"Es gibt derzeit noch keine Claims!");
                    return false;
                }

                p.sendMessage("----------------------------------");
                for (Claim all : plugin.getClaimManager().getClaimsNearPlayer(p)) {
                    p.sendMessage(ClaimSystem.PREFIX+ "Claim by: "+ all.getOwner());
                    p.sendMessage(ClaimSystem.PREFIX+ "Friends: "+ all.getTrustetPlayerString());
                    p.sendMessage(ClaimSystem.PREFIX+ "Location: "+ all.getCuboid().toString());
                    p.sendMessage("----------------------------------");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("gcp") || args[0].equalsIgnoreCase("getClaimPlayers")) {

                if (plugin.getPlayerManager().getClaimPlayers() == null) {
                    p.sendMessage(ClaimSystem.PREFIX+"Es gibt derzeit noch keine ClaimPlayer!");
                    return false;
                }

                for (ClaimPlayer all : plugin.getPlayerManager().getClaimPlayers()) {
                    p.sendMessage(ClaimSystem.PREFIX+ all.getPlayer().getDisplayName());
                    return true;
                }

                return true;
            }
            if (args[0].equalsIgnoreCase("i") || args[0].equalsIgnoreCase("info")) {

                Claim claim = plugin.getClaimManager().getClaimByLocation(p.getLocation());
                if (claim == null) {
                    p.sendMessage(ClaimSystem.PREFIX+"An dieser Stelle befindet sich kein Claim");
                    return false;
                }

                plugin.getClaimManager().visualizeClaim(claim,p);
                p.sendMessage("----------------------------------");
                p.sendMessage(ClaimSystem.PREFIX+ "Claim by: "+ claim.getOwnerName());
                p.sendMessage(ClaimSystem.PREFIX+ "Friends: "+ claim.getTrustetPlayerString());
                p.sendMessage(ClaimSystem.PREFIX+ "Location: "+ claim.getCuboid().toString());
                p.sendMessage("----------------------------------");
                return true;
            }
        }
        return false;
    }
}
