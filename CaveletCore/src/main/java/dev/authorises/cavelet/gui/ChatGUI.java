package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.chat.ChatChannel;
import dev.authorises.cavelet.chat.ChatSettings;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ChatGUI {

    private ChestGui gui;
    private Player player;
    private MProfile mp;

    private void update(){
        gui.getPanes().removeAll(gui.getPanes());

        OutlinePane background = new OutlinePane(0, 0, 9, 1);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        OutlinePane channels = new OutlinePane(0, 0, 9, 1);

        ChatSettings settings = Cavelet.chatManager.playerSettings.get(player.getUniqueId());

        for(ChatChannel channel : ChatChannel.values()){
            if(channel.hasAccess.test(player)){

                ItemBuilder builder = new ItemBuilder(channel.item)
                        .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+channel.colour+channel.name));
                if(settings.getCurrentChannel()==channel){
                    builder = builder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><green>Selected"))
                            .addUnsafeEnchantment(Enchantment.CHANNELING, 1)
                            .addFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_PLACED_ON)
                            .addFlagsToMeta(ItemFlag.HIDE_ATTRIBUTES);
                }
                builder = builder
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Visibility: "+(settings.getVisibility().get(channel)?"<green>Visible":"<red>Hidden")))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left click to select"))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Right click to toggle visibility"));
                channels.addItem(new GuiItem(
                        builder.toItemStack(),
                        inventoryClickEvent -> {
                            if(inventoryClickEvent.isLeftClick() && channel!=settings.getCurrentChannel()){
                                settings.setCurrentChannel(channel);
                                player.sendMessage(Cavelet.miniMessage.deserialize("<gray>You are now talking in "+channel.colour+channel.name+" Chat"));
                                update();
                            }else if(inventoryClickEvent.isRightClick()){
                                boolean newValue = !settings.getVisibility().get(channel);
                                settings.setVisibility(channel, newValue);
                                player.sendMessage(Cavelet.miniMessage.deserialize(channel.colour+channel.name+" Chat<gray> is now "+(newValue?"<green>Visible":"<red>Hidden")));
                                update();
                            }
                        }

                ));
            }
        }

        gui.addPane(background);
        gui.addPane(channels);
        gui.update();

    }

    public ChatGUI(Player player){
        this.player = player;
        this.mp = Cavelet.cachedMPlayers.get(player.getUniqueId());
        gui = new ChestGui(1, ColorUtils.format("&dChat Settings"));
        gui.setOnGlobalClick((click) -> click.setCancelled(true));
        update();
        gui.show(player);
    }

}
