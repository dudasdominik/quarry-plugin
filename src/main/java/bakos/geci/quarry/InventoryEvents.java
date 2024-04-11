package bakos.geci.quarry;

import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onCLick(InventoryClickEvent e){
        // Check if the clicked inventory is null
        if(e.getClickedInventory() == null) {return;}
        ItemStack clickedItem = e.getCurrentItem();
        // Check if the clicked item is null
        if(e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE && e.getClickedInventory().getHolder() instanceof SelectionScreen){
            e.setCancelled(true);
        }
        // Check if the clicked inventory is the SelectionScreen
        if(e.getClickedInventory().getHolder() instanceof SelectionScreen){
            int clickedSlot = e.getSlot();

            if(clickedSlot == 14 && clickedItem.getType() != Material.DIAMOND && clickedItem == null){
                e.setCancelled(true);
            }
            if(!(clickedSlot == 26 && isFuel(clickedItem)) && clickedItem == null){
                e.setCancelled(true);
            }
        }
    }

    private boolean isFuel(ItemStack item){
        Material type = item.getType();
        return type == Material.COAL || type == Material.CHARCOAL || type == Material.LAVA_BUCKET || type == Material.BLAZE_ROD || type == Material.COAL_BLOCK;
    }
}
