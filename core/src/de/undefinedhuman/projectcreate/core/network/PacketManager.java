package de.undefinedhuman.projectcreate.core.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import de.undefinedhuman.projectcreate.core.network.inventory.EquipPacket;
import de.undefinedhuman.projectcreate.core.network.entity.AddEntityPacket;
import de.undefinedhuman.projectcreate.core.network.entity.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.player.AddPlayerPacket;
import de.undefinedhuman.projectcreate.core.network.player.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.world.BlockPacket;
import de.undefinedhuman.projectcreate.core.network.world.WorldLayerPacket;
import de.undefinedhuman.projectcreate.core.network.world.WorldPacket;

public class PacketManager {

    public static void register(EndPoint endpoint) {

        Kryo kryo = endpoint.getKryo();
        kryo.register(AddEntityPacket.class);
        kryo.register(RemoveEntityPacket.class);
        kryo.register(LoginPacket.class);
        kryo.register(WorldLayerPacket.class);
        kryo.register(WorldPacket.class);
        kryo.register(AddPlayerPacket.class);
        kryo.register(ServerClosedPacket.class);
        kryo.register(JumpPacket.class);
        kryo.register(ComponentPacket.class);
        kryo.register(BlockPacket.class);
        kryo.register(EquipPacket.class);

    }

}
