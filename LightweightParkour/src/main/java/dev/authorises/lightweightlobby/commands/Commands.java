package dev.authorises.lightweightlobby.commands;

import net.minestom.server.command.builder.Command;

public class Commands {
    public static final Command SHUTDOWN = new ShutdownCommand();
    public static final Command RESTART = new RestartCommand();
    public static final Command EDITOR = new EditorCommand();
    public static final Command PLAY = new PlayCommand();
}
