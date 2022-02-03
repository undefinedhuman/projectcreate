package de.undefinedhuman.projectcreate.core.network.packets.auth;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.ListPacketHandler;
import de.undefinedhuman.projectcreate.core.network.utils.PacketConsumer;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class EncryptionPacketHandler extends ListPacketHandler<EncryptionPacket> {
    @Override
    public void handle(Connection connection, EncryptionPacket packet) {
        PacketConsumer<EncryptionPacket> handlerFunction = packetHandlerFunctions.get(packet.getCode());
        if(handlerFunction != null) handlerFunction.accept(connection, packet);
        else Log.error("No handler function for encryption stage " + packet.getCode() + " is defined!");
    }
}
