package com.itndev.factions.Utils;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.RedisStreams.StaticVal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JedisUtils {
    private static String splitter = "/=&C&:&G&:&1&=/";
    private static String buffer = "-buffer-";

    @Deprecated
    public static Boolean HashMapUpdate(HashMap<String, String> map, String ServerName) {
        if (!map.isEmpty()) {
            for(int c = 1; c <= Integer.parseInt(map.get(StaticVal.getMaxAmount())); c++) {
                JedisManager.updatehashmap(map.get(String.valueOf(c)), ServerName);
            }
            return true;
        }
        return false;
    }

    public static String HashMap2String(HashMap<String, String> map) {
        String finalbuildstring = buffer + splitter;
        if(!map.isEmpty()) {
            int maxamount = Integer.parseInt(map.get(StaticVal.getMaxAmount()));
            for (int c = 1; c <= maxamount; c++) {
                if (c == maxamount) {
                    finalbuildstring = finalbuildstring + map.get(String.valueOf(c)) + splitter + buffer;
                } else {
                    finalbuildstring = finalbuildstring + map.get(String.valueOf(c)) + splitter;
                }
            }
        } else {
            return null;
        }
        return finalbuildstring;
    }

    public static HashMap<String, String> String2HashMap(String info) {
        HashMap<String, String> finalmap = new HashMap<>();
        if(!info.contains(splitter)) {
            if(info.length() > 0) {
                finalmap.put("1", info);
                finalmap.put(StaticVal.getMaxAmount(), "1");
            }
            return finalmap;
        }
        String[] info_args = info.split(splitter);
        finalmap.put(StaticVal.getMaxAmount(), String.valueOf(info_args.length - 1));
        for(int c = 0; c < info_args.length; c++) {
            finalmap.put(String.valueOf(c + 1), info_args[c]);
        }
        return finalmap;
    }
}
