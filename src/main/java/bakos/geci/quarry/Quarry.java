package bakos.geci.quarry;

import org.bukkit.plugin.java.JavaPlugin;

public final class Quarry extends JavaPlugin {

    @Override
    public void onEnable() {
        QuarryRecipe recipe = new QuarryRecipe(this);
        QuarryMiner miner = new QuarryMiner(this);
        recipe.createCustomRecipe();
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        getServer().getPluginManager().registerEvents(new QuarryInterfaceBlocker(miner), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
