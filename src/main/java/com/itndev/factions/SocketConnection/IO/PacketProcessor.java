package com.itndev.factions.SocketConnection.IO;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.RedisStreams.BungeeAPI.BungeeStorage;
import com.itndev.factions.RedisStreams.StaticVal;
import com.itndev.factions.Utils.SystemUtils;

import java.util.HashMap;
import java.util.List;

public class PacketProcessor {

    public static void run(HashMap<Integer, Object> stream) {
        String ServerName = (String) stream.get(StaticVal.getServerNameArgs());
        String DataType = (String) stream.get(StaticVal.getDataTypeArgs());
                                    /*switch (DataType) {
                                        case "FrontEnd-Chat":
                                            ((List<String>)stream.get(1)).forEach(data -> SystemUtils.PROCCED_INNER2_CHAT(data, ServerName));
                                        case "FrontEnd-Interconnect":
                                            ((List<String>)stream.get(1)).forEach(data -> JedisManager.updatehashmap(data, ServerName));
                                        case "BackEnd-Responce":
                                            ((List<String>)stream.get(1)).forEach(data -> JedisManager.updatehashmap(data, ServerName));
                                        case "BungeeCord-Forward":
                                            ((List<String>)stream.get(1)).forEach(BungeeStorage::READ_Bungee_command);
                                    }*/
        if(DataType.equalsIgnoreCase("FrontEnd-Chat")) {
            ((List<String>)stream.get(1)).forEach(data -> new Thread(() -> SystemUtils.PROCCED_INNER2_CHAT(data, ServerName)).start());
            //System.out.println(1);
        } else if(DataType.equalsIgnoreCase("FrontEnd-Interconnect") || DataType.equalsIgnoreCase("BackEnd-Responce")) {
            ((List<String>)stream.get(1)).forEach(data -> JedisManager.updatehashmap(data, ServerName));
            //System.out.println(2);
        } else {
            ((List<String>)stream.get(1)).forEach(BungeeStorage::READ_Bungee_command);
            //System.out.println(3);
        }
    }
}
