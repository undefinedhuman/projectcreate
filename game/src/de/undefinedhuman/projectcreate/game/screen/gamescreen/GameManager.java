package de.undefinedhuman.projectcreate.game.screen.gamescreen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.background.BackgroundManager;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.entity.ecs.system.*;
import de.undefinedhuman.projectcreate.game.gui.GuiManager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.item.drop.DropItemManager;
import de.undefinedhuman.projectcreate.game.projectiles.Projectile;
import de.undefinedhuman.projectcreate.game.world.World;
import de.undefinedhuman.projectcreate.game.world.WorldManager;

public class GameManager {

    public static GameManager instance;
    public SpriteBatch batch;
    public Entity player;
    public Projectile projectile = null;

    private Engine engine;

    private ManagerList manager;

    public GameManager() {
        batch = new SpriteBatch();
        CameraManager.getInstance();
        manager = new ManagerList();
        engine = new Engine();
    }

    public void init() {
        BackgroundManager.getInstance();
        BackgroundManager.getInstance().init();
        loadManager();

        engine.addSystem(new AngleSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new ArmSystem());
        engine.addSystem(new InteractionSystem());
        engine.addSystem(new EquipSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(CameraManager.gameCamera));

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

        //if (!ClientManager.getInstance().isConnected()) Main.instance.setScreen(MenuScreen.instance);
        //ClientManager.getInstance().update(delta);
        manager.update(delta);

        if (projectile != null) projectile.update(delta);

        GuiManager.getInstance().update(delta);
        InventoryManager.getInstance().update(delta);
        DropItemManager.instance.update(delta);
        WorldManager.getInstance().update(delta);

        BackgroundManager.getInstance().update(delta);

        CameraManager.getInstance().update(delta);
    }

    public void render() {

        engine.update(Main.delta);

        batch.setProjectionMatrix(CameraManager.gameCamera.combined);
        batch.begin();
        BackgroundManager.getInstance().render(batch, CameraManager.gameCamera);
        // World.instance.renderBackLayer(gameBatch);
        DropItemManager.instance.render(batch);
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
        engine.removeAllEntities();
        ItemManager.getInstance().delete();
        batch.dispose();
    }

    private void loadManager() {
        ItemManager.getInstance().init();
        InventoryManager.getInstance().init();
        DropItemManager.instance = new DropItemManager();
    }

    public Engine getEngine() {
        return engine;
    }
}
