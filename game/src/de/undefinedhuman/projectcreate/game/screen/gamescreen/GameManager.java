package de.undefinedhuman.projectcreate.game.screen.gamescreen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.ecs.Mappers;
import de.undefinedhuman.projectcreate.core.ecs.transform.TransformComponent;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.ecs.blueprint.BlueprintManager;
import de.undefinedhuman.projectcreate.engine.ecs.entity.EntityManager;
import de.undefinedhuman.projectcreate.engine.gui.GuiManager;
import de.undefinedhuman.projectcreate.engine.gui.text.Text;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.projectcreate.engine.gui.transforms.offset.RelativeOffset;
import de.undefinedhuman.projectcreate.engine.utils.ManagerList;
import de.undefinedhuman.projectcreate.game.Main;
import de.undefinedhuman.projectcreate.game.background.BackgroundManager;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.inventory.InventoryManager;
import de.undefinedhuman.projectcreate.game.item.drop.DropItemManager;
import de.undefinedhuman.projectcreate.game.network.ClientManager;
import de.undefinedhuman.projectcreate.game.projectiles.Projectile;
import de.undefinedhuman.projectcreate.game.world.World;
import de.undefinedhuman.projectcreate.game.world.WorldManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameManager {

    private static volatile GameManager instance;
    public SpriteBatch batch;
    public Entity player;
    public Projectile projectile = null;

    private ManagerList manager;

    private Text ping, player1Text, player2Text;

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
        GuiManager.getInstance().addGui(player1Text = new Text(""), player2Text = new Text(""), ping = new Text(""));
        player1Text.setPosition(new CenterConstraint(), new CenterConstraint()).setOffset(new PixelOffset(-200), new CenterOffset());
        player2Text.setPosition(new CenterConstraint(), new CenterConstraint()).setOffset(new PixelOffset(200), new CenterOffset());
        ping.setPosition(new PixelConstraint(0), new RelativeConstraint(1f)).setOffset(new PixelOffset(10), new RelativeOffset(-1f));
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

        ClientManager.getInstance().update(Main.delta);

        ping.setText(ClientManager.getInstance().getReturnTime());

        List<Entity> entities = EntityManager.getInstance().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        if(entities.size() > 0) {
            Entity player1 = entities.get(0);
            TransformComponent transformComponent = Mappers.TRANSFORM.get(player1);
            player1Text.setText((int) (transformComponent.getPosition().x * 1000) / 1000f);
        }

        if(entities.size() > 1) {
            Entity player2 = entities.get(1);
            TransformComponent transformComponent = Mappers.TRANSFORM.get(player2);
            player2Text.setText((int) (transformComponent.getPosition().x * 1000) / 1000f);
        }

        if (projectile != null) projectile.update(delta);

        GuiManager.getInstance().update(delta);
        InventoryManager.getInstance().update(delta);
        DropItemManager.instance.update(delta);
        WorldManager.getInstance().update(delta);

        BackgroundManager.getInstance().update(delta);
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
