package dev.authorises.cavelet.command;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CaveletVersionCommand implements CommandExecutor {

    private String readGitProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("git.json");
        try {
            return readFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "Version information could not be retrieved";
        }
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject o = (JSONObject) parser.parse(readGitProperties());
            String s = "<green>Currently running Cavelet Version %ver%. <gray>(%commit-id%)<newline><aqua>Branch: <white>%branch%Mnewline><aqua>Build Time: <white>%buildtime%";
            s=s.replace("%ver%", (String) o.get("git.build.version"));
            s=s.replace("%commit-id%", (String) o.get("git.commit.id.describe"));
            s=s.replace("%branch%", (String) o.get("git.branch"));
            s=s.replace("%buildtime%", (String) o.get("git.build.time"));
            sender.sendMessage(Cavelet.miniMessage.deserialize(s));
        } catch (ParseException e) {
            sender.sendMessage(ColorUtils.format("&cAn error occurred getting the cavelet version information."));
            throw new RuntimeException(e);
        }
        return true;
    }
}
