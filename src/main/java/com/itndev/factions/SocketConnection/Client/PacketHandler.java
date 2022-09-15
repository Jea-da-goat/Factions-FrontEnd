package com.itndev.factions.SocketConnection.Client;

import com.itndev.factions.SocketConnection.IO.PacketProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;

public class PacketHandler extends SimpleChannelInboundHandler<Object> {



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        HashMap<Integer, Object> stream = (HashMap<Integer, Object>) o;
        if(stream.isEmpty()) {
            channelHandlerContext.channel().close();
        }
        new Thread(() -> PacketProcessor.run(stream)).start();
    }
}
