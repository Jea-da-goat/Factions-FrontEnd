package com.itndev.factions.SocketConnection.Client;

import com.comphenix.protocol.PacketType;
import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.RedisStreams.BungeeAPI.BungeeStorage;
import com.itndev.factions.RedisStreams.StaticVal;
import com.itndev.factions.SocketConnection.IO.ResponceList;
import com.itndev.factions.Utils.JedisUtils;
import com.itndev.factions.Utils.SystemUtils;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Client {

    private String hostname;
    private int port;

    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;


    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.run();
    }

    public void update(HashMap<String, String> map) throws IOException {
        if(output == null) {
            System.out.println("OutputStream is Null");
            return;
        }
        output.writeObject(map);
        output.flush();
    }

    private void run() {
        new Thread(() -> {
            while(true) {
                try {
                    clientSocket = new Socket(this.hostname, this.port);
                    //output = new PrintStream(clientSocket.getOutputStream());

                    //output.println("Connection Enabled");

                    //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    output = new ObjectOutputStream(clientSocket.getOutputStream());
                    input = new ObjectInputStream(clientSocket.getInputStream());

                    Boolean close = false;
                    while(!close) {
                        try {
                            HashMap<String, String> map;
                            try {
                                map = (HashMap<String, String>) input.readObject();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                map = new HashMap<>();
                            }
                            if(map != null && !map.isEmpty()) {
                                String ServerName;
                                String DataType;
                                ServerName = map.getOrDefault(StaticVal.getServerNameArgs(), "");
                                DataType = map.getOrDefault(StaticVal.getDataTypeArgs(), "");
                                if(DataType.equalsIgnoreCase("FrontEnd-Chat")) {
                                    for (int c = 1; c <= Integer.parseInt(map.get(StaticVal.getMaxAmount())); c++) {
                                        SystemUtils.PROCCED_INNER2_CHAT(map.get(String.valueOf(c)), ServerName);
                                    }
                                } else if(DataType.equalsIgnoreCase("FrontEnd-Interconnect")) {
                                    for (int c = 1; c <= Integer.parseInt(map.get(StaticVal.getMaxAmount())); c++) {
                                        JedisManager.updatehashmap(map.get(String.valueOf(c)), ServerName);
                                    }
                                } else {
                                    for (int c = 1; c <= Integer.parseInt(map.get(StaticVal.getMaxAmount())); c++) {
                                        BungeeStorage.READ_Bungee_command(map.get(String.valueOf(c)));
                                    }
                                }
                            } else {
                                close = true;
                                this.closeAll();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            close = true;
                        }
                    }
                    Thread.sleep(100);
                } catch (IOException | InterruptedException e) {

                    this.closeAll();
                    System.out.println("e -< error Report");
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


    }

    public void closeAll() {
        try {
            output.writeObject(new HashMap<String, String>());
            output.flush();
        } catch (IOException ex) {
            System.out.println("ex -< error Report");
            ex.printStackTrace();
        }
        try {
            clientSocket.close();
        } catch (IOException ex) {
            System.out.println("ex -< error Report");
            ex.printStackTrace();
        }
    }
}
