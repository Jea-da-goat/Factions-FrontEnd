package com.itndev.factions.SocketConnection.Client;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.RedisStreams.BungeeAPI.BungeeStorage;
import com.itndev.factions.RedisStreams.StaticVal;
import com.itndev.factions.Utils.SystemUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.HashMap;

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

    private void run() {
        new Thread(() -> {
            while(true) {
                try {
                    clientSocket = new Socket(this.hostname, this.port);
                    //output = new PrintStream(clientSocket.getOutputStream());
                    clientSocket.setTcpNoDelay(true);
                    //output.println("Connection Enabled");

                    //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    output = new ObjectOutputStream(clientSocket.getOutputStream());
                    input = new ObjectInputStream(clientSocket.getInputStream());
                    //writer = new PrintWriter(output, true);
                    //reader = new Buffered(new InputStreamReader(input));

                    while(true) {
                        try {
                            HashMap<Integer, String> map;
                            try {
                                //map = (HashMap<Integer, String>) reader.read
                                map = (HashMap<Integer, String>) input.readObject();
                                if(!map.isEmpty()) {
                                    String ServerName;
                                    String DataType;
                                    ServerName = map.getOrDefault(StaticVal.getServerNameArgs(), "");
                                    DataType = map.getOrDefault(StaticVal.getDataTypeArgs(), "");
                                    if(DataType.equalsIgnoreCase("FrontEnd-Chat")) {
                                        for (int c = 1; c <= map.size() - 2; c++) {
                                            SystemUtils.PROCCED_INNER2_CHAT(map.get(c), ServerName);
                                        }
                                        //System.out.println(1);
                                    } else if(DataType.equalsIgnoreCase("FrontEnd-Interconnect") || DataType.equalsIgnoreCase("BackEnd-Responce")) {
                                        for (int c = 1; c <= map.size() - 2; c++) {
                                            JedisManager.updatehashmap(map.get(c), ServerName);
                                        }
                                        //System.out.println(2);
                                    } else {
                                        for (int c = 1; c <= map.size() - 2; c++) {
                                            BungeeStorage.READ_Bungee_command(map.get(c));
                                        }
                                        //System.out.println(3);
                                    }
                                    //System.out.println(DataType);
                                } else {
                                    this.closeAll();
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Thread.sleep(100);
                    }
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
            update(new HashMap<Integer, String>());
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
