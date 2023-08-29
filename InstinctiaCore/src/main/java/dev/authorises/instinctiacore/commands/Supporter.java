package dev.authorises.instinctiacore.commands;

import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.time.LocalDateTime;
import java.util.Date;

public class Supporter extends Command {

    public Supporter(){
        super("supporter");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if(strings.length>=1){
                /**
                if(strings[0].equalsIgnoreCase("test")){
                    InstinctiaCore.supporterExpires.put(p.getUniqueId(), System.currentTimeMillis()+Integer.valueOf(strings[1]));
                }
                 */
            }else{
                if(InstinctiaCore.supporterExpires.containsKey(p.getUniqueId())){
                    Long expires = InstinctiaCore.supporterExpires.get(p.getUniqueId());
                    if(expires<=System.currentTimeMillis()){
                        p.sendMessage(ColorUtils.format("&fYou currently &cdo not&f have supporter, you can get it from the &astore&f or by &avoting&f."));
                    }else{
                        long d = InstinctiaCore.supporterExpires.get(p.getUniqueId())-System.currentTimeMillis();
                        long seconds = d/1000;
                        long minutes = seconds/60;
                        long hours = minutes/60;
                        long days = hours/24;
                        if(days>0){
                            p.sendMessage(ColorUtils.format("&fYour &dSupporter Subscription&f will end in "+days+"d "+hours%24+"h "+minutes%60+"m "+seconds%60+"s"));
                        }else{
                            if(hours>0){
                                p.sendMessage(ColorUtils.format("&fYour &dSupporter Subscription&f will end in "+hours%24+"h "+minutes%60+"m "+seconds%60+"s"));
                                p.sendMessage(ColorUtils.format("&c&lNOTE&f your subscription ends soon, purchase more on the &astore&f or by &avoting&f."));
                            }else{
                                if(minutes>0){
                                    p.sendMessage(ColorUtils.format("&fYour &dSupporter Subscription&f will end in "+minutes%60+"m "+seconds%60+"s"));
                                    p.sendMessage(ColorUtils.format("&c&lNOTE&f your subscription ends soon, purchase more on the &astore&f or by &avoting&f."));
                                }else{
                                    if(seconds>0){
                                        p.sendMessage(ColorUtils.format("&fYour &dSupporter Subscription&f will end in "+seconds%60+"s"));
                                        p.sendMessage(ColorUtils.format("&c&lNOTE&f your subscription ends soon, purchase more on the &astore&f or by &avoting&f."));
                                    }
                                }
                            }
                        }

                    }
                }else{
                    p.sendMessage(ColorUtils.format("&cError! Error code 1"));
                }
            }
        }
    }
}
