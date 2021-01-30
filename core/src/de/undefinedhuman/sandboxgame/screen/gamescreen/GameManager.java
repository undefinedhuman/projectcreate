package de.undefinedhuman.sandboxgame.screen.gamescreen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.background.BackgroundManager;
import de.undefinedhuman.sandboxgame.crafting.gui.CraftingInventory;
import de.undefinedhuman.sandboxgame.engine.camera.CameraManager;
import de.undefinedhuman.sandboxgame.engine.utils.ManagerList;
import de.undefinedhuman.sandboxgame.entity.Entity;
import de.undefinedhuman.sandboxgame.entity.EntityManager;
import de.undefinedhuman.sandboxgame.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.sandboxgame.gui.GuiManager;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.item.drop.DropItemManager;
import de.undefinedhuman.sandboxgame.projectiles.Projectile;
import de.undefinedhuman.sandboxgame.world.World;
import de.undefinedhuman.sandboxgame.world.WorldManager;

public class GameManager {

    public static GameManager instance;
    public SpriteBatch batch;
    public Entity player;
    public Projectile projectile = null;

    private ManagerList manager;

    public GameManager() {
        batch = new SpriteBatch();
        CameraManager.instance = new CameraManager();
        manager = new ManagerList();
    }

    public void init() {
        BackgroundManager.instance = new BackgroundManager();
        BackgroundManager.instance.init();
        loadManager();

        GuiManager.instance.addGui(new CraftingInventory());
    }

    public void resize(int width, int height) {

        CameraManager.instance.resize(width, height);

        manager.resize(width, height);

        BackgroundManager.instance.resize(width, height);

        GuiManager.instance.resize(width, height);
        InventoryManager.instance.resize(width, height);

    }

    public void update(float delta) {

        //if (!ClientManager.instance.isConnected()) Main.instance.setScreen(MenuScreen.instance);
        //ClientManager.instance.update(delta);
        manager.update(delta);

        if (projectile != null) projectile.update(delta);

        GuiManager.instance.update(delta);
        InventoryManager.instance.update(delta);
        DropItemManager.instance.update(delta);
        EntityManager.instance.update(delta);
        WorldManager.instance.update(delta);

        BackgroundManager.instance.update(delta);

        CameraManager.instance.update(delta);

    }

    public void render() {

        batch.setProjectionMatrix(CameraManager.gameCamera.combined);
        batch.begin();
        BackgroundManager.instance.render(batch, CameraManager.gameCamera);
        // World.instance.renderBackLayer(gameBatch);
        EntityManager.instance.render(batch, CameraManager.gameCamera);
        DropItemManager.instance.render(batch);
        if (projectile != null) projectile.render(batch);
        World.instance.renderMainLayer(batch);
        batch.end();

        batch.setProjectionMatrix(CameraManager.guiCamera.combined);
        batch.begin();
        GuiManager.instance.renderGui(batch, CameraManager.guiCamera);
        InventoryManager.instance.render(batch, CameraManager.guiCamera);
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

}
