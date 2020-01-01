package de.undefinedhuman.sandboxgame.projectiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class Projectile {

    private Entity target;
    private int ownerWorldID;
    private float rotation, gravity = 750;
    private Vector2 position, velocity;

    private TextureRegion texture;

    public Projectile(Entity owner, Vector2 position, float velocityX, float velocityY) {

        velocity = new Vector2(velocityX, velocityY);
        rotation = velocity.angle() - 45;
        texture = TextureManager.instance.getTexture("Arrow.png");

        this.ownerWorldID = owner.getWorldID();
        this.position = new Vector2(position);
        this.position.sub((int) (texture.getRegionWidth()/2f),(int) (texture.getRegionHeight()/2f));

    }

    public void update(float delta) {

        int tests = (int) Math.max(1, delta * 400 * velocity.len() / 800f);

        this.velocity.y -= gravity * delta / tests;
        this.position.mulAdd(velocity,delta / tests);

        velocity.clamp(0, 5000);

        if(Math.abs(velocity.x) > 0.01f || Math.abs(velocity.y) > 0.01f) rotation = Tools.swordLerp(rotation,velocity.angle() - 45,10);

    }

    public void render(SpriteBatch batch) {
        batch.draw(new TextureRegion(texture), position.x, position.y, (int) (texture.getRegionWidth()/2f), (int) (texture.getRegionHeight()/2f), texture.getRegionWidth(), texture.getRegionHeight(),1,1, rotation);
    }

}
