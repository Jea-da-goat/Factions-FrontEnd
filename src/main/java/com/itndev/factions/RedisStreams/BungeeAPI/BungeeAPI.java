package com.itndev.factions.RedisStreams.BungeeAPI;

public class BungeeAPI {

    public static Boolean isOnline(String UUID) {
        return BungeeStorage.UUID_TO_CONNECTEDSERVER.containsKey(UUID);
    }

    public static String colorIFONLINE(String UUID) {
        if(isOnline(UUID)) {
            return "&a";
        } else {
            return "&7";
        }
    }

    public static Integer getOnlineAmount() {
        return BungeeStorage.UUID_TO_CONNECTEDSERVER.size();
    }
}
