package com.itndev.factions.Utils;

import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;

public class BackendIO {

    private static String CMD_SPLITTER = "<CMD>&%CMD_12%<CMD>";
    private static String CMD_Announce = "<&@CMD>";
    private static String ADD_Announce = "<&@ADD>";
    private static String USER_Announce = "<&@USER>";
    private static String CMD_ARGS_SPLITTER = ":=:";

    public static void SendCMD_BACKEND(String UUID, String[] args, String Additional) {
        String CMD = CMD_SPLITTER;
        CMD = CMD + Main.ServerName + CMD_SPLITTER + USER_Announce + UUID + CMD_SPLITTER + CMD_Announce + Args2String_V(args, CMD_ARGS_SPLITTER) + CMD_SPLITTER + ADD_Announce + Additional + CMD_SPLITTER;
        JedisTempStorage.AddCommandToQueue_INPUT(CMD);
    }

    private static String Args2String_V(String[] args, String replacement) {
        String k = "";
        for(int c = 0; c < args.length; c++) {
            if(c == args.length - 1) {
                k = k + args[c];
            } else {
                k = k + args[c] + replacement;
            }
        }
        return k;
    }
}
