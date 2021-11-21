package de.undefinedhuman.projectcreate.core.network.packets.entity.movement;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.player.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.Packet;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.engine.ecs.component.IDComponent;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.log.Log;

public class MovementResponse implements Packet {

    public long worldID;
    public int direction;

    private MovementResponse() {}

    @Override
    public void handle(Connection connection, PacketHandler handler) {
        handler.handle(connection, this);
    }

    public static MovementResponse serialize(Entity entity) {
        IDComponent idComponent = Mappers.ID.get(entity);
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
        if(idComponent == null || movementComponent == null)
            return Log.debug("Error while retrieving id or movement component from entity!", () -> null);
        MovementResponse response = new MovementResponse();
        response.worldID = idComponent.getWorldID();
        response.direction = movementComponent.getDirection();
        return response;
    }

    public static void parse(MovementResponse response) {
        Entity entity = EntityManager.getInstance().getEntity(response.worldID);
        if(entity == null) {
            Log.debug("Error while retrieving entity with world ID: " + response.worldID);
            return;
        }
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);
        if(movementComponent == null) {
            Log.debug("Error while retrieving movement component from entity with world ID: " + response.worldID);
            return;
        }
        movementComponent.move(response.direction);
    }

}
