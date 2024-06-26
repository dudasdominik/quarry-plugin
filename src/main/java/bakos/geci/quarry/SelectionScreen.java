package bakos.geci.quarry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SelectionScreen implements InventoryHolder {

    private  Inventory inv;
    private static final int INVENTORY_SIZE = 45;

    public SelectionScreen() {
        if (inv == null) {
            this.inv = Bukkit.createInventory(this, INVENTORY_SIZE, "Quarry");
            init();
        } else {
            this.inv = inv;
        }
    }
    // Initialize the inventory
    private void init(){
        ItemStack item;
        for(int i = 0; i < INVENTORY_SIZE; i++){
            if(i == 22|| i == 9 || i == 18 || i == 27){
                item = new ItemStack(Material.AIR, 1);
                inv.setItem(i, item);
            } else if (i == 44)  {
                item = createItem("Pick Up", Material.GREEN_CANDLE, null, 1);
                inv.setItem(i, item);
            } else {
                item = createItem(" ", Material.GRAY_STAINED_GLASS_PANE, null, 1);
                inv.setItem(i, item);
            }
        }
    }
    // Create an item with a name, material, lore and amount
    private ItemStack createItem (String name, Material material, List<String> lore, int amount){
       ItemStack newItem = new ItemStack(material, amount);
       ItemMeta meta = newItem.getItemMeta();
       meta.setDisplayName(name);
       meta.setLore(lore);
       newItem.setItemMeta(meta);
       return newItem;
    }
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
