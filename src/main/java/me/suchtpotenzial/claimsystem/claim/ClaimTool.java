package me.suchtpotenzial.claimsystem.claim;

import me.suchtpotenzial.claimsystem.util.ItemStackManager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClaimTool {

    public static void giveTool(Player p) {
        ItemStack tool = ItemStackManager.create(Material.STICK,"§5Claim Tool",1,"§7Linksklick §8-> §7Position 1;§7Rechtsklick §8-> §7Position 2");
        tool.addUnsafeEnchantment(Enchantment.LUCK,1);
        p.getInventory().addItem(tool);
    }
}
