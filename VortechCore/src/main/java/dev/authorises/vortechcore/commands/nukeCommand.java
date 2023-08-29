package dev.authorises.vortechcore.commands;

import dev.authorises.vortechcore.VortechCore;
import dev.authorises.vortechcore.utilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class nukeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(VortechCore.nuke) {
                p.sendMessage(ColorUtils.format("&f(&b!&f) Somebody else is using &c/nuke&f right now!"));
            }else {
                Location l = p.getLocation();
                final Integer[] ticksLeft = {200};
                Bukkit.getScheduler().scheduleSyncRepeatingTask(VortechCore.getPlugin(VortechCore.class), new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(ticksLeft[0] >=1){
                            l.getWorld().playSound(l, Sound.ENTITY_SILVERFISH_AMBIENT, (1000-(ticksLeft[0]*3)), 1.0F);
                            l.getWorld().playEffect(l, Effect.SMOKE, 100);
                            ticksLeft[0]--;
                        }else{
                            l.getWorld().playEffect(l, Effect.EXTINGUISH, 150);
                            l.getWorld().playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 4.0F, 1.0F);
                            l.getWorld().playEffect(l, Effect.EXTINGUISH, 150);
                            Bukkit.getScheduler().cancelTask(this.getTaskId());
                        }
                    }
                },0,1);
            }
        }
        return true;
    }
}
