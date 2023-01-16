package com.itndev.factions.RedisStreams.BungeeAPI;

import com.itndev.factions.Utils.UserInfoUtils;

import java.util.ArrayList;

public class BungeeAPI {

    public static Boolean isOnline(String UUID) {
        return BungeeStorage.UUID_TO_CONNECTEDSERVER.containsKey(UUID);
    }
    private static int FPlayerAmount = 0;

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


    private static Boolean isfirst = true;
    public static ArrayList<String> getOnlineNames() {
        ArrayList<String> Names = new ArrayList<>();
        BungeeStorage.UUID_TO_CONNECTEDSERVER.keySet().forEach(key -> Names.add(UserInfoUtils.getPlayerUUIDOriginName(key)));
        return Names;
    }

    public static void setFPlayerAmount(int amount) {
        FPlayerAmount = amount;
    }

    public static int getFPlayerAmount() {
        return FPlayerAmount;
    }
}
