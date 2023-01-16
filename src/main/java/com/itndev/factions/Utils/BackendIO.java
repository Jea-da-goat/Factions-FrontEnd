package com.itndev.factions.Utils;

import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.RedisStreams.StaticVal;

public class BackendIO {

    private static String CMD_SPLITTER = " ";
    private static String CMD_Announce = "<&@CMD>";
    private static String ADD_Announce = "<&@ADD>";
    private static String USER_Announce = "<&@USER>";
    private static String CMD_ARGS_SPLITTER = ":=:";

    public static void SendCMD_BACKEND(String UUID, String[] args, String Additional) {
        String CMD = StaticVal.getCmdAnnouncer() + " ";
        CMD = CMD + Main.ServerName + CMD_SPLITTER + USER_Announce + CommonUtils.String2Byte(UUID) + CMD_SPLITTER + CMD_Announce + CommonUtils.String2Byte(Args2String_V(args, CMD_ARGS_SPLITTER)) + CMD_SPLITTER + ADD_Announce + CommonUtils.String2Byte(Additional) + CMD_SPLITTER;
        JedisTempStorage.AddCommandToQueue_INPUT(CMD);
    }

    public static void KeepAlive(String UUID, String Nothing) {
        String CMD = StaticVal.getCmdAnnouncer() + " ";
        CMD = CMD + Main.ServerName + CMD_SPLITTER + USER_Announce + CommonUtils.String2Byte(UUID) + CMD_SPLITTER + CMD_Announce + CommonUtils.String2Byte("KEEPALIVECHECK_REDISCLEANUP") + CMD_SPLITTER + ADD_Announce + CommonUtils.String2Byte(Nothing) + CMD_SPLITTER;
        JedisTempStorage.AddCommandToQueue_INPUT(CMD);
    }

    private static String Args2String_V(String[] args, String replacement) {
        String k = "";
        for(int c = 0; c < args.length; c++) {
            if(c == args.length - 1) {
                k = k + CommonUtils.String2Byte(args[c]);
            } else {
                k = k + CommonUtils.String2Byte(args[c]) + replacement;
            }
        }
        return k;
    }
}
