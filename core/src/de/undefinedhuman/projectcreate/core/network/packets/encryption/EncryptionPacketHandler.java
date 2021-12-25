package de.undefinedhuman.projectcreate.core.network.packets.encryption;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.utils.PacketConsumer;
import de.undefinedhuman.projectcreate.engine.log.Log;

import java.util.HashMap;

public class EncryptionPacketHandler implements PacketHandler {

    private HashMap<Integer, PacketConsumer<EncryptionPacket>> encryptionStageHandlerFunctions = new HashMap();

    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof EncryptionPacket)) return false;
        EncryptionPacket encryptionPacket = (EncryptionPacket) packet;
        PacketConsumer<EncryptionPacket> handlerFunction = encryptionStageHandlerFunctions.get(encryptionPacket.code);
        if(handlerFunction != null) handlerFunction.accept(connection, encryptionPacket);
        else Log.error("No handler function for encryption stage " + encryptionPacket.code + " is defined!");
        return true;
    }

    protected void registerHandlerFunction(Integer code, PacketConsumer<EncryptionPacket> handlerFunction) {
        this.encryptionStageHandlerFunctions.put(code, handlerFunction);
    }

}
