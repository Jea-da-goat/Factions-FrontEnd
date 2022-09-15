package com.itndev.factions.SocketConnection.Client.Old;

import com.itndev.FaxLib.Utils.Data.DataStream;
import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.RedisStreams.BungeeAPI.BungeeStorage;
import com.itndev.factions.RedisStreams.StaticVal;
import com.itndev.factions.SocketConnection.IO.ResponceList;
import com.itndev.factions.Utils.SystemUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

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

    public synchronized void update(HashMap<Integer, Object> stream) throws IOException {
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
                            try {
                                //map = (HashMap<Integer, String>) reader.read
                                HashMap<Integer, Object> stream = (HashMap<Integer, Object>) input.readObject();
                                //DataStream stream = (DataStream) input.readObject();
                                if(!stream.isEmpty()) {
                                    //System.out.println(this.getClass().getCanonicalName());
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
                                        ((List<String>)stream.get(1)).forEach(data -> SystemUtils.PROCCED_INNER2_CHAT(data, ServerName));
                                        //System.out.println(1);
                                    } else if(DataType.equalsIgnoreCase("FrontEnd-Interconnect") || DataType.equalsIgnoreCase("BackEnd-Responce")) {
                                        ((List<String>)stream.get(1)).forEach(data -> JedisManager.updatehashmap(data, ServerName));
                                        //System.out.println(2);
                                    } else {
                                        ((List<String>)stream.get(1)).forEach(BungeeStorage::READ_Bungee_command);
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
            update(new HashMap<Integer, Object>());
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
