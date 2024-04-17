package bakos.geci.quarry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.BlockState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryEvents implements Listener {

    private QuarryInterfaceBlocker quarryInterfaceBlocker;


    @EventHandler
    public void onCLick(InventoryClickEvent e){
        // Check if the clicked inventory is null
        if(e.getClickedInventory() == null) return;
        ItemStack clickedItem = e.getCurrentItem();
        // Check if the clicked item is null
        if(e.getCurrentItem().getType() != null && e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE && e.getClickedInventory().getHolder() instanceof SelectionScreen){
            e.setCancelled(true);
        }
        // Check if the clicked inventory is the SelectionScreen
        if(e.getClickedInventory().getHolder() instanceof SelectionScreen){
            int clickedSlot = e.getSlot();

            if(clickedSlot == 14 && clickedItem.getType() != Material.DIAMOND && clickedItem == null){
                e.setCancelled(true);
            }

        }
    }

    private void handleInventoryUpdate(Inventory inventory, HumanEntity whoClicked) {
        Location location = ((BlockState) inventory.getHolder()).getLocation();
        QuarryMiner miner = QuarryInterfaceBlocker.getMiner(location);
        if (miner != null && !miner.isMining() && miner.hasFuel(location)) {
            miner.startMining(location, 16);
        }
    }

}
