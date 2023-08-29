package dev.authorises.lightweightlobby.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtil {

    public static UUID combineUUIDs(UUID uuid1, UUID uuid2) {
        ByteBuffer bb = ByteBuffer.allocate(32);
        bb.putLong(uuid1.getMostSignificantBits());
        bb.putLong(uuid1.getLeastSignificantBits());
        bb.putLong(uuid2.getMostSignificantBits());
        bb.putLong(uuid2.getLeastSignificantBits());
        bb.flip();
        return new UUID(bb.getLong(), bb.getLong());
    }

}
