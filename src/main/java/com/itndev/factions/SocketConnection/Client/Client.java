package com.itndev.factions.SocketConnection.Client;

import com.itndev.FaxLib.Utils.Data.DataStream;
import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.RedisStreams.BungeeAPI.BungeeStorage;
import com.itndev.factions.Utils.SystemUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public synchronized void update(DataStream stream) throws IOException {
        if(output == null) {
            System.out.println("OutputStream is Null");
            return;
        }
        output.writeObject(stream);
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
                            DataStream stream;
                            HashMap<Integer, String> map;
                            try {
                                //map = (HashMap<Integer, String>) reader.read
                                stream = (DataStream) input.readObject();
                                if(!stream.isEmpty()) {
                                    String ServerName = stream.getServerName();
                                    String DataType = stream.getDataType();
                                    switch (DataType) {
                                        case "FrontEnd-Chat":
                                            stream.getStream().forEach(data -> SystemUtils.PROCCED_INNER2_CHAT(data, ServerName));
                                        case "FrontEnd-Interconnect":
                                            stream.getStream().forEach(data -> JedisManager.updatehashmap(data, ServerName));
                                        case "BackEnd-Responce":
                                            stream.getStream().forEach(data -> JedisManager.updatehashmap(data, ServerName));
                                        case "BungeeCord-Forward":
                                            stream.getStream().forEach(BungeeStorage::READ_Bungee_command);
                                    }
                                    /*if(DataType.equalsIgnoreCase("FrontEnd-Chat")) {
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
                                    }*/
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
            update(new DataStream());
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
