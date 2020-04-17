package de.undefinedhuman.sandboxgame.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.background.birds.BirdLayer;
import de.undefinedhuman.sandboxgame.background.clouds.CloudLayer;
import de.undefinedhuman.sandboxgame.background.layers.BackgroundLayer;
import de.undefinedhuman.sandboxgame.background.layers.ForegroundLayer;
import de.undefinedhuman.sandboxgame.engine.entity.ComponentType;
import de.undefinedhuman.sandboxgame.engine.entity.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.engine.resources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.screen.gamescreen.GameManager;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;

public class BackgroundManager extends Manager {

    public static BackgroundManager instance;

    public float scale = 0, worldWidth, speed = 250f;

    private Layer[] layers;
    private float foreGroundWidth = 688;

    public String[] cloudTextures = new String[Variables.CLOUD_COUNT];
    public TextureRegion[][] birdTexture;

    public BackgroundManager() {
        if (instance == null) instance = this;
        worldWidth = World.instance.mainLayer.width * Variables.BLOCK_SIZE;
        layers = new Layer[] {
                new BackgroundLayer(new Vector2(640, 300)),
                new CloudLayer(200, 0.25f),
                new ForegroundLayer("background/foreground/Mountain 1.png", new Vector2(foreGroundWidth, 127), 0.25f, 135f),
                new CloudLayer(125, 0.5f),
                new ForegroundLayer("background/foreground/Mountain 2.png", new Vector2(foreGroundWidth, 162), 0.5f, 75f),
                new CloudLayer(85, 0.75f),
                new ForegroundLayer("background/foreground/Pine 1.png", new Vector2(foreGroundWidth, 148), 0.75f, -10f),
                new BirdLayer(Color.valueOf("#1b2d2d"), 96, 0.75f),
                new ForegroundLayer("background/foreground/Pine 2.png", new Vector2(foreGroundWidth, 199), 1f, -75f)
        };
    }

    @Override
    public void init() {
        birdTexture = TextureManager.instance.getTexture("background/Bird.png").split(Variables.BIRD_WIDTH, Variables.BIRD_HEIGHT);
        for(int i = 0; i < Variables.CLOUD_COUNT; i++) cloudTextures[i] = "background/clouds/Cloud " + i + ".png";
        TextureManager.instance.addTexture(cloudTextures);
        Time.load();
        for(Layer layer : layers) layer.init();
    }

    @Override
    public void resize(int width, int height) {
        this.scale = Math.max((int) ((float) height / foreGroundWidth), 1);
        for(Layer layer : layers) layer.resize(width, height);
    }

    private float lastX = 0f;

    @Override
    public void update(float delta) {
        layers[0].update(delta, Variables.HOUR_LENGTH);
        Entity player = GameManager.instance.player;
        if(player != null) {
            speed = Tools.floorBackgroundSpeed(lastX - player.getX()) * ((MovementComponent) player.getComponent(ComponentType.MOVEMENT)).getSpeed();
            lastX = player.getX();
        }
        for(int i = 1; i < layers.length; i++) layers[i].update(delta, speed);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for(Layer layer : layers) layer.render(batch, camera);
    }

    @Override
    public void delete() {
        for(Layer layer : layers) layer.delete();
        birdTexture = null;
        TextureManager.instance.removeTexture(cloudTextures);
        TextureManager.instance.removeTexture("background/Bird.png");
        Time.delete();
    }

}
