package bakos.geci.quarry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class QuarryInterfaceBlocker implements Listener{

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        BlockState state = block.getState();
        if (state instanceof Beacon) {
            Beacon beacon = (Beacon) state;
            // Check if the block is a quarry
            if (beacon.getCustomName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry") && event.getAction().name().equals("RIGHT_CLICK_BLOCK")) {
                event.setCancelled(true);
                Inventory existingInventory = InventoryStorage.playerInventories.get(block.getLocation());
                // If the player has not opened the inventory yet, create a new one
                if (existingInventory == null) {
                    SelectionScreen screen = new SelectionScreen();
                    InventoryStorage.playerInventories.put(block.getLocation(), screen.getInventory());
                    event.getPlayer().openInventory(screen.getInventory());
                    createQuarryArea(event.getPlayer().getFacing(), block, 16);
                } else {
                    event.getPlayer().openInventory(existingInventory);
                }


            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getState() instanceof Beacon) {
            Beacon beacon = (Beacon) block.getState();
            // Check if the block is a quarry
            if (beacon.getCustomName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry")) {
                Location blockLocation = block.getLocation();
                Inventory inventory = InventoryStorage.playerInventories.get(blockLocation);
                // Drop all items in the inventory
                if (inventory != null) {
                    for (ItemStack item : inventory.getContents()) {
                        if (item != null && item.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                            block.getWorld().dropItemNaturally(blockLocation, item);
                        }
                    }
                    removeQuarryArea(block, 16);
                    InventoryStorage.playerInventories.remove(block.getLocation());
                }
            }
        }
    }

    private void createQuarryArea(BlockFace facing, Block block, int size) {
        int startX = block.getX();
        int startY = block.getY();
        int startZ = (int) (block.getZ() + facing.getDirection().getZ());
        int endX = startX + size - 1;
        int endY = startY + size - 1;
        int endZ = startZ + size - 1;

        // Create the top and bottom layers
        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                if (x == startX || x == endX || z == startZ || z == endZ) {
                    boolean isBlack = (x + z) % 2 == 0; // Check if sum of x and z is even
                    Material material = isBlack ? Material.BLACK_CONCRETE : Material.YELLOW_CONCRETE;
                    block.getWorld().getBlockAt(x, startY, z).setType(material);
                    block.getWorld().getBlockAt(x, endY, z).setType(material);
                }
            }
        }

        // Create the vertical pillars at the corners
        for (int y = startY + 1; y < endY; y++) {
            if (startX != endX && startZ != endZ) { // To avoid filling in the corners when the size is too small
                boolean isBlack = (y) % 2 == 0 ; // Check if sum of y and startX or startZ is even
                Material material = isBlack ? Material.BLACK_CONCRETE : Material.YELLOW_CONCRETE;
                block.getWorld().getBlockAt(startX, y, startZ).setType(material);
                block.getWorld().getBlockAt(startX, y, endZ).setType(material);
                block.getWorld().getBlockAt(endX, y, startZ).setType(material);
                block.getWorld().getBlockAt(endX, y, endZ).setType(material);
            }
        }
    }

    private void removeQuarryArea(Block block, int size) {
        int startX = block.getX();
        int startY = block.getY();
        int startZ = block.getZ();
        int endX = startX + size - 1;
        int endY = startY + size - 1;
        int endZ = startZ + size - 1;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ + 1; z++) {
                    block.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }

}
