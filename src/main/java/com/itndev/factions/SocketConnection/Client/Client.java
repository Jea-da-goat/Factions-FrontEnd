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

    public synchronized void update(HashMap<Integer, String> map) throws IOException {
        if(output == null) {
            System.out.println("OutputStream is Null");
            return;
        }
        output.writeObject(map);
        output.flush();
    }

    private synchronized void run() {
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
                            HashMap<Integer, String> map;
                            try {
                                map = (HashMap<Integer, String>) input.readObject();
                            } catch (StreamCorruptedException e){
                                map = new HashMap<>();
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
                                    for (int c = 1; c <= map.size() - 2; c++) {
                                        SystemUtils.PROCCED_INNER2_CHAT(map.get(c), ServerName);
                                    }
                                } else if(DataType.equalsIgnoreCase("FrontEnd-Interconnect") || DataType.equalsIgnoreCase("BackEnd-Responce")) {
                                    for (int c = 1; c <= map.size() - 2; c++) {
                                        JedisManager.updatehashmap(map.get(c), ServerName);
                                    }
                                } else {
                                    for (int c = 1; c <= map.size() - 2; c++) {
                                        BungeeStorage.READ_Bungee_command(map.get(c));
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
        } catch (Exception ex) {
            System.out.println("ex -< error Report");
            ex.printStackTrace();
        }
        try {
            clientSocket.close();
        } catch (Exception ex) {
            System.out.println("ex -< error Report");
            ex.printStackTrace();
        }
    }
}
