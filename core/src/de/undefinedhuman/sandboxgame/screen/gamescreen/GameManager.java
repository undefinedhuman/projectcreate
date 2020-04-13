package de.undefinedhuman.sandboxgame.screen.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.undefinedhuman.sandboxgame.background.BackgroundManager;
import de.undefinedhuman.sandboxgame.engine.ressources.texture.TextureManager;
import de.undefinedhuman.sandboxgame.engine.utils.ManagerList;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.item.drop.DropItemManager;
import de.undefinedhuman.sandboxgame.projectiles.Projectile;
import de.undefinedhuman.sandboxgame.utils.Tools;
import de.undefinedhuman.sandboxgame.world.World;
import de.undefinedhuman.sandboxgame.world.WorldManager;

public class GameManager {

    public static GameManager instance;
    public static OrthographicCamera gameCamera, guiCamera;
    public SpriteBatch gameBatch, guiBatch;
    public Entity player;
    public Projectile projectile = null;
    private Viewport guiViewport;

    private ManagerList manager;

    public GameManager() {
        gameCamera = new OrthographicCamera();
        guiCamera = new OrthographicCamera();
        guiViewport = new ScreenViewport(guiCamera);
        gameBatch = new SpriteBatch();
        guiBatch = new SpriteBatch();

        manager = new ManagerList();

        Gdx.graphics.setVSync(false);

        //CraftingInventory craftingInventory = new CraftingInventory();

    }

    public void init() {

        BackgroundManager.instance = new BackgroundManager();
        BackgroundManager.instance.init();

        loadManager();

    }

    public void resize(int width, int height) {

        BackgroundManager.instance.resize(width, height);
        manager.resize(width, height);

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
        BackgroundManager.instance.update(delta);
        manager.update(delta);

        if (projectile != null) projectile.update(delta);

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
        BackgroundManager.instance.render(gameBatch, gameCamera);
        manager.render(gameBatch, gameCamera);
        World.instance.computeBounds(gameCamera);
        //World.instance.renderBackLayer(gameBatch);
        EntityManager.instance.render(gameBatch);
        DropItemManager.instance.render(gameBatch);
        if (projectile != null) projectile.render(gameBatch);
        World.instance.renderMainLayer(gameBatch);

        gameBatch.draw(TextureManager.instance.getTexture("tree/Tree.png"), 103 * Variables.BLOCK_SIZE, Variables.BLOCK_SIZE * 50, 128, 128);
        gameBatch.end();

        guiViewport.apply();
        guiBatch.setProjectionMatrix(guiCamera.combined);
        guiBatch.begin();
        manager.renderGui(guiBatch, guiCamera);
        GuiManager.instance.renderGui(guiBatch, guiCamera);
        InventoryManager.instance.render(guiBatch, guiCamera);
        guiBatch.end();

    }

    public void delete() {

        manager.delete();

        BackgroundManager.instance.delete();
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
        if (GameManager.instance.player == null) return;
        float tempCam = gameCamera.viewportHeight * gameCamera.zoom * 0.5f;
        gameCamera.position.set(Tools.lerp(gameCamera.position, new Vector3().set(player.getCenterPosition(), 0), 300));
        gameCamera.position.y = Tools.clamp(gameCamera.position.y, tempCam, World.instance.height * 16 - tempCam - 32);
        gameCamera.update();
    }

}
