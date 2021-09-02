package me.suchtpotenzial.claimsystem.util;

import me.suchtpotenzial.claimsystem.ClaimSystem;
import me.suchtpotenzial.claimsystem.claim.Claim;
import me.suchtpotenzial.claimsystem.claim.ClaimSerializer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {

    public static void loadAllClaims() throws IOException, InvalidConfigurationException {
        File file = new File(ClaimSystem.getPlugin().getDataFolder(),"Claims.yml");

        YamlConfiguration config = new YamlConfiguration();
        config.load(file);

        List<String> serializedClaims = (List<String>) config.get("Claims");
        List<Claim> claims = new ArrayList<>();
        for (String all : serializedClaims) {
            claims.add(ClaimSerializer.deserialize(all));
        }
        ClaimSystem.getPlugin().getClaimManager().claims = claims;
    }

    public static void writeAllClaimsToFile() {

        File file = new File(ClaimSystem.getPlugin().getDataFolder(),"Claims.yml");
        YamlConfiguration config = new YamlConfiguration();

        List<String> serializedClaims = new ArrayList<>();
        for (Claim all : ClaimSystem.getPlugin().getClaimManager().getClaims()) {
            serializedClaims.add(ClaimSerializer.serialize(all));
        }
        config.set("Claims", serializedClaims);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDefaults(ClaimSystem plugin) {
        plugin.getConfig().addDefault("claimBlockCost", 100);
        plugin.getConfig().addDefault("PlayerClaimLimit", 3);


        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    public static void loadEverything() {
        try {
            loadAllClaims();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
