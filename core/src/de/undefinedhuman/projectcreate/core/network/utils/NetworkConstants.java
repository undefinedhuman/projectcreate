package de.undefinedhuman.projectcreate.core.network.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.authentication.LoginResponse;
import de.undefinedhuman.projectcreate.core.network.encryption.*;
import de.undefinedhuman.projectcreate.core.network.authentication.LoginRequest;
import de.undefinedhuman.projectcreate.core.network.packets.MousePacket;
import de.undefinedhuman.projectcreate.core.network.packets.SelectorPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementRequest;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.SelectItemPacket;
import de.undefinedhuman.projectcreate.core.network.packets.inventory.UpdateSlotsPacket;

public class NetworkConstants {

    public static final int WRITE_BUFFER_SIZE = 65536;
    public static final int OBJECT_BUFFER_SIZE = 8192 * 4;

    public static final int DEFAULT_TCP_PORT = 21000;
    public static final int DEFAULT_UDP_PORT = 21001;

    public static final int NETWORK_TIME_OUT = 5000;

    public static void register(EndPoint endpoint) {
        registerPackets(endpoint.getKryo(),
                LoginRequest.class,
                LoginResponse.class,
                CreateEntityPacket.class,
                RemoveEntityPacket.class,
                ComponentPacket.class,
                MovementRequest.class,
                PositionPacket.class,
                JumpPacket.class,
                MousePacket.class,
                SelectorPacket.class,
                SelectItemPacket.class,
                UpdateSlotsPacket.class,
                InitPacket.class,
                EncryptionRequest.class,
                EncryptionResponse.class,
                SessionPacket.class,
                EncryptionPacket.class
        );
    }

    @SafeVarargs
    private static void registerPackets(Kryo kryo, Class<? extends Packet>... packets) {
        for(Class<? extends Packet> packet : packets)
            kryo.register(packet);
    }

}
