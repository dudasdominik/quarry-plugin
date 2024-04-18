package bakos.geci.quarry;

import org.bukkit.plugin.java.JavaPlugin;

public final class Quarry extends JavaPlugin {
    private QuarryInterfaceBlocker quarryInterfaceBlocker;
    @Override
    public void onEnable() {
        QuarryRecipe recipe = new QuarryRecipe(this);
        recipe.createCustomRecipe();
        quarryInterfaceBlocker = new QuarryInterfaceBlocker(this);
        getServer().getPluginManager().registerEvents(new InventoryEvents(quarryInterfaceBlocker), this);
        getServer().getPluginManager().registerEvents(quarryInterfaceBlocker, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
