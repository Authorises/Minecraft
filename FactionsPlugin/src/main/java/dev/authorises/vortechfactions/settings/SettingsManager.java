package dev.authorises.vortechfactions.settings;

import org.bukkit.Material;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;

public class SettingsManager {
    private ArrayList<Setting> settings;

    public SettingsManager(){
        this.settings = new ArrayList<>();
        this.settings.add(new BooleanSetting(
                "public-chat-visible",
                "Public Chat Visibility",
                "Decides your visibility of &bpublic chat&r",
                "Messages from players in &bpublic chat&f are &avisible&f",
                "Messages from players in &bpublic chat&f are &cinvisible&f",
                true,
                Material.BOOK,
                SettingsCategory.CHAT
        ));
        this.settings.add(new BooleanSetting(
                "faction-chat-visible",
                "Faction Chat Visibility",
                "Decides your visibility of &afaction chat&r",
                "Messages from players in &afaction chat&f are &avisible&f",
                "Messages from players in &afaction chat&f are &cinvisible&f",
                true,
                Material.BOOK,
                SettingsCategory.CHAT
        ));
        this.settings.add(new BooleanSetting(
                "truce-chat-visible",
                "Truce Chat Visibility",
                "Decides your visibility of &dtruce chat&r",
                "Messages from players in &dtruce chat&f are &avisible&f",
                "Messages from players in &dtruce chat&f are &cinvisible&f",
                true,
                Material.BOOK,
                SettingsCategory.CHAT
        ));
        this.settings.add(new BooleanSetting(
                "ally-chat-visible",
                "Ally Chat Visibility",
                "Decides your visibility of &aally chat&r",
                "Messages from players in &aally chat&f are &avisible&f",
                "Messages from players in &aally chat&f are &cinvisible&f",
                true,
                Material.BOOK,
                SettingsCategory.CHAT
        ));
        this.settings.add(new BooleanSetting(
                "mod-chat-visible",
                "Mod Chat Visibility",
                "Decides your visibility of &emod chat&r",
                "Messages from players in &emod chat&f are &avisible&f",
                "Messages from players in &emod chat&f are &cinvisible&f",
                true,
                Material.BOOK,
                SettingsCategory.CHAT
        ));
        this.settings.add(new BooleanSetting(
                "squid-sell-visible",
                "Squid Sell Visibility",
                "Decides your visibility of &bSquid Sell Status Messages&r",
                "You will receive information regarding how much your faction earns from squids",
                "You won't receive information regarding how much your faction earns from squids",
                true,
                Material.INK_SACK,
                SettingsCategory.ACTIONBAR
        ));
        this.settings.add(new BooleanSetting(
                "cactus-sell-visible",
                "Cactus Sell Visibility",
                "Decides your visibility of &bCactus Sell Status Messages&r",
                "You will receive information regarding how much your faction earns from cactus",
                "You won't receive information regarding how much your faction earns from cactus",
                true,
                Material.CACTUS,
                SettingsCategory.ACTIONBAR
        ));
    }

    public ArrayList<Setting> getSettings(){
        return this.settings;
    }

    public Setting getSetting(String settingName){
        for(Setting s : getSettings()){
            if(s instanceof BooleanSetting){
                if(((BooleanSetting) s).getName().equals(settingName)){
                    return s;
                }
            }
        }
        return null;
    }
}
