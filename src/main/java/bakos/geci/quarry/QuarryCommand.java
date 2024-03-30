package bakos.geci.quarry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuarryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Ezt a parancsot csak játékosok használhatják!");
            return true;
        }
        Player player = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("quarry")){
            SelectionScreen screen = new SelectionScreen();
            player.openInventory(screen.getInventory());
        }
        return true;
    }
}
