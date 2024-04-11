package bakos.geci.quarry;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public class InventoryStorage {
    public static HashMap<Location, Inventory> playerInventories = new HashMap<>();
}
