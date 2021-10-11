package de.undefinedhuman.projectcreate.game.item.drop;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.base.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.collision.CollisionManager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;
import de.undefinedhuman.projectcreate.game.world.World;

public class DropItem {

    public boolean isDead = false;
    private int id, amount;
    private Vector2 position, velocity = new Vector2();
    private float lifeLength = 300, time = 0, speed = 2 * 60, gravity = 200 * 6f;
    private Entity target;
    private Sprite sprite;

    private Vector2 difference = new Vector2(), offset = new Vector2();

    public DropItem(int id, int amount, Vector2 position) {

        this.id = id;
        this.amount = amount;
        this.position = position;

        this.target = null;
        this.sprite = new Sprite(TextureManager.getInstance().getTexture(ItemManager.getInstance().getItem(id).iconTexture.getValue()));

    }

    public void update(float delta) {

        this.time += delta;
        if (time >= lifeLength) isDead = true;

        Entity newTarget = getNewTarget();

        if (this.target == null) {

            if (newTarget != null) target = newTarget;
            else {
                velocity.set(0, Tools.clamp(collide(position, 0, 8) ? 120 : !collide(position, 0, 7) ? velocity.y - gravity * delta : 0, -speed * 2, speed * 2));
                velocity.scl(1 - delta);
                position.mulAdd(velocity, delta);
                position.y = Math.max(position.y, 0);
            }

        } else {

            TransformComponent transformComponent = Mappers.TRANSFORM.get(target);

            float dist = Math.abs(getCenter().dst(transformComponent.getCenterPosition()));

            if (dist < 100) {

                if (dist < 25) {

                    int i;
                    if ((i = InventoryManager.getInstance().addItem(id, amount)) == 0) amount = i;
                    if (amount == 0) isDead = true;

                } else {

                    difference.set(new Vector2(getCenter()).sub(transformComponent.getCenterPosition()));
                    offset.set(difference.x >= 0 ? 60 : -60, difference.y >= 0 ? 60 : -60);
                    velocity.set(velocity.x - (1 / (difference.x + offset.x)) * 300 * 300 * delta, velocity.y - (1 / (difference.y + offset.y)) * 200 * 300 * delta);
                    velocity.scl(1 - delta);
                    position.mulAdd(velocity, delta);

                }

            } else target = null;

        }

        checkWorld();

    }

    // TODO Refactor also make target choosing server sided

    private Entity getNewTarget() {
        /*for (Entity player : EntityManager.getInstance().getEntityByType(EntityType.Player))
            if (Math.abs(player.getCenterPosition().dst(getCenter())) < 80) return player;*/
        return null;
    }

    public boolean collide(Vector2 position, float xDistance, float yDistance) {
        return CollisionManager.collide(World.instance.getBlock(Tools.floor((position.x + sprite.getWidth() / 2 + xDistance) / Variables.BLOCK_SIZE), Tools.floor((position.y + yDistance) / Variables.BLOCK_SIZE), World.MAIN_LAYER));
    }

    public Vector2 getCenter() {
        return new Vector2(position.x + 8, position.y + 8);
    }

    private void checkWorld() {
        if (position.x < 0.0F) position.x = World.instance.size.x * Variables.BLOCK_SIZE + position.x;
        if (position.x >= World.instance.size.x * Variables.BLOCK_SIZE) position.x = 0.0F + (position.x - World.instance.size.x * Variables.BLOCK_SIZE);
    }

    public void render(SpriteBatch batch) {
        this.sprite.setPosition(position.x, position.y);
        this.sprite.draw(batch);
    }

}
