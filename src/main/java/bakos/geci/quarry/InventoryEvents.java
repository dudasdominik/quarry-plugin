package bakos.geci.quarry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onCLick(InventoryClickEvent e){
        if(e.getClickedInventory() == null) {return;}
        if(e.getClickedInventory().getHolder() instanceof SelectionScreen){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if(e.getCurrentItem() == null) {return;}
            if(e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE){

            }
        }

    }
}
