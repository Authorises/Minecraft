package dev.authorises.instinctiacore.utilities;

import java.util.UUID;

public class FriendRequest {
    public UUID sender;
    public String player;

    public FriendRequest(UUID sender, String player) {
        this.sender = sender;
        this.player = player;
    }

}
