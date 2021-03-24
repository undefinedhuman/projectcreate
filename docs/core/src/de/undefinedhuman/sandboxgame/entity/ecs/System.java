package de.undefinedhuman.sandboxgame.entity.ecs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.entity.Entity;

public abstract class System {

    public System() {}

    public void init(Entity entity) {}
    public abstract void update(float delta, Entity entity);
    public void render(SpriteBatch batch, Entity entity) {}

}
