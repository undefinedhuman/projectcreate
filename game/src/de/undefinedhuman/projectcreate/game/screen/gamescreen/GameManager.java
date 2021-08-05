package de.undefinedhuman.projectcreate.game.screen.gamescreen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.background.BackgroundManager;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.gui.GuiManager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.item.drop.DropItemManager;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.projectiles.Projectile;
import de.undefinedhuman.projectcreate.game.world.World;
import de.undefinedhuman.projectcreate.game.world.WorldManager;

public class GameManager {

    private static volatile GameManager instance;
    public SpriteBatch batch;
    public Entity player;
    public Projectile projectile = null;

    private ManagerList manager;

    private GameManager() {
        batch = new SpriteBatch();
        CameraManager.getInstance();
        manager = new ManagerList();
    }

    public void init() {

        BackgroundManager.getInstance();
        BackgroundManager.getInstance().init();
        loadManager();

        //GuiManager.getInstance().addGui(CraftingInventory.getInstance());
    }

    public void resize(int width, int height) {
        CameraManager.getInstance().resize(width, height);

        manager.resize(width, height);

        BackgroundManager.getInstance().resize(width, height);

        GuiManager.getInstance().resize(width, height);
        InventoryManager.getInstance().resize(width, height);
    }

    public void update(float delta) {
        if (!ClientManager.getInstance().isConnected())
            Gdx.app.exit();

        if (projectile != null) projectile.update(delta);

        GuiManager.getInstance().update(delta);
        InventoryManager.getInstance().update(delta);
        DropItemManager.instance.update(delta);
        WorldManager.getInstance().update(delta);

        BackgroundManager.getInstance().update(delta);

        CameraManager.getInstance().update(delta);
    }

    public void render() {

        batch.setProjectionMatrix(CameraManager.gameCamera.combined);
        batch.begin();
        BackgroundManager.getInstance().render(batch, CameraManager.gameCamera);
        batch.end();
        // World.instance.renderBackLayer(gameBatch);
        batch.setProjectionMatrix(CameraManager.gameCamera.combined);
        batch.begin();
        DropItemManager.instance.render(batch);
        batch.end();
        EntityManager.getInstance().update(Main.delta);
        batch.setProjectionMatrix(CameraManager.gameCamera.combined);
        batch.begin();
        if (projectile != null) projectile.render(batch);
        World.instance.renderMainLayer(batch);
        batch.end();

        batch.setProjectionMatrix(CameraManager.guiCamera.combined);
        batch.begin();
        GuiManager.getInstance().renderGui(batch, CameraManager.guiCamera);
        InventoryManager.getInstance().render(batch, CameraManager.guiCamera);
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
        InventoryManager.getInstance().init();
        DropItemManager.instance = new DropItemManager();
    }

    public static GameManager getInstance() {
        if(instance != null)
            return instance;
        synchronized (GameManager.class) {
            if (instance == null)
                instance = new GameManager();
        }
        return instance;
    }

}
