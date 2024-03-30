package bakos.geci.quarry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class QuarryRecipe {
    private final JavaPlugin plugin;

    public QuarryRecipe(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void createCustomRecipe(){
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta  = item.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry");
        meta.setLore(Collections.singletonList("A powerful tool for mining"));
        item.setItemMeta(meta);


        NamespacedKey key = new NamespacedKey(plugin, "quarry");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("SSS", "SNS", "DDD");
        recipe.setIngredient('S', Material.NETHER_STAR);
        recipe.setIngredient('N', Material.NETHERITE_BLOCK);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);

        Bukkit.addRecipe(recipe);
    }
}
