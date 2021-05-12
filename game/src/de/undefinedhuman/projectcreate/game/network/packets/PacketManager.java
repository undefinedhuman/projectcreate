package de.undefinedhuman.projectcreate.game.network.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import de.undefinedhuman.projectcreate.game.network.packets.entity.AddEntityPacket;
import de.undefinedhuman.projectcreate.game.network.packets.entity.ComponentPacket;
import de.undefinedhuman.projectcreate.game.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.game.network.packets.inventory.EquipPacket;
import de.undefinedhuman.projectcreate.game.network.packets.player.AddPlayerPacket;
import de.undefinedhuman.projectcreate.game.network.packets.player.JumpPacket;
import de.undefinedhuman.projectcreate.game.network.packets.world.BlockPacket;
import de.undefinedhuman.projectcreate.game.network.packets.world.WorldLayerPacket;
import de.undefinedhuman.projectcreate.game.network.packets.world.WorldPacket;

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
