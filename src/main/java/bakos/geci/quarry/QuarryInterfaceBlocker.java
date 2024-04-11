package bakos.geci.quarry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class QuarryInterfaceBlocker implements Listener{

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        BlockState state = block.getState();
        if (state instanceof Beacon) {
            Beacon beacon = (Beacon) state;
            if (beacon.getCustomName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry") && event.getAction().name().equals("RIGHT_CLICK_BLOCK")) {
                event.setCancelled(true);
                Inventory existingInventory = InventoryStorage.playerInventories.get(block.getLocation());
                System.out.println(existingInventory);
                if (existingInventory == null) {
                    SelectionScreen screen = new SelectionScreen();
                    InventoryStorage.playerInventories.put(block.getLocation(), screen.getInventory());
                    event.getPlayer().openInventory(screen.getInventory());
                    System.out.println("Opening new inventory " + existingInventory);
                } else {
                    event.getPlayer().openInventory(existingInventory);
                    System.out.println("Opening existing inventory " + existingInventory);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getState() instanceof Beacon) {
            Beacon beacon = (Beacon) block.getState();
            if (beacon.getCustomName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry")) {
                Location blockLocation = block.getLocation();
                Inventory inventory = InventoryStorage.playerInventories.get(blockLocation);
                System.out.println("Inventory to remove " + inventory);
                if (inventory != null) {
                    for (ItemStack item : inventory.getContents()) {
                        if (item != null && item.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                            block.getWorld().dropItemNaturally(blockLocation, item);
                        }
                    }
                    InventoryStorage.playerInventories.remove(block.getLocation());
                    System.out.println("Removing inventory " + inventory);
                    System.out.println("Inventory removed " + blockLocation + " | " + block.getLocation() + " | " + InventoryStorage.playerInventories.get(blockLocation));
                }
            }
        }
    }
}
