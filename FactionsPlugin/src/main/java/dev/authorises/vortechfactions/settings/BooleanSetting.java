package dev.authorises.vortechfactions.settings;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;

public class BooleanSetting extends Setting{
    private final String name;
    private final String displayName;
    private final String description;
    private final String enabledMessage;
    private final String disabledMessage;
    private final Boolean defaultState;
    private final Material displayItem;

    /**
     * @param name            The name of the setting ( Must be unique from all other setting names )
     * @param displayName     The display name of the setting, used for creating the Item
     * @param description     The description of the setting
     * @param enabledMessage  The message displayed to the user when the setting is disabled
     * @param disabledMessage The message displayed to the user when the setting is disabled
     * @param defaultState    The default state of the setting, true is enabled and false is disabled
     * @param displayItem     The display item of the setting, used for creating the Item
     */
    public BooleanSetting(String name, String displayName, String description, String enabledMessage, String disabledMessage, Boolean defaultState, Material displayItem, SettingsCategory... categories){
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.enabledMessage = enabledMessage;
        this.disabledMessage = disabledMessage;
        this.defaultState = defaultState;
        this.displayItem = displayItem;
        for(SettingsCategory category : categories){
            this.addCategory(category);
        }
    }

    /**
     * @param uuid the UUID of the player you are checking the state of
     * @return a boolean which is the state of the players setting, returns the default if the player has never joined
     */
    public Boolean getState(UUID uuid){
        return VortechFactions.dataFileConfig.getBoolean(uuid.toString()+".settings."+this.name, this.defaultState);
    }

    /**
     * @param uuid the UUID of the player you are setting the state of
     * @param newState a Boolean which is true for enabled and false for disabled for the new state
     */
    public void setState(UUID uuid, Boolean newState) throws IOException {
        VortechFactions.dataFileConfig.set(uuid.toString()+".settings."+this.name, newState);
        VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
    }

    /**
     * @param uuid  the UUID of the player you are toggling the state of
     * @throws IOException If there is an issue saving the file after the change
     */
    public void toggleState(UUID uuid) throws IOException {
        setState(uuid, !getState(uuid));
    }

    /**
     * @param uuid the UUID of the player to generate the item for
     * @return an ItemStack that can be used to display the status of the setting
     */
    public ItemStack getItem(UUID uuid){
        Boolean state = getState(uuid);
        return new ItemBuilder(this.displayItem)
                .setName(ColorUtils.format("&d"+this.displayName))
                .addLoreLine(ColorUtils.format(""))
                .addLoreLine(ColorUtils.format("&7Description"))
                .addLoreLine(ColorUtils.format("&f"+this.description))
                .addLoreLine(ColorUtils.format(""))
                .addLoreLine(ColorUtils.format("&7Currently "+(state?"&aEnabled":"&cDisabled")))
                .addLoreLine(ColorUtils.format("&f"+(state?this.enabledMessage:this.disabledMessage)))
                .addLoreLine(ColorUtils.format(""))
                .addLoreLine(ColorUtils.format("&7Left Click to &aEnable"))
                .addLoreLine(ColorUtils.format("&7Right Click to &cDisable"))
                .toItemStack();
    }

    /**
     * @return the display name of the setting
     */
    public String getDisplayName(){
        return this.displayName;
    }

    /**
     * @return the name of the setting
     */
    public String getName(){
        return this.name;
    }
}
