package de.undefinedhuman.projectcreate.game.screen.gamescreen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.game.background.BackgroundManager;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.entity.Entity;
import de.undefinedhuman.projectcreate.game.entity.EntityManager;
import de.undefinedhuman.projectcreate.game.entity.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.game.gui.GuiManager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.item.ItemManager;
import de.undefinedhuman.projectcreate.game.item.drop.DropItemManager;
import de.undefinedhuman.projectcreate.game.projectiles.Projectile;
import de.undefinedhuman.projectcreate.game.world.World;
import de.undefinedhuman.projectcreate.game.world.WorldManager;

public class GameManager {

    public static GameManager instance;
    public SpriteBatch batch;
    public Entity player;
    public Projectile projectile = null;

    private ManagerList manager;

    public GameManager() {
        batch = new SpriteBatch();
        CameraManager.getInstance();
        manager = new ManagerList();
    }

    public void init() {
        BackgroundManager.getInstance();
        BackgroundManager.getInstance().init();
        loadManager();

        //GuiManager.instance.addGui(new CraftingInventory());
    }

    public void resize(int width, int height) {
        CameraManager.getInstance().resize(width, height);

        manager.resize(width, height);

        BackgroundManager.getInstance().resize(width, height);

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
        EntityManager.getInstance().update(delta);
        WorldManager.instance.update(delta);

        BackgroundManager.getInstance().update(delta);

        CameraManager.getInstance().update(delta);
    }

    public void render() {

        batch.setProjectionMatrix(CameraManager.gameCamera.combined);
        batch.begin();
        BackgroundManager.getInstance().render(batch, CameraManager.gameCamera);
        // World.instance.renderBackLayer(gameBatch);
        EntityManager.getInstance().render(batch, CameraManager.gameCamera);
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
        BackgroundManager.getInstance().delete();
        BlueprintManager.getInstance().delete();
        DropItemManager.instance.delete();
        EntityManager.getInstance().delete();
        ItemManager.getInstance().delete();
        batch.dispose();
    }

    private void loadManager() {
        ItemManager.getInstance().init();
        InventoryManager.instance.init();
        DropItemManager.instance = new DropItemManager();
    }

}
