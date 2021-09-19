package de.undefinedhuman.projectcreate.game.background;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.projectcreate.core.ecs.movement.MovementComponent;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.engine.resources.texture.TextureManager;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.Variables;
import de.undefinedhuman.projectcreate.game.background.birds.BirdLayer;
import de.undefinedhuman.projectcreate.game.background.clouds.CloudLayer;
import de.undefinedhuman.projectcreate.game.background.layers.BackgroundLayer;
import de.undefinedhuman.projectcreate.game.background.layers.ForegroundLayer;
import de.undefinedhuman.projectcreate.game.screen.gamescreen.GameManager;
import de.undefinedhuman.projectcreate.game.utils.Tools;

public class BackgroundManager extends Manager {

    private static volatile BackgroundManager instance;

    public String[] cloudTextures = new String[Variables.CLOUD_COUNT];
    public TextureRegion[] birdTexture;
    public float scale, speed;

    private Layer[] layers;

    private BackgroundManager() {
        layers = new Layer[] {
                new BackgroundLayer(new Vector2(640, 300)),
                new CloudLayer(225, 3),
                new ForegroundLayer("background/foreground/Mountain 1.png", new Vector2(Variables.BASE_BACKGROUND_WIDTH, 127), 0.25f, 135f, 0.55f),
                new BirdLayer(Color.valueOf("#1b2d2d"), 175, 0.5f),
                new CloudLayer(175, 2),
                new ForegroundLayer("background/foreground/Mountain 2.png", new Vector2(Variables.BASE_BACKGROUND_WIDTH, 162), 0.5f, 75f, 0.7f),
                new CloudLayer(80, 1),
                new ForegroundLayer("background/foreground/Pine 1.png", new Vector2(Variables.BASE_BACKGROUND_WIDTH, 148), 0.75f, -10f, 0.85f),
                new ForegroundLayer("background/foreground/Pine 2.png", new Vector2(Variables.BASE_BACKGROUND_WIDTH, 199), 1f, -75f, 1f)
        };
    }

    @Override
    public void init() {
        birdTexture = TextureManager.getInstance().getTexture("background/Bird.png").split((int) Variables.BIRD_SIZE.x, (int) Variables.BIRD_SIZE.y)[0];
        for(int i = 0; i < Variables.CLOUD_COUNT; i++) cloudTextures[i] = "background/clouds/Cloud " + i + ".png";
        TextureManager.getInstance().loadTextures(cloudTextures);
        Time.load();
        for(Layer layer : layers)
            layer.init();
    }

    @Override
    public void resize(int width, int height) {
        this.scale = Math.max((int) ((float) height / Variables.BASE_BACKGROUND_WIDTH) * 0.25f, 1);
        for(Layer layer : layers) layer.resize(width, height);
    }

    private float lastX = 0f;

    @Override
    public void update(float delta) {
        layers[0].update(delta, 5f);
        Entity player = GameManager.getInstance().player;
        if(player == null) return;
        speed = Math.abs(Tools.floorBackgroundSpeed(lastX - player.getComponent(TransformComponent.class).getX())) * -player.getComponent(MovementComponent.class).velocity.x / delta;
        lastX = player.getComponent(TransformComponent.class).getX();
        for(int i = 1; i < layers.length; i++)
            layers[i].update(delta, speed);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        for(Layer layer : layers) layer.render(batch, camera);
    }

    @Override
    public void delete() {
        for(Layer layer : layers) layer.delete();
        TextureManager.getInstance().removeTextures(cloudTextures);
        TextureManager.getInstance().removeTextures("background/Bird.png");
        birdTexture = new TextureRegion[0];
        Time.delete();
    }

    public static BackgroundManager getInstance() {
        if (instance == null) {
            synchronized (BackgroundManager.class) {
                if (instance == null)
                    instance = new BackgroundManager();
            }
        }
        return instance;
    }

}
