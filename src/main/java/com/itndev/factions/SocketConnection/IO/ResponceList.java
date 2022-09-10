package com.itndev.factions.SocketConnection.IO;

import com.itndev.FaxLib.Utils.Data.DataStream;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.RedisStreams.StaticVal;
import com.itndev.factions.RedisStreams.StreamConfig;
import com.itndev.factions.SocketConnection.Client.Client;
import com.itndev.factions.Utils.JedisUtils;
import com.itndev.factions.Utils.SystemUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public void run() {
        new Thread(() -> {
            while (true) {
                try {
                    synchronized (JedisTempStorage.Temp_INPUT_MAP) {
                        if(!JedisTempStorage.Temp_INPUT_MAP.isEmpty()) {
                            DataStream stream = new DataStream(Main.ServerName, "FrontEnd-Output", JedisTempStorage.Temp_INPUT_MAP);
                            client.update(stream);
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
                            DataStream stream = new DataStream(Main.ServerName, "FrontEnd-Interconnect", JedisTempStorage.Temp_INTERCONNECT_MAP);
                            client.update(stream);
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
                            DataStream stream = new DataStream(Main.ServerName, "FrontEnd-Chat", JedisTempStorage.Temp_INTERCONNECT2_MAP);
                            client.update(stream);
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
