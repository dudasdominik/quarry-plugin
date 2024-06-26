package bakos.geci.quarry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QuarryInterfaceBlocker implements Listener{
    private final Set<Location> borderBlocks = new HashSet<>();
    private final Map<Location, QuarryMiner> miners = new HashMap<>();

    private final Map<Inventory, Location> inventoryLocations = new HashMap<>();



    private JavaPlugin plugin;
    public QuarryInterfaceBlocker(JavaPlugin plugin) {
        this.plugin = plugin;

    }


  public QuarryMiner getMiner(Location location){
        return miners.get(location);
  }

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
                    inventoryLocations.put(screen.getInventory(), block.getLocation());
                    event.getPlayer().openInventory(screen.getInventory());
                } else {
                    event.getPlayer().openInventory(existingInventory);
                }


            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getState() instanceof Beacon) {
            Beacon beacon = (Beacon) block.getState();
            // Check if the block is a quarry
            if (beacon.getCustomName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry")) {
                createQuarryArea(event.getPlayer().getFacing(), block, 16);
                QuarryMiner miner = new QuarryMiner(plugin, block.getLocation());
                miners.put(block.getLocation(), miner);
                miner.startMining(block.getLocation(), 16);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(borderBlocks.contains(block.getLocation())){
            event.setCancelled(true);
        }
        if (block.getState() instanceof Beacon) {
            Beacon beacon = (Beacon) block.getState();
            // Check if the block is a quarry

            if (beacon.getCustomName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry")) {
                event.setCancelled(true);
            }

            if (beacon.getCustomName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry")) {
                Location blockLocation = block.getLocation();
                Inventory inventory = InventoryStorage.playerInventories.get(blockLocation);
               QuarryMiner miner = miners.get(blockLocation);
                if (miner != null) {
                    miner.stopMining();
                    miners.remove(blockLocation);
                    System.out.println(miners.get(blockLocation));
                }
                // Drop all items in the inventory
                if (inventory != null) {
                    for (ItemStack item : inventory.getContents()) {
                        if (item != null && item.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                            block.getWorld().dropItemNaturally(blockLocation, item);
                        }
                    }
                    inventoryLocations.remove(inventory);
                    InventoryStorage.playerInventories.remove(block.getLocation());
                }
                removeQuarryArea(block, 16);
            }

        }


    }

    private void createQuarryArea(BlockFace facing, Block block, int size) {
        World world = block.getWorld();
        int startX = block.getX();
        int startY = block.getY();
        int startZ = (int) (block.getZ() + facing.getDirection().getZ());
        int endX = startX + size - 1;
        int endY = startY + size - 1;
        int endZ = startZ + size - 1;

        createLayers(world, startX, startY, startZ, endX, endY, endZ);
        createPillars(world, startX, startY, startZ, endX, endY, endZ);
    }

    private void createLayers(World world,int startX, int startY, int startZ, int endX, int endY, int endZ) {
        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                if (x == startX || x == endX || z == startZ || z == endZ) {
                    createLayerBlock(world,x, startY, z, endY);
                }
            }
        }
    }

    private void createLayerBlock(World world, int x, int yStart, int z, int yEnd) {
        boolean isBlack = (x + z) % 2 == 0;
        Material material = isBlack ? Material.BLACK_CONCRETE : Material.YELLOW_CONCRETE;
        createBlock(world, x, yStart, z, material);
        createBlock(world, x, yEnd, z, material);
    }

    private void createPillars(World world, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        for (int y = startY + 1; y < endY; y++) {
            if (startX != endX && startZ != endZ) {
                createPillarBlock(world,startX, y, startZ, endZ);
                createPillarBlock(world, endX, y, startZ, endZ);
            }
        }
    }

    private void createPillarBlock(World world, int x, int y, int zStart, int zEnd) {
        boolean isBlack = (y) % 2 == 0;
        Material material = isBlack ? Material.BLACK_CONCRETE : Material.YELLOW_CONCRETE;
        createBlock(world, x, y, zStart, material);
        createBlock(world, x, y, zEnd, material);
    }

    private void createBlock(World world, int x, int y, int z, Material material) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(material);
        borderBlocks.add(block.getLocation());
    }

    public void removeQuarryArea(Block block, int size) {
        int startX = block.getX();
        int startY = block.getY();
        int startZ = block.getZ();
        int endX = startX + size - 1;
        int endY = startY + size - 1;
        int endZ = startZ + size - 1;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ + 1; z++) {
                    removeBlockAt(block.getWorld(), x, y, z);
                }
            }
        }
    }

    private void removeBlockAt(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(Material.AIR);
        borderBlocks.remove(block.getLocation());
    }

    public Location getLocationForInventory(Inventory inventory) {
        return inventoryLocations.get(inventory);
    }

}
