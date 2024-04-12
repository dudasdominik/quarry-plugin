package bakos.geci.quarry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public class QuarryMiner {
    private int taskID;
    private int x, y, z;
    private World world;
    private int size;
    private JavaPlugin plugin;

    public QuarryMiner(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startMining(Location location, int size) {
        this.world = location.getWorld();
        this.x = location.getBlockX() + 1;
        this.y = location.getBlockY() - 1;
        this.z = location.getBlockZ() + 2;
        this.size = size;

        int startX = x;
        int startZ = z;

        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                System.out.println("Mining block at " + (x) + ", " + (y) + ", " + (z));
                mineBlock(startX, startZ);
            }
        }, 0L, 3L);
    }

    private void mineBlock(int startX, int startZ) {
        if (y < 0) {
            Bukkit.getScheduler().cancelTask(taskID);
            return;
        }

        Block block = world.getBlockAt(x, y, z);
        if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
            System.out.println("Breaking block at " + (x) + ", " + (y) + ", " + (z) + " - " + block.getType());
            block.breakNaturally();
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

    public void stopMining() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
