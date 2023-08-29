package dev.authorises.cavelet.eventsystem;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.intelligentblocks.IntelligentBlock;
import dev.authorises.cavelet.utils.LootDecider;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.Bukkit;
import org.reflections.Reflections;
import scala.Int;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class EventManager {

    public Event currentEvent;
    public HashMap<String, Class<? extends Event>> loadedEvents;
    public LootDecider<String> eventDecider;
    public Integer secondsNextEvent;


    public EventManager() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Bukkit.getLogger().info("Loading events");

        loadedEvents = new HashMap<>();

        Set<Class<? extends Event>> events = new Reflections("dev.authorises.cavelet.")
                .getSubTypesOf(Event.class);
        for(Class<? extends Event> c : events) {
            Event b = c.getConstructor().newInstance();
            loadedEvents.put(b.id, c);
            Bukkit.getLogger().info("Loaded event: "+b.id);
        }

        eventDecider = new LootDecider<String>()
                .addChance(25.0, "CTHULU")
                .addChance(20.0, "DOUBLE_FARMING_XP")
                .addChance(20.0, "DOUBLE_MINING_XP")
                .addChance(20.0, "DOUBLE_COMBAT_XP")
                .addChance(5.0, "TRIPLE_COMBAT_XP")
                .addChance(5.0, "TRIPLE_MINING_XP")
                .addChance(5.0, "TRIPLE_FARMING_XP");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Cavelet.getPlugin(Cavelet.class), () -> {
            if(secondsNextEvent==null) return;
            secondsNextEvent-=1;
            if(secondsNextEvent<=0){

                try {
                    startEvent(loadedEvents.get(eventDecider.decide()));
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        },0L, 20L);
        secondsNextEvent=1800;
    }

    public void clearCurrentEvent(){
        this.currentEvent = null;
        secondsNextEvent=1800;
    }


    public void startEvent(Class<? extends Event> event) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        secondsNextEvent = null;
        Event e = event.getConstructor().newInstance();
        Bukkit.getLogger().info("Starting event: "+e.id);
        this.currentEvent = e;
        e.start();
    }

    public void forceEndEvent(){
        this.currentEvent.end(true);
        clearCurrentEvent();
    }

}
