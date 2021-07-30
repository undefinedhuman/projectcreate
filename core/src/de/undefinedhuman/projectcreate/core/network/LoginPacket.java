package de.undefinedhuman.projectcreate.core.network;

public class LoginPacket implements Packet {

    public String name;
    public boolean loggedIn;

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }

}
