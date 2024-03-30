package bakos.geci.quarry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class QuarryInterfaceBlocker implements Listener{

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Beacon beacon = (Beacon) block.getState();

        if (beacon.getCustomName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry") && event.getAction().name().equals("RIGHT_CLICK_BLOCK")) {
            event.setCancelled(true);
            SelectionScreen screen = new SelectionScreen();
            event.getPlayer().openInventory(screen.getInventory());
        }
    }
}
