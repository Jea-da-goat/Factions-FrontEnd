package com.itndev.factions.SocketConnection;

import com.itndev.factions.SocketConnection.Client.Client;
import com.itndev.factions.SocketConnection.IO.ResponceList;

public class Main {

    public static void launch() {
        Client client = new Client(Socket.Address, Socket.Port);
        ResponceList.get().set(client);
        ResponceList.get().run();
    }
}
