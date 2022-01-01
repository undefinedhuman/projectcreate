package de.undefinedhuman.projectcreate.core.network.packets.input;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.ListPacketHandler;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.utils.PacketConsumer;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class InputPacketHandler extends ListPacketHandler<InputPacket> {

    @Override
    public boolean handle(Connection connection, Packet packet) {
        if(!(packet instanceof InputPacket)) return false;
        InputPacket inputPacket = (InputPacket) packet;
        PacketConsumer<InputPacket> handlerFunction = packetHandlerFunctions.get(inputPacket.getCode());
        if(handlerFunction != null) handlerFunction.accept(connection, inputPacket);
        else Log.error("No handler function for input stage " + inputPacket.getCode() + " is defined!");
        return true;
    }

}
