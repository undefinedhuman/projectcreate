package de.undefinedhuman.sandboxgame.screen.gamescreen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import de.undefinedhuman.sandboxgame.background.BackgroundManager;
import de.undefinedhuman.sandboxgame.engine.camera.CameraManager;
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
    public SpriteBatch batch;
    public Entity player;
    public Projectile projectile = null;

    private ManagerList manager;

    public GameManager() {
        gameCamera = new OrthographicCamera();
        guiCamera = new OrthographicCamera();

        batch = new SpriteBatch();

        manager = new ManagerList();

        //CraftingInventory craftingInventory = new CraftingInventory();

        CameraManager.instance = new CameraManager();

    }

    public void init() {

        BackgroundManager.instance = new BackgroundManager();
        BackgroundManager.instance.init();

        loadManager();

    }

    public void resize(int width, int height) {

        guiCamera.setToOrtho(false, width, height);
        gameCamera.setToOrtho(false, width/Variables.GAME_CAMERA_ZOOM, height/ Variables.GAME_CAMERA_ZOOM);

        CameraManager.instance.resize(width, height);

        manager.resize(width, height);

        BackgroundManager.instance.resize(width, height);

        GuiManager.instance.resize(width, height);
        InventoryManager.instance.resize(width, height);

    }

    public void update(float delta) {

        //if (!ClientManager.instance.isConnected()) Main.instance.setScreen(MenuScreen.instance);
        //ClientManager.instance.update(delta);
        BackgroundManager.instance.update(delta);
        manager.update(delta);

        CameraManager.instance.update(delta);

        if (projectile != null) projectile.update(delta);

        GuiManager.instance.update(delta);
        InventoryManager.instance.update(delta);
        DropItemManager.instance.update(delta);
        EntityManager.instance.update(delta);
        WorldManager.instance.update(delta);

        updateCamera();

    }

    public void render() {

        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();
        BackgroundManager.instance.render(batch, gameCamera);
        World.instance.computeBounds(gameCamera);
        //World.instance.renderBackLayer(gameBatch);
        EntityManager.instance.render(batch);
        DropItemManager.instance.render(batch);
        if (projectile != null) projectile.render(batch);
        World.instance.renderMainLayer(batch);
        batch.end();

        batch.setProjectionMatrix(guiCamera.combined);
        batch.begin();
        GuiManager.instance.renderGui(batch, guiCamera);
        InventoryManager.instance.render(batch, guiCamera);
        batch.end();

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
        float cameraYBounds = gameCamera.viewportHeight * gameCamera.zoom * 0.5f;
        gameCamera.position.set(new Vector3(player.getCenterPosition(), 0));
        // If lerp gets added again, make sure, that if the player gets teleported to the other side of the world the camera sets with him, otherwise there will be some kind of laggy movement
        gameCamera.position.y = Tools.clamp(gameCamera.position.y, cameraYBounds, World.instance.height * Variables.BLOCK_SIZE - cameraYBounds - Variables.BLOCK_SIZE*2);
        gameCamera.update();
    }

}
