package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.util.HashMap;

public class PacketListener extends Listener {

    private HashMap<Class<? extends Packet>, PacketHandler> handlers;

    protected PacketListener() {
        handlers = new HashMap<>();
    }

    @Override
    public void received(Connection connection, Object object) {
        if(!(object instanceof Packet))
            return;
        Packet packet = (Packet) object;
        PacketHandler handler = getHandlers().get(packet.getClass());
        if(handler == null || !handler.handle(connection, packet))
            Log.error("Packet handler for packet type " + packet.getClass().getSimpleName() + " not defined!");
    }

    protected void registerPacketHandlers(Class<? extends Packet> packetClass, PacketHandler packetHandler) {
        handlers.put(packetClass, packetHandler);
    }

    protected HashMap<Class<? extends Packet>, PacketHandler> getHandlers() {
        return handlers;
    }

}
