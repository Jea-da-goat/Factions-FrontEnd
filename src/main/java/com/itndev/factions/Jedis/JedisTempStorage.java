package com.itndev.factions.Jedis;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JedisTempStorage {

    public static final ConcurrentHashMap<String, String> Temp_INPUT_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, String> Temp_INTERCONNECT_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, String> Temp_INTERCONNECT2_MAP = new ConcurrentHashMap<>();
    //public static final ConcurrentHashMap<String, String> TempChatQueue = new ConcurrentHashMap<>();


    public static void AddCommandToQueue_INPUT(String command) {
        synchronized (Temp_INPUT_MAP) {
            if(!Temp_INPUT_MAP.containsKey("MAXAMOUNT")) {
                Temp_INPUT_MAP.put("MAXAMOUNT", "1");
                Temp_INPUT_MAP.put("1", command);
            } else {
                int num = Integer.parseInt(Temp_INPUT_MAP.get("MAXAMOUNT"));
                Temp_INPUT_MAP.put("MAXAMOUNT", String.valueOf(num + 1));
                Temp_INPUT_MAP.put(String.valueOf(num + 1), command);
            }
        }
    }

    public static void AddCommandToQueueFix_INPUT(String command, String nothing) {
        synchronized (Temp_INPUT_MAP) {
            if (!Temp_INPUT_MAP.containsKey("MAXAMOUNT")) {
                Temp_INPUT_MAP.put("MAXAMOUNT", "1");
                Temp_INPUT_MAP.put("1", command);
            } else {
                int num = Integer.parseInt(Temp_INPUT_MAP.get("MAXAMOUNT"));
                Temp_INPUT_MAP.put("MAXAMOUNT", String.valueOf(num + 1));
                Temp_INPUT_MAP.put(String.valueOf(num + 1), command);
            }
        }
    }


    public static void AddBulkCommandToQueue_INPUT(List<String> BulkCMD) {
        synchronized (Temp_INPUT_MAP) {
            for(String command : BulkCMD) {
                if(!Temp_INPUT_MAP.containsKey("MAXAMOUNT")) {
                    Temp_INPUT_MAP.put("MAXAMOUNT", "1");
                    Temp_INPUT_MAP.put("1", command);
                } else {
                    int num = Integer.parseInt(Temp_INPUT_MAP.get("MAXAMOUNT"));
                    Temp_INPUT_MAP.put("MAXAMOUNT", String.valueOf(num + 1));
                    Temp_INPUT_MAP.put(String.valueOf(num + 1), command);
                }
            }
        }
    }

    public static void AddCommandToQueue_INNER(String command) {
        synchronized (Temp_INTERCONNECT_MAP) {
            if(!Temp_INTERCONNECT_MAP.containsKey("MAXAMOUNT")) {
                Temp_INTERCONNECT_MAP.put("MAXAMOUNT", "1");
                Temp_INTERCONNECT_MAP.put("1", command);
            } else {
                int num = Integer.parseInt(Temp_INTERCONNECT_MAP.get("MAXAMOUNT"));
                Temp_INTERCONNECT_MAP.put("MAXAMOUNT", String.valueOf(num + 1));
                Temp_INTERCONNECT_MAP.put(String.valueOf(num + 1), command);
            }
        }
    }

    public static void AddCommandToQueueFix_INNER(String command, String nothing) {
        synchronized (Temp_INTERCONNECT_MAP) {
            if (!Temp_INTERCONNECT_MAP.containsKey("MAXAMOUNT")) {
                Temp_INTERCONNECT_MAP.put("MAXAMOUNT", "1");
                Temp_INTERCONNECT_MAP.put("1", command);
            } else {
                int num = Integer.parseInt(Temp_INTERCONNECT_MAP.get("MAXAMOUNT"));
                Temp_INTERCONNECT_MAP.put("MAXAMOUNT", String.valueOf(num + 1));
                Temp_INTERCONNECT_MAP.put(String.valueOf(num + 1), command);
            }
        }
    }


    public static void AddBulkCommandToQueue_INNER(List<String> BulkCMD) {
        synchronized (Temp_INTERCONNECT_MAP) {
            for(String command : BulkCMD) {
                if(!Temp_INTERCONNECT_MAP.containsKey("MAXAMOUNT")) {
                    Temp_INTERCONNECT_MAP.put("MAXAMOUNT", "1");
                    Temp_INTERCONNECT_MAP.put("1", command);
                } else {
                    int num = Integer.parseInt(Temp_INTERCONNECT_MAP.get("MAXAMOUNT"));
                    Temp_INTERCONNECT_MAP.put("MAXAMOUNT", String.valueOf(num + 1));
                    Temp_INTERCONNECT_MAP.put(String.valueOf(num + 1), command);
                }
            }
        }
    }

    public static void AddCommandToQueue_INNER2(String command) {
        synchronized (Temp_INTERCONNECT2_MAP) {
            if(!Temp_INTERCONNECT2_MAP.containsKey("MAXAMOUNT")) {
                Temp_INTERCONNECT2_MAP.put("MAXAMOUNT", "1");
                Temp_INTERCONNECT2_MAP.put("1", command);
            } else {
                int num = Integer.parseInt(Temp_INTERCONNECT2_MAP.get("MAXAMOUNT"));
                Temp_INTERCONNECT2_MAP.put("MAXAMOUNT", String.valueOf(num + 1));
                Temp_INTERCONNECT2_MAP.put(String.valueOf(num + 1), command);
            }
        }
    }

}
