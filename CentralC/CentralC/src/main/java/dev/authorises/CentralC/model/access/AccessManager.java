package dev.authorises.CentralC.model.access;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class AccessManager {
    public final HashMap<String, AccessToken> accessTokens;
    private final long startTime;

    public AccessManager() {
        this.accessTokens = new HashMap<>();
        this.startTime = System.currentTimeMillis();
    }

    public void addToken(AccessToken token){
        this.accessTokens.put(token.hash, token);
    }

    public Boolean canAccess(String token, PermissionType type){
        token = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        if(!(accessTokens.containsKey(token))) return false;
        AccessToken accessToken = accessTokens.get(token);
        return accessToken.permissions.weight >= type.weight;
    }

    public Boolean canAccess(AccessToken token, PermissionType type){
        if(!(accessTokens.containsValue(token))) return false;
        return token.permissions.weight >= type.weight;
    }


}
