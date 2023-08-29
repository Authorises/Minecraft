package dev.authorises.cavelet.command.acf;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.eventsystem.EventManager;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

@CommandAlias("e|events|event")
public class EventCommand extends BaseCommand {

    @Default
    @Subcommand("info")
    @Description("Show the currently running event, if there is one.")
    public static void onInfo(Player player, String[] args){
        EventManager manager = Cavelet.eventManager;
        if(manager.currentEvent!=null){
            manager.currentEvent.showEventInfo(player);
        }else{
            int mins = Double.valueOf(Math.floor(manager.secondsNextEvent/60)).intValue();
            int seconds = manager.secondsNextEvent-(mins*60);
            String secs = String.valueOf(seconds).length()==1?"0"+seconds:""+seconds;
            player.sendMessage(ColorUtils.format("&7The next event will start in &a"+mins+":"+secs));
        }
    }

    @Subcommand("start")
    @Description("Start an event")
    @CommandPermission("events.manage")
    @CommandCompletion("@events")
    public static void startEvent(Player player, String eventId) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        EventManager eventManager = Cavelet.eventManager;
        if(eventManager.currentEvent!=null){
            player.sendMessage(ColorUtils.format("&cAn event is already running. Force end it with /event stop"));
            return;
        }
        if(eventManager.loadedEvents.containsKey(eventId)){
            eventManager.startEvent(eventManager.loadedEvents.get(eventId));
        }else{
            player.sendMessage(ColorUtils.format("&cAn event was not found with that name"));
        }
    }

    @Subcommand("stop")
    @Description("Stops the currently running event")
    @CommandPermission("events.manage")
    public static void stopEvent(Player player) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        EventManager eventManager = Cavelet.eventManager;
        eventManager.currentEvent.end(true);
    }


}
