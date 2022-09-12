package com.itndev.factions.SocketConnection.IO;

import com.itndev.FaxLib.Utils.Data.DataStream;
import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.RedisStreams.StaticVal;
import com.itndev.factions.RedisStreams.StreamConfig;
import com.itndev.factions.SocketConnection.Client.Client;
import com.itndev.factions.Utils.JedisUtils;
import com.itndev.factions.Utils.SystemUtils;

import java.io.IOException;
import java.util.*;

public class ResponceList {

    private static ResponceList instance = null;

    private Client client = null;

    public static ResponceList get() {
        if(instance == null) {
            instance = new ResponceList();
        }
        return instance;
    }

    public void set(Client client) {
        this.client = client;
    }

    private ResponceList() {

    }

    public void update(HashMap<Integer, Object> stream) throws IOException {
        client.update(stream);
    }

    public void run() {
        new Thread(() -> {
            while (true) {
                try {
                    synchronized (JedisTempStorage.Temp_INPUT_MAP) {
                        if(!JedisTempStorage.Temp_INPUT_MAP.isEmpty()) {
                            HashMap<Integer, Object> stream = new HashMap<>();
                            stream.put(StaticVal.getServerNameArgs(), Main.ServerName);
                            stream.put(StaticVal.getDataTypeArgs(), "FrontEnd-Output");
                            List<String> temp = new ArrayList<>(JedisTempStorage.Temp_INPUT_MAP.stream().toList());
                            stream.put(1, temp);
                            //DataStream stream = new DataStream(Main.ServerName, "FrontEnd-Output", JedisTempStorage.Temp_INPUT_MAP);
                            JedisTempStorage.Temp_INPUT_MAP.clear();
                            client.update(stream);
                            //System.out.println(ResponceList.class.getCanonicalName());
                        }
                    }
                    Thread.sleep(2);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    synchronized (JedisTempStorage.Temp_INTERCONNECT_MAP) {
                        if(!JedisTempStorage.Temp_INTERCONNECT_MAP.isEmpty()) {
                            //DataStream stream = new DataStream(Main.ServerName, "FrontEnd-Interconnect", JedisTempStorage.Temp_INTERCONNECT_MAP);
                            HashMap<Integer, Object> stream = new HashMap<>();
                            stream.put(StaticVal.getServerNameArgs(), Main.ServerName);
                            stream.put(StaticVal.getDataTypeArgs(), "FrontEnd-Interconnect");
                            List<String> temp = new ArrayList<>(JedisTempStorage.Temp_INTERCONNECT_MAP.stream().toList());
                            stream.put(1, temp);
                            //DataStream stream = new DataStream(Main.ServerName, "FrontEnd-Output", JedisTempStorage.Temp_INPUT_MAP);
                            JedisTempStorage.Temp_INTERCONNECT_MAP.clear();
                            client.update(stream);
                            //System.out.println(ResponceList.class.getCanonicalName());
                        }
                    }
                    Thread.sleep(2);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    synchronized (JedisTempStorage.Temp_INTERCONNECT2_MAP) {
                        if(!JedisTempStorage.Temp_INTERCONNECT2_MAP.isEmpty()) {
                            HashMap<Integer, Object> stream = new HashMap<>();
                            stream.put(StaticVal.getServerNameArgs(), Main.ServerName);
                            stream.put(StaticVal.getDataTypeArgs(), "FrontEnd-Chat");
                            List<String> temp = new ArrayList<>(JedisTempStorage.Temp_INTERCONNECT2_MAP.stream().toList());
                            stream.put(1, temp);
                            JedisTempStorage.Temp_INTERCONNECT2_MAP.clear();
                            client.update(stream);
                            //System.out.println(ResponceList.class.getCanonicalName());
                        }
                    }
                    Thread.sleep(2);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
