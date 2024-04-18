package bakos.geci.quarry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class QuarryMiner {
    private int taskID;
    private int x, y, z;
    private World world;
    private int size;
    private JavaPlugin plugin;

    private int originalX, originalZ;
    private int blockCounter = 0;

    public QuarryMiner(JavaPlugin plugin, Location location) {
        this.plugin = plugin;
        this.taskID = -1;
        this.world = location.getWorld();
        this.x = location.getBlockX() + 1;
        this.y = location.getBlockY() - 1;
        this.z = location.getBlockZ() + 2;
        this.originalX = this.x; // And these lines
        this.originalZ = this.z;
    }

    public void startMining(Location location, int size) {
        if (!hasFuel(location)) {
            System.out.println("No fuel to start mining");
            return;
        }

        if(isMining()){
            System.out.println("Already mining");
            return;
        }


        this.size = size;



        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                mineBlock(originalX, originalZ, location);
            }
        }, 0L, 3L);
    }

    public boolean hasFuel(Location location) {
        Inventory inventory = InventoryStorage.playerInventories.get(location);
        if (inventory == null) {
            return false;
        }

        for (ItemStack item : inventory.getContents()) {
            if (item != null && isFuel(item)) {
                return true;
            }
        }

        System.out.println("No fuel");

        return false;
    }

    private boolean isFuel(ItemStack item) {
        Material type = item.getType();
        return type == Material.COAL || type == Material.CHARCOAL || type == Material.LAVA_BUCKET || type == Material.BLAZE_ROD || type == Material.COAL_BLOCK;
    }

    public boolean isMining() {
        System.out.println("Is mining");
        System.out.println(Bukkit.getScheduler().isQueued(taskID) + " - " + taskID + " - " + x + " - " + y + " - " + z);
        return taskID >= 0 && Bukkit.getScheduler().isQueued(taskID);
    }

    private void mineBlock(int startX, int startZ, Location location) {
        if (y < 0 || !hasFuel(location)) {
            Bukkit.getScheduler().cancelTask(taskID);
            return;
        }

        Block block = world.getBlockAt(x, y, z);
        if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
            blockCounter++;
            if(blockCounter >= 256){
                consumeFuel(location);
                blockCounter = 0;
            }

            Location dropLocation = new Location(world, location.getBlockX()-2, location.getBlockY(), location.getBlockZ());
            Collection<ItemStack> drops = block.getDrops();
            block.setType(Material.AIR);
            for (ItemStack drop : drops) {
                world.dropItem(dropLocation, drop);
            }
        }

        z++;
        if (z >= startZ + 14) {
            z = startZ;
            x++;
            if (x >= startX + 14) {
                x = startX;
                y--;
            }
        }
    }

    private void consumeFuel(Location location) {
        Inventory inventory = InventoryStorage.playerInventories.get(location);
        if (inventory == null) {
            return;
        }

        for (ItemStack item : inventory.getContents()) {
            if (item != null && isFuel(item)) {
                item.setAmount(item.getAmount() - 1);
                break;
            }
        }
    }

    public void stopMining() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}