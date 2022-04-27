package de.undefinedhuman.projectcreate.core.network.packets.input;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.ListPacketHandler;
import de.undefinedhuman.projectcreate.core.network.utils.PacketConsumer;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class InputPacketHandler extends ListPacketHandler<InputPacket> {

    @Override
    public void handle(Connection connection, InputPacket packet) {
        PacketConsumer<InputPacket> handlerFunction = packetHandlerFunctions.get(packet.getCode());
        if(handlerFunction != null) handlerFunction.accept(connection, packet);
        else Log.error("No handler function for input stage " + packet.getCode() + " is defined!");
    }

}
