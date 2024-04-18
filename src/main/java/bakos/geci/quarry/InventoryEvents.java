package bakos.geci.quarry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.BlockState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Objects;

public class InventoryEvents implements Listener {

    private QuarryInterfaceBlocker quarryInterfaceBlocker;

    public InventoryEvents(QuarryInterfaceBlocker quarryInterfaceBlocker) {
        this.quarryInterfaceBlocker = quarryInterfaceBlocker;
    }
    @EventHandler
    public void onCLick(InventoryClickEvent e){
        // Check if the clicked inventory is null
        if(e.getClickedInventory() != null) {
            ItemStack clickedItem = e.getCurrentItem();
            // Check if the clicked item is null
            if (clickedItem != null) {
                if (e.getCurrentItem().getType() == Material.GREEN_CANDLE && e.getClickedInventory().getHolder() instanceof SelectionScreen) {
                    e.setCancelled(true);
                    Location location = quarryInterfaceBlocker.getLocationForInventory(e.getClickedInventory());
                    QuarryMiner miner = quarryInterfaceBlocker.getMiner(location);
                    if(miner != null){
                        miner.stopMining();
                        quarryInterfaceBlocker.removeQuarryArea(location.getBlock(), 16);
                        givePlayerQuarry(e.getWhoClicked());
                    }
                }

                if (e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE && e.getClickedInventory().getHolder() instanceof SelectionScreen) {
                    e.setCancelled(true);
                }
                // Check if the clicked inventory is the SelectionScreen
                if (e.getClickedInventory().getHolder() instanceof SelectionScreen) {
                    Location location = quarryInterfaceBlocker.getLocationForInventory(e.getClickedInventory());
                    QuarryMiner miner = quarryInterfaceBlocker.getMiner(location);
                    if(!(miner.isMining()) && miner.hasFuel(location)){
                            System.out.println("Starting mining process");
                            System.out.println(miner.isMining() + " - " + miner.hasFuel(location));
                            miner.startMining(location, 16);

                    }
                    if(e.getSlot() == 24){
                        System.out.println("this is the good slot");
                    }
                }
            }
        }
    }
    private void givePlayerQuarry(HumanEntity player){
        ItemStack quarryItem = new ItemStack(Material.BEACON);
        ItemMeta meta = quarryItem.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Quarry");
        meta.setLore(Collections.singletonList("A powerful tool for mining"));
        quarryItem.setItemMeta(meta);
        player.getInventory().addItem(quarryItem);
    }




}
