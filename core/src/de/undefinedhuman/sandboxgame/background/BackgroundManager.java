package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.world.World;

public class BackgroundManager extends Manager {

    public static BackgroundManager instance;

    private Layer[] layers;
    public float scale = 0, worldWidth;
    private float foreGroundWidth = 688;

    static String[] cloudTextures = new String[] { "background/clouds/cloud1.png", "background/clouds/cloud2.png", "background/clouds/cloud3.png", "background/clouds/cloud4.png", "background/clouds/cloud5.png", "background/clouds/cloud6.png", "background/clouds/cloud7.png", "background/clouds/cloud8.png" };

    public BackgroundManager() {
        if (instance == null) instance = this;
        worldWidth = World.instance.mainLayer.width * Variables.BLOCK_SIZE;
        layers = new Layer[] {
                new BackgroundLayer(new Vector2(640, 313)),
                new CloudLayer(150, 0.25f),
                new ForegroundLayer("background/foreground/mountain.png", new Vector2(688, 162), 0.25f, 65f),
                new CloudLayer(100, 0.5f),
                new ForegroundLayer("background/foreground/pine1.png", new Vector2(688, 199), 0.5f, -35f),
                new CloudLayer(50, 0.75f),
                new ForegroundLayer("background/foreground/pine2.png", new Vector2(688, 148), 0.75f, -55f)
        };
    }

    @Override
    public void init() {
        TextureManager.instance.addTexture(cloudTextures);
        Time.load();
        for(Layer layer : layers) layer.init();
    }

    @Override
    public void resize(int width, int height) {
        this.scale = Math.max((int) ((float) height / foreGroundWidth), 1);
        for(Layer layer : layers) layer.resize(width, height);
    }

    @Override
    public void update(float delta) {
        layers[0].update(delta, Variables.HOUR_LENGTH);
        float speed = 250;
        if(GameManager.instance.player != null) {
            MovementComponent movement = (MovementComponent) GameManager.instance.player.getComponent(ComponentType.MOVEMENT);
            speed = (movement.moveLeft ? 1f : movement.moveRight ? -1f : 0) * movement.getSpeed();
        }
        for(int i = 1; i < layers.length; i++) layers[i].update(delta, speed);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        // batch.draw(TextureManager.instance.getTexture("background/foreground/Sun.png"), camera.position.x + camera.viewportWidth * 0.5f, );
        for(Layer layer : layers) layer.render(batch, camera);
    }

    @Override
    public void delete() {
        for(Layer layer : layers) layer.delete();
        TextureManager.instance.removeTexture(cloudTextures);
        Time.delete();
    }

}
