package de.undefinedhuman.sandboxgame.collision;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.engine.items.type.blocks.Block;
import de.undefinedhuman.sandboxgame.world.World;

public class CollisionManager {

    public static boolean blockCanBePlaced(Entity entity, Vector2 blockPosition, byte cell) {

        CollisionComponent collisionComponent;

        if ((collisionComponent = (CollisionComponent) entity.getComponent(ComponentType.COLLISION)) != null) {

            Vector2 pos = new Vector2().add(entity.transform.getPosition()).add(collisionComponent.getOffset());

            boolean collide = collide(pos, collisionComponent.getWidth(), collisionComponent.getHeight());

            if (!collide) {

                World.instance.mainLayer.setBlock((int) blockPosition.x, (int) blockPosition.y, cell);
                collide = collide(pos, collisionComponent.getWidth(), collisionComponent.getHeight());
                World.instance.mainLayer.setBlock((int) blockPosition.x, (int) blockPosition.y, (byte) 0);
                return !collide;

            }

        }

        return false;

    }

    // Position + offset
    public static boolean collide(Vector2 position, float width, float height) {

        boolean collide = false;

        if (position.y < 0 || position.y + width > World.instance.height * Variables.BLOCK_SIZE) collide = true;

        for (int yPos = (int) (position.y / Variables.BLOCK_SIZE); yPos < Math.ceil(((int) position.y + height) / Variables.BLOCK_SIZE); yPos++)
            for (int xPos = (int) (position.x / Variables.BLOCK_SIZE); xPos < Math.ceil(((int) position.x + width) / Variables.BLOCK_SIZE); xPos++)
                if (!collide) collide = collide(World.instance.mainLayer.getBlock(xPos, yPos));

        return collide;

    }

    public static boolean collide(byte id) {
        return ((Block) ItemManager.instance.getItem(id)).collide.getBoolean();
    }

    public static boolean collideHike(Vector2 position, float width, float height) {

        boolean collide = false;

        for (int xPos = (int) (position.x / Variables.BLOCK_SIZE); xPos < Math.ceil(((int) position.x + width) / Variables.BLOCK_SIZE); xPos++)
            if (collide(World.instance.mainLayer.getBlock(xPos, (int) (position.y / Variables.BLOCK_SIZE)))) {
                collide = true;
                break;
            }

        loop:
        for (int yPos = (int) (position.y / Variables.BLOCK_SIZE + 1); yPos < Math.ceil(((int) position.y + height) / Variables.BLOCK_SIZE); yPos++)
            for (int xPos = (int) (position.x / Variables.BLOCK_SIZE); xPos < Math.ceil(((int) position.x + width) / Variables.BLOCK_SIZE); xPos++)
                if (collide(World.instance.mainLayer.getBlock(xPos, yPos))) {
                    collide = false;
                    break loop;
                }

        return collide;

    }

    public static boolean collideWithEntityAABB(Vector2 position, float width, float height, Vector2 position2, float width2, float height2) {
        return position.x < position2.x + width2 && position.x + width > position2.x && position.y < position2.y + height2 && position.y + height > position2.y;
    }

}
