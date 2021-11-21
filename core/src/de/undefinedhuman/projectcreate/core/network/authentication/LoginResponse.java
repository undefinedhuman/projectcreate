package de.undefinedhuman.projectcreate.core.network.authentication;

import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;

public class LoginResponse implements Packet {

    public long worldID;
    public String componentData;

    private LoginResponse() {}

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static LoginResponse serialize(long worldID, String componentData) {
        LoginResponse packet = new LoginResponse();
        packet.worldID = worldID;
        packet.componentData = componentData;
        return packet;
    }

}
