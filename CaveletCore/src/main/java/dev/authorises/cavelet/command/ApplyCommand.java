package dev.authorises.cavelet.command;


import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ApplyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TextComponent c = Component.text(ColorUtils.format("&bClick here"))
                                   .clickEvent(ClickEvent.openUrl("https://forms.gle/zPudq6AssDj7YXQdA"));
        sender.sendMessage(c);
        return true;
    }
}
