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
        inv = Bukkit.createInventory(this, INVENTORY_SIZE, "Quarry");
        init();
    }

    private void init(){
        ItemStack item;
        for(int i = 0; i < INVENTORY_SIZE; i++){
            if(i == 13|| i == 25 || i == 31){

            }else {
                item = createItem(" ", Material.GRAY_STAINED_GLASS_PANE, null, 1);
                inv.setItem(i, item);
            }
        }
    }

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
