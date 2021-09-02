package me.suchtpotenzial.claimsystem.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemStackManager {

    private static ItemStack iS;
    private static ItemMeta iM;

    private static String[] seclore;
    private static List<String> lore;
    private static short damage;

    public static ItemStack create(Material material, String name, int amount, String rawlore) {
        iS = new ItemStack(material, amount);
        iM = iS.getItemMeta();
        iM.setDisplayName(name);
        seclore = rawlore.split(";");
        lore = Arrays.asList(seclore);
        iM.setLore(lore);
        iS.setItemMeta(iM);
        return iS;
    }

    public static ItemStack create(Material material, String name, int amount) {
        iS = new ItemStack(material, amount);
        iM = iS.getItemMeta();
        iM.setDisplayName(name);
        iS.setItemMeta(iM);
        return iS;
    }

    public static ItemStack create(Material material, int amount) {
        iS = new ItemStack(material, amount);
        return iS;
    }

    public static ItemStack create(Material material, String name, int amount, short damage) {
        iS = new ItemStack(material, amount, damage);
        iM = iS.getItemMeta();
        iM.setDisplayName(name);
        iS.setItemMeta(iM);
        return iS;
    }

    public static ItemStack create(Material material, String name, int amount, String rawlore, short damage) {
        iS = new ItemStack(material, amount, damage);
        iM = iS.getItemMeta();
        iM.setDisplayName(name);
        seclore = rawlore.split(";");
        lore = Arrays.asList(seclore);
        iM.setLore(lore);
        iS.setItemMeta(iM);
        return iS;
    }
}
