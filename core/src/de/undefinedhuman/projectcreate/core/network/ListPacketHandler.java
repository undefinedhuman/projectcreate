package de.undefinedhuman.projectcreate.core.network;

import de.undefinedhuman.projectcreate.core.network.utils.PacketConsumer;

import java.util.HashMap;

public abstract class ListPacketHandler<P extends Packet> implements PacketHandler {

    protected final HashMap<Integer, PacketConsumer<P>> packetHandlerFunctions = new HashMap<>();

    protected void registerHandlerFunction(Integer code, PacketConsumer<P> handlerFunction) {
        this.packetHandlerFunctions.put(code, handlerFunction);
    }

}
