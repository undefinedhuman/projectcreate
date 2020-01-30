package de.undefinedhuman.sandboxgame.screen.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.EntityType;
import de.undefinedhuman.sandboxgame.entity.ecs.ComponentType;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.sandboxgame.entity.ecs.components.animation.AnimationComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.animation.AnimationParam;
import de.undefinedhuman.sandboxgame.entity.ecs.components.collision.CollisionComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.combat.CombatComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.interaction.InteractionComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.movement.MovementComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.name.NameComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.sprite.SpriteComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.sprite.SpriteParam;
import de.undefinedhuman.sandboxgame.entity.ecs.components.stats.health.HealthComponent;
import de.undefinedhuman.sandboxgame.entity.ecs.components.stats.mana.ManaComponent;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.items.ItemManager;
import de.undefinedhuman.sandboxgame.items.drop.DropItemManager;
import de.undefinedhuman.sandboxgame.projectiles.Projectile;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;
import de.undefinedhuman.sandboxgame.world.WorldGenerator;
import de.undefinedhuman.sandboxgame.world.WorldManager;
import de.undefinedhuman.sandboxgame.world.settings.BiomeSetting;
import de.undefinedhuman.sandboxgame.world.settings.WorldPreset;
import de.undefinedhuman.sandboxgame.world.settings.WorldSetting;

import java.util.HashMap;

public class GameManager {

    public static GameManager instance;

    public SpriteBatch gameBatch, guiBatch;
    public static OrthographicCamera gameCamera, guiCamera;
    private Viewport guiViewport;

    public Entity player, boss, furnace;
    public Projectile projectile = null;

    private float tempCam = 0;

    public GameManager() {
        gameCamera = new OrthographicCamera(); guiCamera = new OrthographicCamera();
        guiViewport = new ScreenViewport(guiCamera);
        gameBatch = new SpriteBatch(); guiBatch = new SpriteBatch();

        //CraftingInventory craftingInventory = new CraftingInventory();

    }

    public void init() {

        boss = new Entity(EntityType.ENTITY, new Vector2(360,360));
        boss.addComponent(new HealthComponent(boss,1000, new Vector2(0,120)));
        HashMap<String, SpriteParam> sprites = new HashMap<>();
        sprites.put("Main", new SpriteParam("Abomination.png",100, new Vector2(48,1)));
        boss.addComponent(new SpriteComponent(boss, sprites));
        HashMap<String, AnimationParam> animations = new HashMap<>();
        animations.put("Idle", new AnimationParam(new String[] {"Main"}, new Vector2(1,6),0.1f, Animation.PlayMode.LOOP));
        animations.put("Attack", new AnimationParam(new String[] {"Main"}, new Vector2(7,21),0.1f, Animation.PlayMode.LOOP));
        animations.put("Run", new AnimationParam(new String[] {"Main"}, new Vector2(25,32),0.05f, Animation.PlayMode.LOOP));
        animations.put("Hurt", new AnimationParam(new String[] {"Main"}, new Vector2(34,36),0.1f, Animation.PlayMode.NORMAL));
        animations.put("Die", new AnimationParam(new String[] {"Main"}, new Vector2(37,45),0.1f, Animation.PlayMode.NORMAL));
        boss.addComponent(new AnimationComponent(boss,"Idle", animations));
        boss.addComponent(new NameComponent(boss,"Abomination"));
        boss.addComponent(new CollisionComponent(boss,102,178, new Vector2(120,10)));
        boss.addComponent(new MovementComponent(boss,250,250,1000));
        boss.setPosition(100,6600);
        //EntityManager.instance.addEntity(100, boss);

        furnace = new Entity(EntityType.SCENERY, new Vector2(128,128));
        sprites = new HashMap<>();
        sprites.put("Main", new SpriteParam("props/Furnace.png",-1, new Vector2(7,1)));
        furnace.addComponent(new SpriteComponent(furnace, sprites));
        animations = new HashMap<>();
        animations.put("Idle", new AnimationParam(new String[] { "Main" }, new Vector2(0,0),0, Animation.PlayMode.NORMAL));
        animations.put("Running", new AnimationParam(new String[] {"Main"}, new Vector2(2,7),0.09f, Animation.PlayMode.LOOP));
        furnace.addComponent(new AnimationComponent(furnace,"Running", animations));
        furnace.addComponent(new CollisionComponent(furnace,84,62, new Vector2(22,0)));
        furnace.addComponent(new InteractionComponent(furnace,75, Input.Keys.F));
        furnace.setPosition(800,16*50);
        EntityManager.instance.addEntity(101, furnace);

        ((HealthComponent) player.getComponent(ComponentType.HEALTH)).getProfileOffset().set(0, 10);
        player.addComponent(new ManaComponent(player,100));
        player.addComponent(new CombatComponent(player));

        loadManager();

    }

    public void resize(int width, int height) {

        guiCamera.setToOrtho(false, width, height);
        guiViewport.update(width, height);
        guiCamera.update();

        gameCamera.viewportWidth = 1280;
        gameCamera.viewportHeight = ((float) height / (float) width) * 1280;
        gameCamera.update();

        GuiManager.instance.resize(width, height);
        InventoryManager.instance.resize(width, height);

    }

    public void update(float delta) {

        //if (!ClientManager.instance.isConnected()) Main.instance.setScreen(MenuScreen.instance);
        //ClientManager.instance.update(delta);

        // TODO Remove
        if(Gdx.input.isKeyJustPressed(Input.Keys.U)) World.instance = WorldGenerator.instance.generateWorld(new WorldPreset("Main", WorldSetting.DEV, BiomeSetting.DEV));
        if(Gdx.input.isKeyJustPressed(Input.Keys.H)) gameCamera.zoom++;
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) gameCamera.zoom--;
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) ((HealthComponent) player.getComponent(ComponentType.HEALTH)).damage(10);
        if(Gdx.input.isKeyJustPressed(Input.Keys.O)) ((HealthComponent) player.getComponent(ComponentType.HEALTH)).heal(10);

        //World.instance.lightManager.updateLight();

        if(projectile != null) projectile.update(delta);

        GuiManager.instance.update(delta);
        InventoryManager.instance.update(delta);
        DropItemManager.instance.update(delta);
        EntityManager.instance.update(delta);
        WorldManager.instance.update(delta);

        updateCamera();

    }

    public void render() {

        gameBatch.setColor(Color.WHITE);
        gameBatch.setProjectionMatrix(gameCamera.combined);
        gameBatch.begin();
        World.instance.computeBounds(gameCamera);
        //World.instance.renderBackLayer(gameBatch);
        EntityManager.instance.render(gameBatch);
        DropItemManager.instance.render(gameBatch);
        if(projectile != null) projectile.render(gameBatch);
        World.instance.renderMainLayer(gameBatch);

        gameBatch.draw(TextureManager.instance.getTexture("tree/Tree.png"),1000,16*50,128,128);
        gameBatch.end();

        guiViewport.apply();
        guiBatch.setProjectionMatrix(guiCamera.combined);
        guiBatch.begin();
        GuiManager.instance.renderGui(guiBatch, guiCamera);
        InventoryManager.instance.render(guiBatch, guiCamera);
        guiBatch.end();

    }

    public void delete() {

        BlueprintManager.instance.delete();
        DropItemManager.instance.delete();
        EntityManager.instance.delete();
        ItemManager.instance.delete();

    }

    private void loadManager() {
        ItemManager.instance.init();
        InventoryManager.instance.init();
        DropItemManager.instance = new DropItemManager();
    }

    private void updateCamera() {

        if(GameManager.instance.player != null) {

            tempCam = gameCamera.viewportHeight * gameCamera.zoom / 2.0F;

            gameCamera.position.set(Tools.lerp(gameCamera.position, new Vector3((int) (player.getX() + player.getWidth() / 2), (int) (player.getY() + player.getHeight() / 2),10),250));

            if (gameCamera.position.y < tempCam) gameCamera.position.y = (tempCam);
            if (gameCamera.position.y > World.instance.height * 16 - tempCam - 32.0F) gameCamera.position.y = (World.instance.height * 16 - tempCam - 32.0F);

            gameCamera.update();

        }

    }

}
