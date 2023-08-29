package dev.authorises.cavelet.command;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.FactionTop;
import dev.authorises.cavelet.factions.MFaction;
import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FactionTopCommand implements CommandExecutor {

    public static int getTotalPages(List<MFaction> factions, int pageSize) {
        int totalBalances = factions.size();
        int totalPages = (int) Math.ceil((double) totalBalances / pageSize);
        return totalPages;
    }

    public static int getNumElementsOnPage(int pageNumber, List<MFaction> objectList) {
        int startIndex = (pageNumber - 1) * 10;
        int endIndex = Math.min(startIndex + 10, objectList.size());
        return endIndex - startIndex;
    }

    public static Component viewPage(List<MFaction> factions, int pageNumber, int pageSize) {
        if (pageNumber < 1) {
            return Cavelet.miniMessage.deserialize("<red>Error: Invalid page number");
        }
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, factions.size());
        if (start >= factions.size()) {
            return Cavelet.miniMessage.deserialize("<red>Error: There are not enough factions with any points to show that page.");
        }
        Component c = Cavelet.miniMessage.deserialize(String.format("<#9eb5db>Showing page <#9eb5db>%d <#9eb5db>of <#9eb5db>%d<#9eb5db>:\n", pageNumber, getTotalPages(factions, pageSize)));
        for (int i = start; i < end; i++) {
            MFaction faction = factions.get(i);
            c=c.append(Cavelet.miniMessage.deserialize(String.format("<#9eb5db>- %s#%,d <green>%s: <#9eb5db><#19b6e6>%,2d points\n", FactionTop.getColorFor(i+1), i + 1, faction.getName(), faction.lastUpdatedPoints))
                    .hoverEvent(HoverEvent.showText(Cavelet.miniMessage.deserialize("<green>"+faction.getName()+"\n<#9eb5db>Click to view more information")))
                    .clickEvent(ClickEvent.runCommand("/f show "+faction.getName())));

        }

        return c;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(sender instanceof Player p){


            List<MFaction> leaderboard = Cavelet.factionManager.factionTop.leaderBoard;

            int page = 1;

            if(args.length>=1){
                try{
                    page = Integer.parseInt(args[0]);
                }catch (Exception e){
                    p.sendMessage(ColorUtils.format("&cCommand usage: /f top [page]"));
                    return true;
                }
            }

            p.sendMessage(viewPage(leaderboard, page, 10));

            /**


            if(!pageExists(page, leaderboard)){
                p.sendMessage(ColorUtils.format("&cThere is currently no page "+page+" on the faction top list. This is likely as not enough factions have a Faction Gateway placed down"));
                return true;
            }

            int start = (page*10)-10;
            int end = start+((getNumElementsOnPage(page, leaderboard)-1));

            p.sendMessage("start: "+start);
            p.sendMessage("end: "+end);

            List<MFaction> pageList = leaderboard.subList(start, end);

            p.sendMessage("listsize: "+pageList.size());

            AtomicInteger x = new AtomicInteger(start + 1);

            pageList.forEach((mFaction -> {
                p.sendMessage("#"+x+": "+mFaction.getName());
                x.addAndGet(1);
            }));

             */
        }

        return true;
    }

}
