package com.itndev.factions.Config;

import com.itndev.factions.Main;
import com.itndev.factions.RedisStreams.RedisConnection;
import com.itndev.factions.SocketConnection.Socket;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class Config {
    public static double FactionCreateBalance = 50000.0;
    public static double DTRperPlayer = 1.5;
    public static double LandClaimPrice = 12000;

    public static String command_message_not_in_faction = "&r&f소속된 국가가 없습니다";
    public static String command_message_not_in_faction_other = "&r&f해당 유저는 소속된 국가가 없습니다";

    public static String Leader = "leader";
    public static String CoLeader = "coleader";
    public static String VipMember = "vipmember";
    public static String Warrior = "warrior";
    public static String Member = "member";
    public static String Nomad = "nomad";

    public static String Ally = "ally";
    public static String Enemy = "enemy";
    public static String Neutral = "neutral";

    public static String Ally_Lang = "동맹";
    public static String Enemy_Lang = "적대";
    public static String Neutral_Lang = "중립";

    public static String Leader_Lang = "왕";
    public static String CoLeader_Lang = "영주";
    public static String VipMember_Lang = "귀족";
    public static String Warrior_Lang = "기사";
    public static String Member_Lang = "백성";
    public static String Nomad_Lang = "방랑자";

    public static Integer Faction_Main_Beacon_Data = 548431;

    public static String PERM_LEVEL_INDICATOR  = "PERM_LVL=";
    public static String PERM_FINAL_LEVEL_INDICATOR  = "PERM_FINAL_LVL=";
    public static String SUCCESS_ID_INDICATOR = "SUCCESS_ID=";
    public static String PERM_MN_SPLITTER = ":(%PERM_MN%):"; //Map Name
    public static String PERM_KV_SPLITTER = ":(%PERM_KV%):"; //Key & Value

    public static String DiscordLink = "https://discord.gg/2ayB6DQGAk";
    public static String CafeLink = "https://cafe.naver.com/";

    public static YamlConfiguration customlocalstorage = null;

    public static void saveConfig() {
        try {
            customlocalstorage.set("lastread.LastID_OUTPUT", RedisConnection.get_LastID_OUTPUT());
            customlocalstorage.set("lastread.LastID_INNER", RedisConnection.get_LastID_INNER());
            customlocalstorage.set("lastread.LastID_BUNGEE", RedisConnection.get_LastID_BUNGEE());
            customlocalstorage.set("lastread.LastID_INNER2", RedisConnection.get_LastID_INNER2());
            customlocalstorage.save(StorageDir.MainConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readConfig() {
        customlocalstorage = YamlConfiguration.loadConfiguration(StorageDir.MainConfig);
        addDefaultConfig();
        RedisConnection.setRedis_address(customlocalstorage.getString("redis.address"));
        RedisConnection.setRedis_port(customlocalstorage.getInt("redis.port"));
        RedisConnection.setRedis_password(customlocalstorage.getString("redis.password"));
        RedisConnection.setSslEnabled(customlocalstorage.getBoolean("redis.sslEnable"));
        Main.ServerName = customlocalstorage.getString("redisconfig.mainservername");
        RedisConnection.set_LastID_OUTPUT(customlocalstorage.getString("lastread.LastID_OUTPUT"));
        RedisConnection.set_LastID_INNER(customlocalstorage.getString("lastread.LastID_INNER"));
        RedisConnection.set_LastID_BUNGEE(customlocalstorage.getString("lastread.LastID_BUNGEE"));
        Socket.Address = customlocalstorage.getString("backend.address");
        Socket.Port = customlocalstorage.getInt("backend.port");
    }

    public static void addDefaultConfig() {
        String k = "0-0";
        customlocalstorage.addDefault("redis.address", "127.0.0.1");
        customlocalstorage.addDefault("redis.port", 6374);
        customlocalstorage.addDefault("redis.password", "password");
        customlocalstorage.addDefault("redis.sslEnable", false);
        customlocalstorage.addDefault("redisconfig.mainservername", "client1");
        customlocalstorage.addDefault("lastread.LastID_OUTPUT", k);
        customlocalstorage.addDefault("lastread.LastID_INNER", k);
        customlocalstorage.addDefault("lastread.LastID_BUNGEE", k);
        customlocalstorage.addDefault("backend.address", Socket.Address);
        customlocalstorage.addDefault("backend.port", Socket.Port);
        customlocalstorage.options().copyDefaults(true);
    }
}
