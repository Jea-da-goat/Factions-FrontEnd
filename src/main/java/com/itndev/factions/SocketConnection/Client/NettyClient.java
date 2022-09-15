package com.itndev.factions.SocketConnection.Client;


import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.RedisStreams.StaticVal;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NettyClient {
    public static String HOST;// = System.getProperty("host", "127.0.0.1");
    public static int PORT;// = Integer.parseInt(System.getProperty("port", "8992"));

    private static Channel channel;

    public static Channel getConnection() {
        return channel;
    }

    public static void send(HashMap<Integer, Object> stream) {
        channel.writeAndFlush(stream);
    }

    public NettyClient(String host, int port) {
        HOST = host;
        PORT = port;
    }

    public void run() throws Exception {

        final SslContext sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new StreamInitializer(sslCtx));

            // Start the connection attempt.
            channel = b.connect(HOST, PORT).sync().channel();

            // Read commands from the stdin
            //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
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
                                NettyClient.channel.writeAndFlush(stream);
                                //System.out.println(ResponceList.class.getCanonicalName());
                            }
                        }
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
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
                                NettyClient.channel.writeAndFlush(stream);
                                //System.out.println(ResponceList.class.getCanonicalName());
                            }
                        }
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
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
                                NettyClient.channel.writeAndFlush(stream);
                                //System.out.println(ResponceList.class.getCanonicalName());
                            }
                        }
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            while (!Main.ShutDown) {
                Thread.onSpinWait();
            }
            /*for (;;) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                // Sends the received line to the server.
                lastWriteFuture = channel.writeAndFlush(line + "\r\n");

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if ("bye".equals(line.toLowerCase())) {
                    channel.closeFuture().sync();
                    break;
                }
            }*/

            // Wait until all messages are flushed before closing the channel.
        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }
}
