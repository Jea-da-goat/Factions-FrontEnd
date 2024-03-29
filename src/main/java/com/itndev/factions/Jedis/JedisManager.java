package com.itndev.factions.Jedis;

import com.itndev.FaxLib.Utils.Data.DataStream;
import com.itndev.factions.Dump.MySQLDump;
import com.itndev.factions.Listener.PlayerListener;
import com.itndev.factions.Main;
import com.itndev.factions.RedisStreams.StaticVal;
import com.itndev.factions.SocketConnection.Client.NettyClient;
import com.itndev.factions.SocketConnection.IO.ResponceList;
import com.itndev.factions.Storage.CachedStorage;
import com.itndev.factions.Storage.Faction.FactionStorage;
import com.itndev.factions.Storage.UserInfoStorage;
import com.itndev.factions.Utils.BackendIO;
import com.itndev.factions.Utils.CommonUtils;
import com.itndev.factions.Utils.DiscordAuth.DiscordAuth;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JedisManager {

    public static Long JEDISSYNCCLOCK = 1L;

    public static void StartUpJedis() {
        JedisFactory123();
    }

    private static JedisPool jedisPool;
    public static void JedisFactory123() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6614, 300000, "54rg46ujhy7ju57wujndt35ytgryeutwefer4rt34rd34rsfg6hdf43truhgfwgr348yfgcs", false);
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }




    @Deprecated
    public static void jedisTest() {
        JedisFactory123();







        new BukkitRunnable() {



            /*HashMap<String, String> commandlist1 = new HashMap<>();
            HashMap<String, String> commandlist2 = new HashMap<>();
            HashMap<String, String> commandlist3 = new HashMap<>();
            HashMap<String, String> commandlist4 = new HashMap<>();
            HashMap<String, String> chatmsg1 = new HashMap<>();
            HashMap<String, String> chatmsg2 = new HashMap<>();
            HashMap<String, String> chatmsg3 = new HashMap<>();
            HashMap<String, String> chatmsg4 = new HashMap<>();*/



            @Override
            public void run(){



                HashMap<String, String> commandlist1 = new HashMap<>();
                HashMap<String, String> commandlist2 = new HashMap<>();
                HashMap<String, String> commandlist3 = new HashMap<>();
                HashMap<String, String> commandlist4 = new HashMap<>();
                HashMap<String, String> commandlist5 = new HashMap<>(); //not used rn

                HashMap<String, String> chatmsg1 = new HashMap<>();
                HashMap<String, String> chatmsg2 = new HashMap<>();
                HashMap<String, String> chatmsg3 = new HashMap<>();
                HashMap<String, String> chatmsg4 = new HashMap<>();
                HashMap<String, String> chatmsg5 = new HashMap<>(); //not used rn

                HashMap<String, String> bungeeinfo1 = new HashMap<>();
                try(Jedis jedis1 = getJedisPool().getResource()) {
                    if (jedis1.isConnected()) {

                        commandlist1 = (HashMap<String, String>) jedis1.hgetAll("client1");
                        commandlist2 = (HashMap<String, String>) jedis1.hgetAll("client2");
                        commandlist3 = (HashMap<String, String>) jedis1.hgetAll("client3");
                        commandlist4 = (HashMap<String, String>) jedis1.hgetAll("client4");
                        commandlist5 = (HashMap<String, String>) jedis1.hgetAll("client5");

                        chatmsg1 = (HashMap<String, String>) jedis1.hgetAll("client1chatmsg");
                        chatmsg2 = (HashMap<String, String>) jedis1.hgetAll("client2chatmsg");
                        chatmsg3 = (HashMap<String, String>) jedis1.hgetAll("client3chatmsg");
                        chatmsg4 = (HashMap<String, String>) jedis1.hgetAll("client4chatmsg");
                        chatmsg5 = (HashMap<String, String>) jedis1.hgetAll("client5chatmsg");

                        bungeeinfo1 = (HashMap<String, String>) jedis1.hgetAll("bungee1");

                    } else {

                        System.out.println("Not connected to any Redis Server!!!");
                    }
                } catch (Exception e) {

                    System.out.println("[WARNING] " + e.getMessage());
                }


                if (!commandlist1.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist1.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist1.get(String.valueOf(c)), "client1");
                    }
                }
                commandlist1.clear();
                if (!commandlist2.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist2.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist2.get(String.valueOf(c)), "client2");
                    }
                }
                commandlist2.clear();
                if (!commandlist3.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist3.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist3.get(String.valueOf(c)), "client3");
                    }
                }
                commandlist3.clear();
                if (!commandlist4.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist4.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist4.get(String.valueOf(c)), "client4");
                    }
                }
                commandlist4.clear();
                if(!commandlist5.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist5.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist5.get(String.valueOf(c)), "client5");
                    }
                }
                commandlist5.clear();






                //Chat
                if (!chatmsg1.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg1.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg1.get(String.valueOf(c)), "client1");
                    }
                }
                chatmsg1.clear();
                if (!chatmsg2.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg2.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg2.get(String.valueOf(c)), "client2");
                    }
                }
                chatmsg2.clear();
                if (!chatmsg3.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg3.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg3.get(String.valueOf(c)), "client3");
                    }
                }
                chatmsg3.clear();
                if (!chatmsg4.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg4.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg4.get(String.valueOf(c)), "client4");
                    }
                }
                chatmsg4.clear();
                if(!chatmsg5.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg5.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg5.get(String.valueOf(c)), "client5");
                    }
                }
                new Thread( () -> {
                    HashMap<String, String> templands = FactionStorage.LandToFaction;
                    for(String key : templands.keySet()) {
                        FactionStorage.LandToFaction.put(key, templands.get(key));
                    }
                    HashMap<String, String> tempoutposts = FactionStorage.OutPostToFaction;
                    for(String key : tempoutposts.keySet()) {
                        FactionStorage.LandToFaction.put(key, tempoutposts.get(key));
                    }
                }).start();


                JEDISSYNCCLOCK++;

            }

        }.runTaskTimerAsynchronously(Main.getInstance(), 1L, 1L);//runTaskTimer(main.getInstance(), 5L, 5L);




    }

    public static void updatehashmap(String k, String ServerName) {
        if(k.equalsIgnoreCase("-buffer-")) {
            return;
        }
        double c = 1000;
        if(c > 600) {
            String[] args = k.split(":=:");
            if(args[0].equalsIgnoreCase("update")) {
                if(args.length >= 6) {
                    args[3] = CommonUtils.Byte2String(args[3]);
                    args[5] = CommonUtils.Byte2String(args[5]);
                } else if(args.length >= 4) {
                    args[3] = CommonUtils.Byte2String(args[3]);
                }
                if(args[1].equalsIgnoreCase("FactionToLand")
                        || args[1].equalsIgnoreCase("LandToFaction")
                        || args[1].equalsIgnoreCase("FactionRank")
                        || args[1].equalsIgnoreCase("PlayerFaction")
                        || args[1].equalsIgnoreCase("FactionMember")
                        || args[1].equalsIgnoreCase("FactionNameToFactionName")
                        || args[1].equalsIgnoreCase("FactionNameToFactionUUID")
                        || args[1].equalsIgnoreCase("FactionUUIDToFactionName")
                        || args[1].equalsIgnoreCase("FactionInviteQueue")
                        || args[1].equalsIgnoreCase("FactionDTR")
                        || args[1].equalsIgnoreCase("FactionInfo")
                        || args[1].equalsIgnoreCase("FactionInfoList")
                        || args[1].equalsIgnoreCase("Timeout2")
                        || args[1].equalsIgnoreCase("Timeout2info")
                        || args[1].equalsIgnoreCase("FactionOutPost")
                        || args[1].equalsIgnoreCase("FactionOutPostList")
                        || args[1].equalsIgnoreCase("FactionToOutPost")
                        || args[1].equalsIgnoreCase("OutPostToFaction")
                        || args[1].equalsIgnoreCase("DESTORYED_FactionToLand")
                        || args[1].equalsIgnoreCase("DESTORYED_LandToFaction")
                        || args[1].equalsIgnoreCase("DESTROYED_FactionUUIDToFactionName")) {
                    FactionStorage.FactionStorageUpdateHandler(args, ServerName);
                } else if(args[1].equalsIgnoreCase("namename")
                        || args[1].equalsIgnoreCase("nameuuid")
                        || args[1].equalsIgnoreCase("uuidname")) {
                    UserInfoStorage.UserInfoStorageUpdateHandler(args);
                } else if(args[1].equalsIgnoreCase("cachedDTR")
                        || args[1].equalsIgnoreCase("cachedBank")) {
                    CachedStorage.JedisCacheSync(args);
                }
            } else if(args[0].equalsIgnoreCase("ping")) {
                //jedis.c = Math.toIntExact(c + 1);
                if(c == 4) {
                    //jedis.c = 0;
                    System.out.println("Pinged from Redis Database");
                }
            } else if(args[0].equalsIgnoreCase("chat")) {
                String playeruuid = CommonUtils.Byte2String(args[1]);
                String message = CommonUtils.Byte2String(args[2]);
                FactionUtils.FactionChat(playeruuid, message);
                //utils.teamchat(playeruuid, message);
            } else if(args[0].equalsIgnoreCase("sync")) {
                if(Main.ServerName.equals(args[1])) {
                    try {
                        MySQLDump.LoadFromMySQL();
                        List<String> str = new ArrayList<>();
                        str.add("synccomplete");
                        HashMap<Integer, Object> stream = new HashMap<>();
                        stream.put(StaticVal.getServerNameArgs(), Main.ServerName);
                        stream.put(StaticVal.getDataTypeArgs(), "FrontEnd-Output");
                        stream.put(1, str);
                        //DataStream stream = new DataStream(Main.ServerName, "FrontEnd-Output", str);
                        NettyClient.getConnection().writeAndFlush(stream);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if(args[0].equalsIgnoreCase("proxyuserupdate")) {

                //String playeruuid = args[1];
                //if(args[0].equalsIgnoreCase())
                //String targetuuid = args[2];
                //String message = args[3];
                //String trueorfalse = args[4];
                //utils.teamnotify(playeruuid, targetuuid, message, trueorfalse);
            } else if(args[0].equalsIgnoreCase("notify")) {

                String playeruuid = CommonUtils.Byte2String(args[1]);
                String targetuuid = CommonUtils.Byte2String(args[2]);
                String message = CommonUtils.Byte2String(args[3]);
                String trueorfalse = CommonUtils.Byte2String(args[4]);
                FactionUtils.FactionNotify(playeruuid, targetuuid, message, trueorfalse);
                //utils.teamnotify(playeruuid, targetuuid, message, trueorfalse);
            } else if(args[0].equalsIgnoreCase("warplocation")) {
                String targetuuid = CommonUtils.Byte2String(args[1]);
                String Server = args[2];
                String stringlocation = CommonUtils.Byte2String(args[3]);
                String expired = CommonUtils.Byte2String(args[4]);
                if(Main.ServerName.equalsIgnoreCase(Server)) {
                    if(!expired.equalsIgnoreCase("expired")) {
                        Location loc = SystemUtils.string2loc(stringlocation);
                        PlayerListener.onJoinWarp.put(targetuuid, loc);
                    } else {
                        PlayerListener.onJoinWarp.remove(targetuuid);
                    }
                }
            } else if(args[0].equalsIgnoreCase("eco")) {
                String giveortake = args[1];
                String targetuuid = CommonUtils.Byte2String(args[2]);
                String Amount = CommonUtils.Byte2String(args[3]);
                if(Bukkit.getOfflinePlayer(UUID.fromString(targetuuid)).isOnline()) {
                    OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(targetuuid));
                    Double AmountDouble = Double.parseDouble(Amount);
                    if(giveortake.equalsIgnoreCase("give")) {
                        Main.getEconomy().depositPlayer(op, AmountDouble);
                    } else if(giveortake.equalsIgnoreCase("take")) {
                        Main.getEconomy().withdrawPlayer(op, AmountDouble);
                    } else {
                        SystemUtils.warning("INVALID COMMAND USAGE AT ECON COMMAND [give/take/null <-]");
                    }
                }
            } else if(args[0].equalsIgnoreCase("discord")) {
                if(args[1].equalsIgnoreCase("auth")) {
                    DiscordAuth.AcceptAuthInfo(CommonUtils.Byte2String(args[2]), CommonUtils.Byte2String(args[3]));
                }
            } else if(args[0].equalsIgnoreCase("keepalive")) {
                String UUID = CommonUtils.Byte2String(args[1]);
                BackendIO.KeepAlive(UUID, "BLABLABLA");
            } else {
                System.out.println("[WARNING (REDIS)] WRONG COMMAND USAGE FROM REDIS" + " ( '" + k + "' )");
            }


        } else if(c <= 600) {
            String warn = "data update failed... trying to update data that should already been processed or update has been duplicated / processed delayed (" + String.valueOf(c) + ")";
            SystemUtils.warning(warn);
            //utils.broadcastwarn(warn);
        }
        //example command "update:=:hashmap1~4:=:add/remove:=:key:=:add/remove/(앞에 remove일 경우 여기랑 이 뒤는 쓸 필요 없다):=:value"


    }

    public static void updatehashmap(String k) {
        if(k.equalsIgnoreCase("-buffer-")) {
            return;
        }
        double c = 1000;
        if(c > 600) {
            String[] args = k.split(":=:");
            if(args[0].equalsIgnoreCase("update")) {
                if(args[1].equalsIgnoreCase("FactionToLand")
                        || args[1].equalsIgnoreCase("LandToFaction")
                        || args[1].equalsIgnoreCase("FactionRank")
                        || args[1].equalsIgnoreCase("PlayerFaction")
                        || args[1].equalsIgnoreCase("FactionMember")
                        || args[1].equalsIgnoreCase("FactionNameToFactionName")
                        || args[1].equalsIgnoreCase("FactionNameToFactionUUID")
                        || args[1].equalsIgnoreCase("FactionUUIDToFactionName")
                        || args[1].equalsIgnoreCase("FactionInviteQueue")
                        || args[1].equalsIgnoreCase("FactionDTR")
                        || args[1].equalsIgnoreCase("FactionInfo")
                        || args[1].equalsIgnoreCase("FactionInfoList")
                        || args[1].equalsIgnoreCase("Timeout2")
                        || args[1].equalsIgnoreCase("Timeout2info")
                        || args[1].equalsIgnoreCase("FactionOutPost")
                        || args[1].equalsIgnoreCase("FactionOutPostList")
                        || args[1].equalsIgnoreCase("FactionToOutPost")
                        || args[1].equalsIgnoreCase("OutPostToFaction")
                        || args[1].equalsIgnoreCase("DESTORYED_FactionToLand")
                        || args[1].equalsIgnoreCase("DESTORYED_LandToFaction")
                        || args[1].equalsIgnoreCase("DESTROYED_FactionUUIDToFactionName")) {
                    FactionStorage.FactionStorageUpdateHandler(args, Main.ServerName);
                } else if(args[1].equalsIgnoreCase("namename")
                        || args[1].equalsIgnoreCase("nameuuid")
                        || args[1].equalsIgnoreCase("uuidname")) {
                    UserInfoStorage.UserInfoStorageUpdateHandler(args);
                } else if(args[1].equalsIgnoreCase("cachedDTR")
                        || args[1].equalsIgnoreCase("cachedBank")) {
                    CachedStorage.JedisCacheSync(args);
                }
            } else if(args[0].equalsIgnoreCase("ping")) {
                //jedis.c = Math.toIntExact(c + 1);
                if(c == 4) {
                    //jedis.c = 0;
                    System.out.println("Pinged from Redis Database");
                }
            } else if(args[0].equalsIgnoreCase("chat")) {
                String playeruuid = args[1];
                String message = args[2];
                FactionUtils.FactionChat(playeruuid, message);
                //utils.teamchat(playeruuid, message);
            } else if(args[0].equalsIgnoreCase("sync")) {
                if(Main.ServerName.equals(args[1])) {
                    try {
                        MySQLDump.LoadFromMySQL();
                        List<String> str = new ArrayList<>();
                        str.add("synccomplete");
                        HashMap<Integer, Object> stream = new HashMap<>();
                        stream.put(StaticVal.getServerNameArgs(), Main.ServerName);
                        stream.put(StaticVal.getDataTypeArgs(), "FrontEnd-Output");
                        stream.put(1, str);
                        //DataStream stream = new DataStream(Main.ServerName, "FrontEnd-Output", str);
                        NettyClient.getConnection().writeAndFlush(stream);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if(args[0].equalsIgnoreCase("proxyuserupdate")) {

                //String playeruuid = args[1];
                //if(args[0].equalsIgnoreCase())
                //String targetuuid = args[2];
                //String message = args[3];
                //String trueorfalse = args[4];
                //utils.teamnotify(playeruuid, targetuuid, message, trueorfalse);
            } else if(args[0].equalsIgnoreCase("notify")) {

                String playeruuid = args[1];
                String targetuuid = args[2];
                String message = args[3];
                String trueorfalse = args[4];
                FactionUtils.FactionNotify(playeruuid, targetuuid, message, trueorfalse);
                //utils.teamnotify(playeruuid, targetuuid, message, trueorfalse);
            } else if(args[0].equalsIgnoreCase("warplocation")) {
                String targetuuid = args[1];
                String Server = args[2];
                String stringlocation = args[3];
                String expired = args[4];
                if(Main.ServerName.equalsIgnoreCase(Server)) {
                    if(!expired.equalsIgnoreCase("expired")) {
                        Location loc = SystemUtils.string2loc(stringlocation);
                        PlayerListener.onJoinWarp.put(targetuuid, loc);
                    } else {
                        PlayerListener.onJoinWarp.remove(targetuuid);
                    }
                }
            } else if(args[0].equalsIgnoreCase("eco")) {
                String giveortake = args[1];
                String targetuuid = args[2];
                String Amount = args[3];
                if(Bukkit.getOfflinePlayer(UUID.fromString(targetuuid)).isOnline()) {
                    OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(targetuuid));
                    Double AmountDouble = Double.parseDouble(Amount);
                    if(giveortake.equalsIgnoreCase("give")) {
                        Main.getEconomy().depositPlayer(op, AmountDouble);
                    } else if(giveortake.equalsIgnoreCase("take")) {
                        Main.getEconomy().withdrawPlayer(op, AmountDouble);
                    } else {
                        SystemUtils.warning("INVALID COMMAND USAGE AT ECON COMMAND [give/take/null <-]");
                    }
                }
            } else if(args[0].equalsIgnoreCase("discord")) {
                if(args[1].equalsIgnoreCase("auth")) {
                    DiscordAuth.AcceptAuthInfo(args[2], args[3]);
                }
            } else if(args[0].equalsIgnoreCase("keepalive")) {
                String UUID = args[1];
                BackendIO.KeepAlive(UUID, "BLABLABLA");
            } else {
                System.out.println("[WARNING (REDIS)] WRONG COMMAND USAGE FROM REDIS" + " ( '" + k + "' )");
            }


        } else if(c <= 600) {
            String warn = "data update failed... trying to update data that should already been processed or update has been duplicated / processed delayed (" + String.valueOf(c) + ")";
            SystemUtils.warning(warn);
            //utils.broadcastwarn(warn);
        }
        //example command "update:=:hashmap1~4:=:add/remove:=:key:=:add/remove/(앞에 remove일 경우 여기랑 이 뒤는 쓸 필요 없다):=:value"


    }

}
