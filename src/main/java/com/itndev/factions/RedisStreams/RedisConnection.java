package com.itndev.factions.RedisStreams;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Storage.Faction.FactionStorage;
import com.itndev.factions.Storage.UserInfoStorage;
import com.itndev.factions.Utils.JedisUtils;
import com.itndev.factions.Utils.SystemUtils;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.api.sync.RedisStreamCommands;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

public class RedisConnection {

    private static RedisClient client = null;
    private static StatefulRedisConnection<String, String> connection = null;
    private static StatefulRedisConnection<String, String> connection2 = null;
    private static RedisAsyncCommands<String, String> asyncCommands = null;
    private static RedisAsyncCommands<String, String> asyncCommands2 = null;
    private static RedisStreamCommands<String, String> commands = null;
    private static RedisCommands<String, String> setcommands = null;
    private static String LastID_OUTPUT = "0-0";
    private static String LastID_INNER = "0-0";
    private static String LastID_BUNGEE = "0-0";
    private static String LastID_INNER2 = "0-0";


    private static String redis_address = "127.0.0.1";
    private static Integer redis_port = 6374;
    private static String redis_password = "password";
    private static Boolean sslEnabled = false;

    private static Boolean closed = false;

    public static Boolean isClosed() {
        return closed;
    }

    public static void setRedis_address(String address) {
        redis_address = address;
    }

    public static void setRedis_port(Integer port) {
        redis_port = port;
    }

    public static void setRedis_password(String password) {
        redis_password = password;
    }

    public static void setSslEnabled(Boolean enabled) {
        sslEnabled = enabled;
    }

    public static String get_LastID_OUTPUT() {
        return LastID_OUTPUT;
    }

    public static void set_LastID_OUTPUT(String data) {
        LastID_OUTPUT = data;
    }

    public static String get_LastID_INNER() {
        return LastID_INNER;
    }

    public static void set_LastID_INNER(String data) {
        LastID_INNER = data;
    }

    public static String get_LastID_BUNGEE() {
        return LastID_BUNGEE;
    }

    public static void set_LastID_BUNGEE(String data) {
        LastID_BUNGEE = data;
    }

    public static String get_LastID_INNER2() {
        return LastID_INNER2;
    }

    public static void set_LastID_INNER2(String data) {
        LastID_INNER2 = data;
    }


    public static void RedisConnect() {
        RedisURI redisURI = RedisURI.Builder.redis(redis_address, redis_port).withPassword(redis_password.toCharArray()).build();
        client = RedisClient.create(redisURI);
        connection = client.connect();
        connection2 = client.connect();
        commands = connection.sync();
        setcommands = connection.sync();
        
    }

    public static void RemoveUploadedStorage(String key) {
        if(key == null) {
            setcommands.del("FactionInfo");
            setcommands.del("FactionMember");
            setcommands.del("FactionNameToFactionName");
            setcommands.del("FactionInfoList");
            setcommands.del("FactionNameToFactionUUID");
            setcommands.del("FactionOutPost");
            setcommands.del("FactionOutPostList");
            setcommands.del("FactionRank");
            setcommands.del("FactionToLand");
            setcommands.del("FactionToOutPost");
            setcommands.del("FactionUUIDToFactionName");
            setcommands.del("FactionWarpLocations");
            setcommands.del("OutPostToFaction");
            setcommands.del("PlayerFaction");
            setcommands.del("namename");
            setcommands.del("nameuuid");
            setcommands.del("uuidname");
        } else {
            setcommands.del(key + "-" + "FactionInfo");
            setcommands.del(key + "-" + "FactionMember");
            setcommands.del(key + "-" + "FactionNameToFactionName");
            setcommands.del(key + "-" + "FactionInfoList");
            setcommands.del(key + "-" + "FactionNameToFactionUUID");
            setcommands.del(key + "-" + "FactionOutPost");
            setcommands.del(key + "-" + "FactionOutPostList");
            setcommands.del(key + "-" + "FactionRank");
            setcommands.del(key + "-" + "FactionToLand");
            setcommands.del(key + "-" + "FactionToOutPost");
            setcommands.del(key + "-" + "FactionUUIDToFactionName");
            setcommands.del(key + "-" + "FactionWarpLocations");
            setcommands.del(key + "-" + "OutPostToFaction");
            setcommands.del(key + "-" + "PlayerFaction");
            setcommands.del(key + "-" + "namename");
            setcommands.del(key + "-" + "nameuuid");
            setcommands.del(key + "-" + "uuidname");
        }
    }

    public static void UploadStorageToRedis(String key) {
        Long expiretime = System.currentTimeMillis() + 60000;
        if(key == null) {
            if (!FactionStorage.FactionInfo.isEmpty()) {
                setcommands.hmset("FactionInfo", FactionStorage.FactionInfo);
                setcommands.expireat("FactionInfo", expiretime);
            }

            //FactionStorage.AsyncLandToFaction = (ConcurrentHashMap<String, String>) setcommands.hmget("FactionInfo");
            if(!FactionStorage.FactionMember.isEmpty()) {
                setcommands.hmset("FactionMember", ConcurrentListMapConvert(FactionStorage.FactionMember));
                setcommands.expireat("FactionMember", expiretime);
            }

            if(!FactionStorage.FactionNameToFactionName.isEmpty()) {
                setcommands.hmset("FactionNameToFactionName", FactionStorage.FactionNameToFactionName);
                setcommands.expireat("FactionNameToFactionName", expiretime);
            }

            if(!FactionStorage.FactionInfoList.isEmpty()) {
                setcommands.hmset("FactionInfoList", ConcurrentListMapConvert(FactionStorage.FactionInfoList));
                setcommands.expireat("FactionInfoList", expiretime);
            }
            //FactionStorage.AsyncOutPostToFaction;

            if(!FactionStorage.FactionNameToFactionUUID.isEmpty()) {
                setcommands.hmset("FactionNameToFactionUUID", FactionStorage.FactionNameToFactionUUID);
                setcommands.expireat("FactionNameToFactionUUID", expiretime);
            }

            if(!FactionStorage.FactionOutPost.isEmpty()) {
                setcommands.hmset("FactionOutPost", FactionStorage.FactionOutPost);
                setcommands.expireat("FactionOutPost", expiretime);
            }

            if(!FactionStorage.FactionOutPostList.isEmpty()) {
                setcommands.hmset("FactionOutPostList", ConcurrentListMapConvert(FactionStorage.FactionOutPostList));
                setcommands.expireat("FactionOutPostList", expiretime);
            }

            if(!FactionStorage.FactionRank.isEmpty()) {
                setcommands.hmset("FactionRank", FactionStorage.FactionRank);
                setcommands.expireat("FactionRank", expiretime);
            }

            if(!FactionStorage.FactionToLand.isEmpty()) {
                setcommands.hmset("FactionToLand", ListMapConvert(FactionStorage.FactionToLand));
                setcommands.expireat("FactionToLand", expiretime);
            }

            if(!FactionStorage.FactionToOutPost.isEmpty()) {
                setcommands.hmset("FactionToOutPost", ListMapConvert(FactionStorage.FactionToOutPost));
                setcommands.expireat("FactionToOutPost", expiretime);
            }

            if(!FactionStorage.FactionUUIDToFactionName.isEmpty()) {
                setcommands.hmset("FactionUUIDToFactionName", FactionStorage.FactionUUIDToFactionName);
                setcommands.expireat("FactionUUIDToFactionName", expiretime);
            }

            if(!FactionStorage.FactionWarpLocations.isEmpty()) {
                setcommands.hmset("FactionWarpLocations", FactionStorage.FactionWarpLocations);
                setcommands.expireat("FactionWarpLocations", expiretime);
            }

            if(!FactionStorage.OutPostToFaction.isEmpty()) {
                setcommands.hmset("OutPostToFaction", FactionStorage.OutPostToFaction);
                setcommands.expireat("OutPostToFaction", expiretime);
            }

            if(!FactionStorage.PlayerFaction.isEmpty()) {
                setcommands.hmset("PlayerFaction", FactionStorage.PlayerFaction);
                setcommands.expireat("PlayerFaction", expiretime);
            }

            if(!UserInfoStorage.namename.isEmpty()) {
                setcommands.hmset("namename", UserInfoStorage.namename);
                setcommands.expireat("namename", expiretime);
            }

            if(!UserInfoStorage.nameuuid.isEmpty()) {
                setcommands.hmset("nameuuid", UserInfoStorage.nameuuid);
                setcommands.expireat("nameuuid", expiretime);
            }

            if(!UserInfoStorage.uuidname.isEmpty()) {
                setcommands.hmset("uuidname", UserInfoStorage.uuidname);
                setcommands.expireat("uuidname", expiretime);
            }
        } else {
            if (!FactionStorage.FactionInfo.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionInfo", FactionStorage.FactionInfo);
                setcommands.expireat(key + "-" + "FactionInfo", expiretime);
            }

            //FactionStorage.AsyncLandToFaction = (ConcurrentHashMap<String, String>) setcommands.hmget("FactionInfo");
            if(!FactionStorage.FactionMember.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionMember", ConcurrentListMapConvert(FactionStorage.FactionMember));
                setcommands.expireat(key + "-" + "FactionMember", expiretime);
            }

            if(!FactionStorage.FactionNameToFactionName.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionNameToFactionName", FactionStorage.FactionNameToFactionName);
                setcommands.expireat(key + "-" + "FactionNameToFactionName", expiretime);
            }

            if(!FactionStorage.FactionInfoList.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionInfoList", ConcurrentListMapConvert(FactionStorage.FactionInfoList));
                setcommands.expireat(key + "-" + "FactionInfoList", expiretime);
            }
            //FactionStorage.AsyncOutPostToFaction;

            if(!FactionStorage.FactionNameToFactionUUID.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionNameToFactionUUID", FactionStorage.FactionNameToFactionUUID);
                setcommands.expireat(key + "-" + "FactionNameToFactionUUID", expiretime);
            }

            if(!FactionStorage.FactionOutPost.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionOutPost", FactionStorage.FactionOutPost);
                setcommands.expireat(key + "-" + "FactionOutPost", expiretime);
            }

            if(!FactionStorage.FactionOutPostList.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionOutPostList", ConcurrentListMapConvert(FactionStorage.FactionOutPostList));
                setcommands.expireat(key + "-" + "FactionOutPostList", expiretime);
            }

            if(!FactionStorage.FactionRank.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionRank", FactionStorage.FactionRank);
                setcommands.expireat(key + "-" + "FactionRank", expiretime);
            }

            if(!FactionStorage.FactionToLand.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionToLand", ListMapConvert(FactionStorage.FactionToLand));
                setcommands.expireat(key + "-" + "FactionToLand", expiretime);
            }

            if(!FactionStorage.FactionToOutPost.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionToOutPost", ListMapConvert(FactionStorage.FactionToOutPost));
                setcommands.expireat(key + "-" + "FactionToOutPost", expiretime);
            }

            if(!FactionStorage.FactionUUIDToFactionName.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionUUIDToFactionName", FactionStorage.FactionUUIDToFactionName);
                setcommands.expireat(key + "-" + "FactionUUIDToFactionName", expiretime);
            }

            if(!FactionStorage.FactionWarpLocations.isEmpty()) {
                setcommands.hmset(key + "-" + "FactionWarpLocations", FactionStorage.FactionWarpLocations);
                setcommands.expireat(key + "-" + "FactionWarpLocations", expiretime);
            }

            if(!FactionStorage.OutPostToFaction.isEmpty()) {
                setcommands.hmset(key + "-" + "OutPostToFaction", FactionStorage.OutPostToFaction);
                setcommands.expireat(key + "-" + "OutPostToFaction", expiretime);
            }

            if(!FactionStorage.PlayerFaction.isEmpty()) {
                setcommands.hmset(key + "-" + "PlayerFaction", FactionStorage.PlayerFaction);
                setcommands.expireat(key + "-" + "PlayerFaction", expiretime);
            }

            if(!UserInfoStorage.namename.isEmpty()) {
                setcommands.hmset(key + "-" + "namename", UserInfoStorage.namename);
                setcommands.expireat(key + "-" + "namename", expiretime);
            }

            if(!UserInfoStorage.nameuuid.isEmpty()) {
                setcommands.hmset(key + "-" + "nameuuid", UserInfoStorage.nameuuid);
                setcommands.expireat(key + "-" + "nameuuid", expiretime);
            }

            if(!UserInfoStorage.uuidname.isEmpty()) {
                setcommands.hmset(key + "-" + "uuidname", UserInfoStorage.uuidname);
                setcommands.expireat(key + "-" + "uuidname", expiretime);
            }
        }

    }

    private static HashMap<String, String> ListMapConvert(HashMap<String, ArrayList<String>> map) {
        HashMap finalMap = new HashMap<String, String>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalMap.put(key, SystemUtils.list2string(map.get(key)));
            }
        }
        return finalMap;
    }

    private static HashMap<String, String> ConcurrentListMapConvert(ConcurrentHashMap<String, ArrayList<String>> map) {
        synchronized (map) {
            HashMap finalMap = new HashMap<String, String>();
            if(!map.isEmpty()) {
                for(String key : map.keySet()) {
                    finalMap.put(key, SystemUtils.list2string(map.get(key)));
                }
            }
            return finalMap;
        }
    }

    public static void ReloadStorageFromRemoteServer(String key) {
        FactionStorage.FactionInfo = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "FactionInfo");
        //FactionStorage.AsyncLandToFaction = (ConcurrentHashMap<String, String>) setcommands.hmget("FactionInfo");
        HashMap<String, String> tempmap = new HashMap<>();
        tempmap = (HashMap<String, String>) setcommands.hmget(key + "-" + "FactionMember");
        FactionStorage.FactionMember = ConcurrentMapListConvert(tempmap);
        //
        FactionStorage.FactionNameToFactionName = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "FactionNameToFactionName");
        //FactionStorage.AsyncOutPostToFaction;
        tempmap = (HashMap<String, String>) setcommands.hmget(key + "-" + "FactionInfoList");
        FactionStorage.FactionInfoList = ConcurrentMapListConvert(tempmap);
        FactionStorage.FactionNameToFactionUUID = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "FactionNameToFactionUUID");

        FactionStorage.FactionOutPost = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "FactionOutPost");

        tempmap = (HashMap<String, String>) setcommands.hmget(key + "-" + "FactionOutPostList");
        FactionStorage.FactionOutPostList = ConcurrentMapListConvert(tempmap);

        FactionStorage.FactionRank = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "FactionRank");

        tempmap = (HashMap<String, String>) setcommands.hmget(key + "-" + "FactionToLand");
        FactionStorage.FactionToLand = MapListConvert(tempmap);

        tempmap = (HashMap<String, String>) setcommands.hmget(key + "-" + "FactionToOutPost");
        FactionStorage.FactionToOutPost = MapListConvert(tempmap);

        FactionStorage.FactionUUIDToFactionName = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "FactionUUIDToFactionName");
        FactionStorage.FactionWarpLocations = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "FactionWarpLocations");
        FactionStorage.OutPostToFaction = (HashMap<String, String>) setcommands.hmget(key + "-" + "OutPostToFaction");
        FactionStorage.PlayerFaction = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "PlayerFaction");

        UserInfoStorage.namename = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "namename");
        UserInfoStorage.uuidname = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "uuidname");
        UserInfoStorage.nameuuid = (ConcurrentHashMap<String, String>) setcommands.hmget(key + "-" + "nameuuid");
    }

    private static HashMap<String, ArrayList<String>> MapListConvert(HashMap<String, String> map) {
        HashMap finalMap = new HashMap<String, String>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalMap.put(key, SystemUtils.string2list(map.get(key)));
            }
        }
        return finalMap;
    }

    private static ConcurrentHashMap<String, ArrayList<String>> ConcurrentMapListConvert(HashMap<String, String> map) {
        ConcurrentHashMap finalMap = new ConcurrentHashMap<String, String>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalMap.put(key, SystemUtils.string2list(map.get(key)));
            }
        }
        return finalMap;
    }

    public static void RedisDisConnect() {
        connection.close();
        connection2.close();
        closed = true;
    }

    private static StatefulRedisConnection<String, String> getRedisConnection() {
        if (connection == null || !connection.isOpen()) {
            connection = client.connect();
        }
        return connection;
    }

    private static StatefulRedisConnection<String, String> getRedisConnection2() {
        if (connection2 == null || !connection2.isOpen()) {
            connection2 = client.connect();
        }
        return connection2;
    }

    public static RedisStreamCommands<String, String> getRedisCommands() {
        if(commands == null) {
            commands = getRedisConnection().sync();
        }
        return commands;
    }

    public static RedisAsyncCommands<String, String> getAsyncRedisCommands() {
        if(asyncCommands == null) {
            asyncCommands = getRedisConnection().async();
        }
        return asyncCommands;
    }

    public static RedisAsyncCommands<String, String> getAsyncRedisCommands2() {
        if(asyncCommands2 == null) {
            asyncCommands2 = getRedisConnection2().async();
        }
        return asyncCommands2;
    }

    public static void RedisStreamReader() {

        new Thread(() -> {
            Thread.currentThread().setPriority(1);
            while (true) {
                try {
                    READ_OUTPUT_STREAM();
                    READ_INNER_STREAM();
                    Thread.sleep(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(closed) {
                    break;
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    READ_INNER2_STREAM();
                    Thread.sleep(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(closed) {
                    break;
                }
            }
        }).start();


        //String lastSeenMessage = "0-0";

    }

    private static void READ_STREAM_ASYNC() throws ExecutionException, InterruptedException, TimeoutException {
        RedisFuture<List<StreamMessage<String, String>>> OUTPUT = getAsyncRedisCommands().xread(XReadArgs.Builder.block(Duration.ofMillis(100)),
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_OUTPUT_NAME(), get_LastID_OUTPUT()));
        RedisFuture<List<StreamMessage<String, String>>> INTER = getAsyncRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INTERCONNECT_NAME(), get_LastID_INNER()));
        RedisFuture<List<StreamMessage<String, String>>> INTER2 = getAsyncRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INTERCONNECT2_NAME(), get_LastID_INNER2()));
        Thread.sleep(5);
        List<StreamMessage<String, String>> OUTPUT_RESPONCE = OUTPUT.get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);
        List<StreamMessage<String, String>> INTER_RESPONCE = INTER.get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);
        List<StreamMessage<String, String>> INTER2_RESPONCE = INTER2.get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);
        for (StreamMessage<String, String> message : OUTPUT_RESPONCE) {
            set_LastID_OUTPUT(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            ReadCompressedHashMap_READ(StreamConfig.get_Stream_OUTPUT_NAME(), compressedhashmap);
        }
        for (StreamMessage<String, String> message : INTER_RESPONCE) {
            set_LastID_INNER(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            ReadCompressedHashMap_READ(StreamConfig.get_Stream_INTERCONNECT_NAME(), compressedhashmap);
        }
        Thread PROCESS_MAIN_CHAT = new Thread(() -> {
            for (StreamMessage<String, String> message : INTER2_RESPONCE) {
                set_LastID_INNER2(message.getId());
                String compressedhashmap = message.getBody().get(StaticVal.getCommand());
                ReadCompressedHashMap_READ_INNER2(StreamConfig.get_Stream_INTERCONNECT2_NAME(), compressedhashmap);
            }
        });
        PROCESS_MAIN_CHAT.start();
    }

    private static void READ_OUTPUT_STREAM() throws ExecutionException, InterruptedException, TimeoutException {
        List<StreamMessage<String, String>> messages = getAsyncRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_OUTPUT_NAME(), get_LastID_OUTPUT())).get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);

        for (StreamMessage<String, String> message : messages) {
            set_LastID_OUTPUT(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            ReadCompressedHashMap_READ(StreamConfig.get_Stream_OUTPUT_NAME(), compressedhashmap);
        }
    }

    private static void READ_INNER_STREAM() throws ExecutionException, InterruptedException, TimeoutException {
        List<StreamMessage<String, String>> messages = getAsyncRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INTERCONNECT_NAME(), get_LastID_INNER())).get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);
        for (StreamMessage<String, String> message : messages) {
            set_LastID_INNER(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            ReadCompressedHashMap_READ(StreamConfig.get_Stream_INTERCONNECT_NAME(), compressedhashmap);
        }
    }

    private static void READ_INNER2_STREAM() throws ExecutionException, InterruptedException, TimeoutException {
        RedisFuture<List<StreamMessage<String, String>>> INTER2 = getAsyncRedisCommands2().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INTERCONNECT2_NAME(), get_LastID_INNER2()));
        List<StreamMessage<String, String>> INTER2_RESPONCE = INTER2.get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);

        for (StreamMessage<String, String> message : INTER2_RESPONCE) {
            set_LastID_INNER2(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            ReadCompressedHashMap_READ_INNER2(StreamConfig.get_Stream_INTERCONNECT2_NAME(), compressedhashmap);
        }
    }


    private static void ReadCompressedHashMap_READ(String clientname, String compressedhashmap) {
        HashMap<String, String> map = JedisUtils.String2HashMap(compressedhashmap);
        if (!map.isEmpty()) {
            for(int c = 1; c <= Integer.parseInt(map.get(StaticVal.getMaxAmount())); c++) {
                JedisManager.updatehashmap(map.get(String.valueOf(c)), clientname);
            }
        }
    }

    private static void ReadCompressedHashMap_READ_INNER2(String clientname, String compressedhashmap) {
        HashMap<String, String> map = JedisUtils.String2HashMap(compressedhashmap);
        if (!map.isEmpty()) {
            for(int c = 1; c <= Integer.parseInt(map.get(StaticVal.getMaxAmount())); c++) {
                SystemUtils.PROCCED_INNER2_CHAT(map.get(String.valueOf(c)), clientname);
            }
        }
    }

    public static void RedisStreamWriter() {
        new Thread(() -> {
            Thread.currentThread().setPriority(1);
            while (true) {
                try {
                    String compressedhashmap;
                    synchronized (JedisTempStorage.Temp_INPUT_MAP) {
                        //compressedhashmap = JedisUtils.HashMap2String(JedisTempStorage.Temp_INPUT_MAP);
                        JedisTempStorage.Temp_INPUT_MAP.clear();
                    }
                    //if(compressedhashmap != null) {
                    //    Map<String, String> body = Collections.singletonMap(StaticVal.getCommand(), compressedhashmap);
                    //    getAsyncRedisCommands().xadd(StreamConfig.get_Stream_INPUT_NAME(), body);
                    //}
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(closed) {
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    String compressedhashmap;
                    synchronized (JedisTempStorage.Temp_INTERCONNECT_MAP) {
                        compressedhashmap = null;//JedisUtils.HashMap2String(JedisTempStorage.Temp_INTERCONNECT_MAP);
                        JedisTempStorage.Temp_INTERCONNECT_MAP.clear();
                    }
                    if(compressedhashmap != null) {
                        Map<String, String> body = Collections.singletonMap(StaticVal.getCommand(), compressedhashmap);
                        getAsyncRedisCommands().xadd(StreamConfig.get_Stream_INTERCONNECT_NAME(), body);
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(closed) {
                    break;
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    String compressedhashmap;
                    synchronized (JedisTempStorage.Temp_INTERCONNECT2_MAP) {
                        //compressedhashmap = JedisUtils.HashMap2String(JedisTempStorage.Temp_INTERCONNECT2_MAP);
                        JedisTempStorage.Temp_INTERCONNECT2_MAP.clear();
                    }
                    if(false){//compressedhashmap != null) {
                        Map<String, String> body = Collections.singletonMap(StaticVal.getCommand(), compressedhashmap);
                        getAsyncRedisCommands().xadd(StreamConfig.get_Stream_INTERCONNECT2_NAME(), body);
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(closed) {
                    break;
                }
            }
        }).start();
    }
}
