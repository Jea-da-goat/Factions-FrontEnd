package com.itndev.factions.SocketConnection.IO;

import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.RedisStreams.StaticVal;
import com.itndev.factions.SocketConnection.Client.Old.Client;

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


    }
}
