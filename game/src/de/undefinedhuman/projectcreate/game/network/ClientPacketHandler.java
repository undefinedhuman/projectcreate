package de.undefinedhuman.projectcreate.game.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.system.MovementSystem;
import de.undefinedhuman.projectcreate.core.network.PacketHandler;
import de.undefinedhuman.projectcreate.core.network.packets.LoginPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.CreateEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.RemoveEntityPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.ComponentPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.MovementPacket;
import de.undefinedhuman.projectcreate.core.network.packets.entity.components.PositionPacket;
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
        EntityManager.getInstance().addEntity(packet.worldID, player);
        GameManager.getInstance().player = player;
        Main.instance.setScreen(GameScreen.getInstance());
    }

    @Override
    public void handle(Connection connection, CreateEntityPacket packet) {
        Entity entity = CreateEntityPacket.parse(packet);
        if(entity == null) return;
        EntityManager.getInstance().addEntity(packet.worldID, entity);
    }

    @Override
    public void handle(Connection connection, RemoveEntityPacket packet) {
        EntityManager.getInstance().removeEntity(packet.worldID);
    }

    @Override
    public void handle(Connection connection, ComponentPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        ComponentPacket.parse(entity, packet);
    }

    @Override
    public void handle(Connection connection, MovementPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;
        MovementPacket.parse(entity, packet);
    }

    private long latestPositionPacketTime = 0;

    @Override
    public void handle(Connection connection, PositionPacket packet) {
        Entity entity = EntityManager.getInstance().getEntity(packet.worldID);
        if(entity == null) return;

        if(!(packet.timeStamp > latestPositionPacketTime))
            return;
        latestPositionPacketTime = packet.timeStamp;

        MovementComponent movementComponent = Mappers.MOVEMENT.get(entity);

        float dt = Math.max(0.001f, movementComponent.historyLength - (ClientManager.getInstance().getReturnTime() / 1000f) - (Gdx.graphics.getFramesPerSecond()/1000f - 20f/1000f));
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
                break;
            }

        }

        if (movementComponent.movementHistory.size() > 0 && new Vector2(packet.velX, packet.velY).sub(movementComponent.movementHistory.get(0).velocity).len() > 5f) {
            movementComponent.predictedPosition = new Vector2(packet.x, packet.y);
            for(MovementComponent.MovementFrame frame : movementComponent.movementHistory) {
                Vector2 newPosition = MovementSystem.moveEntity(movementComponent.predictedPosition, frame.direction, frame.velocity.x, frame.delta);
                frame.position.set(newPosition).sub(movementComponent.predictedPosition);
                frame.velocity.x = frame.direction * frame.velocity.x * frame.delta;
                movementComponent.predictedPosition = newPosition;
            }
        } else {
            movementComponent.predictedPosition = new Vector2(packet.x, packet.y);
            for(MovementComponent.MovementFrame frame : movementComponent.movementHistory)
                movementComponent.predictedPosition.add(frame.position);
        }

    }

}
