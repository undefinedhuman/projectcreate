package de.undefinedhuman.projectcreate.game.screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.projectcreate.engine.collision.Hitbox;
import de.undefinedhuman.projectcreate.engine.collision.SAT;
import de.undefinedhuman.projectcreate.core.ecs.collision.CollisionComponent;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.world.World;

public class CollisionUtils {

    public static Hitbox[] blockCollisionMask = new Hitbox[] {
            null,
            new Hitbox(new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE), new Vector2[] { new Vector2(0, 0), new Vector2(Variables.COLLISION_SIZE, 0), new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE), new Vector2(0, Variables.COLLISION_SIZE) }),
            new Hitbox(new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE), new Vector2[] { new Vector2(0, 0), new Vector2(0, Variables.COLLISION_SIZE), new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE) }),
            new Hitbox(new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE), new Vector2[] { new Vector2(0, Variables.COLLISION_SIZE), new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE), new Vector2(Variables.COLLISION_SIZE, 0) }),
            new Hitbox(new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE), new Vector2[] { new Vector2(0, 0), new Vector2(Variables.COLLISION_SIZE, 0), new Vector2(0, Variables.COLLISION_SIZE) }),
            new Hitbox(new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE), new Vector2[] { new Vector2(0, 0), new Vector2(Variables.COLLISION_SIZE, 0), new Vector2(Variables.COLLISION_SIZE, Variables.COLLISION_SIZE) })
    };

    private static Vector2 tempCenterVector = new Vector2();

    public static Vector3 calculateCollisionX(CollisionComponent collisionComponent) {
        Vector3 response = new Vector3();

        for(int j = collisionComponent.getCollisionBounds().y; j < collisionComponent.getCollisionBounds().w; j++)
            for(int i = collisionComponent.getCollisionBounds().x; i < collisionComponent.getCollisionBounds().z; i++) {
                Vector3 mtv = new Vector3();
                if (!calculateMTV(collisionComponent.getHitbox(), i, j, mtv)) continue;
                if (mtv.z >= response.z) response.set((mtv.x != 0 && mtv.y != 0) ? 0 : mtv.x, 0, mtv.z);
            }

        return response;
    }

    public static Vector3 calculateCollisionY(CollisionComponent collisionComponent) {
        Vector3 response = new Vector3();

        for(int j = collisionComponent.getCollisionBounds().y; j < collisionComponent.getCollisionBounds().w; j++)
            for(int i = collisionComponent.getCollisionBounds().x; i < collisionComponent.getCollisionBounds().z; i++) {
                Vector3 mtv = new Vector3();
                if (!calculateMTV(collisionComponent.getHitbox(), i, j, mtv)) continue;
                if (mtv.z < response.z) continue;
                collisionComponent.onSlope = mtv.x != 0 && mtv.y != 0;
                response.set(mtv.x, collisionComponent.onSlope ? ((mtv.x * mtv.x) / mtv.y) + mtv.y : mtv.y, mtv.z);
            }

        return response;
    }

    public static Vector3 calculateSlopeCollisionX(CollisionComponent collisionComponent) {
        Vector3 response = new Vector3();

        for(int j = collisionComponent.getCollisionBounds().y; j < collisionComponent.getCollisionBounds().w; j++)
            for(int i = collisionComponent.getCollisionBounds().x; i < collisionComponent.getCollisionBounds().z; i++) {
                Vector3 mtv = new Vector3();
                if (!calculateMTV(collisionComponent.getHitbox(), i, j, mtv)) continue;
                if (mtv.z > response.z && mtv.x != 0) {
                    response.set((mtv.y * mtv.y) / mtv.x + mtv.x, 0, mtv.z);
                    collisionComponent.onSlope = false;
                }
            }

        return response;
    }

    public static Vector3 calculateSlopeCollisionY(CollisionComponent collisionComponent) {
        Vector3 response = new Vector3();

        for(int j = collisionComponent.getCollisionBounds().y; j < collisionComponent.getCollisionBounds().w; j++)
            for(int i = collisionComponent.getCollisionBounds().x; i < collisionComponent.getCollisionBounds().z; i++) {
                Vector3 mtv = new Vector3();
                if (!calculateMTV(collisionComponent.getHitbox(), i, j, mtv)) continue;
                if (collisionComponent.onSlope && mtv.z > response.z) response.set(0, mtv.y, mtv.z);
            }

        return response;
    }

    public static boolean calculateMTV(Hitbox entityHitbox, int x, int y, Vector3 mtv) {
        short state = World.instance.getCollision(x, y, World.COLLISION_STATE_LAYER);
        if(state == 0) return false;
        Hitbox hitbox = blockCollisionMask[state];
        hitbox.update(x * Variables.COLLISION_SIZE, y * Variables.COLLISION_SIZE);
        Vector3 overlap = SAT.collide(entityHitbox, hitbox);
        if (overlap == null) return false;
        tempCenterVector.set(entityHitbox.getCenter()).sub(hitbox.getCenter()).nor();
        if (Vector2.dot(tempCenterVector.x, tempCenterVector.y, overlap.x, overlap.y) < 0)
            overlap.scl(-1f, -1f, 1f);
        mtv.set(overlap.x * overlap.z, overlap.y * overlap.z, overlap.z);
        return true;
    }

}
