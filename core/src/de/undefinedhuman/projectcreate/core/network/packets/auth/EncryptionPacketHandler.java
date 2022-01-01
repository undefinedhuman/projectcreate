package de.undefinedhuman.projectcreate.core.network.packets.auth;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.ListPacketHandler;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.utils.PacketConsumer;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class EncryptionPacketHandler extends ListPacketHandler<EncryptionPacket> {

    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof EncryptionPacket)) return false;
        EncryptionPacket encryptionPacket = (EncryptionPacket) packet;
        PacketConsumer<EncryptionPacket> handlerFunction = packetHandlerFunctions.get(encryptionPacket.getCode());
        if(handlerFunction != null) handlerFunction.accept(connection, encryptionPacket);
        else Log.error("No handler function for encryption stage " + encryptionPacket.getCode() + " is defined!");
        return true;
    }

}
