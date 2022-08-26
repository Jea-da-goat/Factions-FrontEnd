package com.itndev.factions.RedisStreams.BungeeAPI;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Main;
import com.itndev.factions.RedisStreams.RedisConnection;
import com.itndev.factions.RedisStreams.StaticVal;
import com.itndev.factions.RedisStreams.StreamConfig;
import com.itndev.factions.Utils.JedisUtils;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BungeeStreamReader {

    public static void RedisStreamReader() {

        new Thread(() -> {
            while (true) {
                try {
                    READ_OUTPUT_STREAM();
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(RedisConnection.isClosed() || Main.ShutDown) {
                    break;
                }
            }
        }).start();
        //String lastSeenMessage = "0-0";
    }

    private static void READ_OUTPUT_STREAM() throws ExecutionException, InterruptedException, TimeoutException {
        List<StreamMessage<String, String>> messages = RedisConnection.getAsyncRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_BUNGEE_LINE(), RedisConnection.get_LastID_BUNGEE())).get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);

        for (StreamMessage<String, String> message : messages) {
            RedisConnection.set_LastID_BUNGEE(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            ReadCompressedHashMap_READ(StreamConfig.get_Stream_BUNGEE_LINE(), compressedhashmap);
        }
    }


    private static void ReadCompressedHashMap_READ(String clientname, String compressedhashmap) {
        HashMap<String, String> map = JedisUtils.String2HashMap(compressedhashmap);
        if (!map.isEmpty()) {
            for(int c = 1; c <= Integer.parseInt(map.get(StaticVal.getMaxAmount())); c++) {
                BungeeStorage.READ_Bungee_command(map.get(String.valueOf(c)));
            }
        }
    }
}
