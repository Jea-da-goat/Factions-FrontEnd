package com.itndev.factions.SocketConnection;

import com.itndev.factions.SocketConnection.Client.NettyClient;
import com.itndev.factions.SocketConnection.Client.Old.Client;
import com.itndev.factions.SocketConnection.IO.ResponceList;

public class Main {

    public static void launch() throws Exception {
        NettyClient client = new NettyClient(Socket.Address, Socket.Port);
        client.run();
        //Client client = new Client(Socket.Address, Socket.Port);
        //ResponceList.get().set(client);
        //ResponceList.get().run();
    }
}
