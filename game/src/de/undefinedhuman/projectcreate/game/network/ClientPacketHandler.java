package de.undefinedhuman.projectcreate.game.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.JumpPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.movement.MovementPacket;
import de.undefinedhuman.projectcreate.core.network.utils.PacketUtils;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameScreen;

public class ClientPacketHandler implements PacketHandler {
    @Override
    public void handle(Connection connection, LoginPacket packet) {
        Entity player = BlueprintManager.getInstance().createEntity(BlueprintManager.PLAYER_BLUEPRINT_ID, packet.worldID);
        PacketUtils.setComponentData(player, PacketUtils.parseComponentData(packet.componentData));
        Mappers.MOVEMENT.get(player).predictedPosition.set(Mappers.TRANSFORM.get(player).getPosition());
        EntityManager.getInstance().addEntity(packet.worldID, player);
        GameManager.getInstance().player = player;
        Main.instance.setScreen(GameScreen.getInstance());
    }

    @Override
    public void handle(Connection connection, CreateEntityPacket packet) {
        Entity entity = CreateEntityPacket.parse(packet);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        if(Mappers.MOVEMENT.get(entity) != null)
            Mappers.MOVEMENT.get(entity).predictedPosition.set(Mappers.TRANSFORM.get(entity).getPosition());
        EntityManager.getInstance().addEntity(packet.worldID, entity);
    }

    @Override
    public void handle(Connection connection, RemoveEntityPacket packet) {
        EntityManager.getInstance().removeEntity(packet.worldID);
    }

    @Override
    public void handle(Connection connection, ComponentPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        ComponentPacket.parse(entity, packet);
    }

    @Override
    public void handle(Connection connection, MovementPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        MovementPacket.parse(entity, packet);
    }

    private Vector2 TEMP_POSITION = new Vector2();

    @Override
    public void handle(Connection connection, PositionPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null || entity.isScheduledForRemoval() || entity.isRemoving()) return;
        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);

        if(!(packet.timeStamp > movementComponent.latestPositionPacketTime))
            return;
        movementComponent.latestPositionPacketTime = packet.timeStamp;

        if(movementComponent.lastPositionPacketTimeLocal == 0)
            movementComponent.lastPositionPacketTimeLocal = System.nanoTime();

        if(entity != GameManager.getInstance().player) {
            // CHANGE FROM PREDICTED POSITION TO PREVIOUS POSITION SO TRANSFORM COMPONENT IS ALWAYS AT SERVER POSITION BUT RENDERSYSTEM IS AT INTERPOLATED POSITION
            MovementComponent.MovementFrame frame = new MovementComponent.MovementFrame();
            frame.position = new Vector2(packet.x, packet.y);
            frame.velocity = new Vector2(packet.velX, packet.velY);
            frame.delta = (System.nanoTime() - movementComponent.lastPositionPacketTimeLocal) * 0.000000001f;
            movementComponent.movementHistory.add(frame);
        } else {
            float dt = Math.max(0.001f, (System.nanoTime() - movementComponent.lastPositionPacketTimeLocal) * 0.000000001f);
            movementComponent.historyLength -= dt;

            while(movementComponent.movementHistory.size() > 0 && dt > 0) {
                MovementComponent.MovementFrame frame = movementComponent.movementHistory.get(0);

                if(dt >= frame.delta) {
                    dt -= frame.delta;
                    movementComponent.movementHistory.remove(0);
                } else {
                    float newDelta = 1 - dt / frame.delta;
                    frame.delta -= dt;
                    frame.position.scl(newDelta);
                    frame.velocity.scl(newDelta);
                    break;
                }

            }

            movementComponent.predictedPosition = new Vector2(packet.x, packet.y);
            for(MovementComponent.MovementFrame frame : movementComponent.movementHistory) {
                TEMP_POSITION.set(movementComponent.predictedPosition).mulAdd(frame.velocity, frame.delta);
                frame.position.set(TEMP_POSITION).sub(movementComponent.predictedPosition);
                movementComponent.predictedPosition.set(TEMP_POSITION);
            }

            if(movementComponent.predictedPosition.y <= 0)
                movementComponent.predictedPosition.y = 0;
        }
        movementComponent.lastPositionPacketTimeLocal = System.nanoTime();
    }

    @Override
    public void handle(Connection connection, JumpPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        MovementComponent component = Mappers.MOVEMENT.get(entity);
        if(component == null) return;
        component.forceJump();
    }

}
