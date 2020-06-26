package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.sandboxgame.engine.camera.CameraManager;
import de.undefinedhuman.sandboxgame.engine.resources.font.Font;
import de.undefinedhuman.sandboxgame.engine.utils.Manager;
import de.undefinedhuman.sandboxgame.engine.utils.Variables;
import de.undefinedhuman.sandboxgame.equip.EquipScreen;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.transforms.Axis;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.sandboxgame.inventory.player.DragAndDrop;
import de.undefinedhuman.sandboxgame.inventory.player.Selector;
import de.undefinedhuman.sandboxgame.inventory.player.SidePanel;
import de.undefinedhuman.sandboxgame.item.ItemManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

import java.util.Set;

public class InventoryManager extends Manager {

    public static InventoryManager instance;

    private Gui[] slots;
    private int maxSlot = -1;

    private DragAndDrop dragAndDrop;
    private Selector selector;
    private SidePanel sidePanel;
    private Inventory inventory;
    private InspectScreen inspectScreen;
    private EquipScreen equipScreen;

    public InventoryManager() {

        if (instance == null) instance = this;

        dragAndDrop = new DragAndDrop();
        slots = new Gui[4];

        selector = new Selector();
        sidePanel = new SidePanel();
        inspectScreen = new InspectScreen();
        inspectScreen.setVisible(false);
        equipScreen = new EquipScreen();
        equipScreen.setTitle("Equip", Font.Title, Color.WHITE);
        equipScreen.setVisible(false);
        inventory = new Inventory(10, 5);
        inventory.setTitle("Inventory", Font.Title, Color.WHITE);
        inventory.setVisible(false);

        dragAndDrop.addTarget(inventory, selector, equipScreen);

    }

    @Override
    public void init() {
        super.init();
        addTempItems(selector, 1, 2);
        addTempItems(inventory, ItemManager.instance.getItems().keySet());
        selector.setSelected(0);
    }

    @Override
    public void resize(int width, int height) {
        dragAndDrop.resize(width, height);
        sidePanel.resize(width, height);
        selector.resize(width, height);
        for (Gui slot : slots) if (slot != null) slot.resize(width, height);
    }

    @Override
    public void update(float delta) {
        selector.updateSelector();
        sidePanel.update(delta);
        for (Gui slot : slots) if (slot != null) slot.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        dragAndDrop.update(camera);
        selector.render(batch, camera);
        sidePanel.render(batch, camera);
        for (Gui slot : slots) if (slot != null) slot.render(batch, camera);
        dragAndDrop.render(batch, camera);
    }

    private void addTempItems(Inventory inventory, int... ids) {
        for (int id : ids) inventory.addItem(id, ItemManager.instance.getItem(id).maxAmount.getInt());
    }

    private void addTempItems(Inventory inventory, Set<Integer> ids) {
        for (int id : ids) inventory.addItem(id, ItemManager.instance.getItem(id).maxAmount.getInt());
    }

    public Selector getSelector() {
        return selector;
    }

    public void addGuiToSlot(Gui gui) {
        if (gui == null) return;
        if (!sidePanel.isVisible()) sidePanel.setVisible(true);
        maxSlot = Tools.clamp(maxSlot + 1, 0, 3);
        if (slots[maxSlot] != null) removeGui(maxSlot);
        this.slots[maxSlot] = gui;
        setGuiVisible(maxSlot, gui);
        for (int i = maxSlot; i < slots.length; i++) if (slots[i] != null) maxSlot = i;
    }

    public void removeGuiFromSlot(Gui gui) {
        if (gui == null) return;
        int id = -1;
        for (int i = 0; i < slots.length; i++) if (gui == slots[i]) id = i;
        if (id != -1) {
            gui.setVisible(false);
            slots[id] = null;
        } else return;
        for (int i = id + 1; i < slots.length; i++)
            if (slots[i] != null) {
                addGuiToSlot(i - 1, slots[i]);
                slots[i] = null;
            } else break;
        maxSlot = 3;
        for (int i = 0; i < slots.length; i++)
            if (slots[i] == null) {
                maxSlot = Tools.clamp(i - 1, -1, 3);
                break;
            }
    }

    public void removeGuiFromSlot(int id) {
        if (Tools.isInRange(id, 0, 3) && slots[id] != null) {
            slots[id].setVisible(false);
            slots[id] = null;
        }
        for (int i = id + 1; i < slots.length; i++) addGuiToSlot(i - 1, slots[i]);
        maxSlot--;
    }

    public void addGuiToSlot(int id, Gui gui) {
        if (gui == null) return;
        if (!sidePanel.isVisible()) sidePanel.setVisible(true);
        if (!Tools.isInRange(id, 0, 3)) return;
        if (slots[id] == null) this.slots[id] = gui;
        for (int i = 0; i < slots.length; i++) if (slots[i] != null) maxSlot = i;
        setGuiVisible(id, gui);
    }

    public void setGuiVisible(int id, Gui gui) {
        gui.setPosition(new RelativeConstraint(1), new CenterConstraint()).setOffsetX(new PixelOffset(Variables.BASE_WINDOW_WIDTH + getWidth(id)));
        gui.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gui.setVisible(true);
    }

    private int getWidth(int id) {
        int width = (int) (-25 - 10 * (id + 1) - sidePanel.getBaseValue(Axis.WIDTH));
        for (int i = 0; i < id; i++) if (slots[i] != null) width -= slots[i].getBaseValue(Axis.WIDTH);
        return width;
    }

    private void removeGui(int id) {
        if (id != -1) {
            slots[id].setVisible(false);
            slots[id] = null;
        }
    }

    public void removeAllGui() {
        for (int i = 0; i < slots.length; i++) {
            slots[i].setVisible(false);
            slots[i] = null;
        }
        maxSlot = -1;
    }

    public boolean isFull(int id, int amount) {
        return inventory.isFull(id, amount) && selector.isFull(id, amount);
    }

    public int addItem(int id, int amount) {
        int i = selector.addItem(id, amount);
        if (i != 0) i = inventory.addItem(id, i);
        return i;
    }

    public void openGui(Gui gui) {
        if (gui.isVisible()) removeGuiFromSlot(gui);
        else addGuiToSlot(gui);
    }

    public boolean isInventoryOpened() {
        return inventory.isVisible();
    }

    public boolean canUseItem() {
        return !(dragAndDrop.isMoving() || overGui());
    }

    public boolean overGui() {
        return Tools.isOverGuis(CameraManager.guiCamera, selector, sidePanel, slots[0], slots[1], slots[2], slots[3]);
    }

    public void handleClick(int id) {

        switch (id) {

            case 0:
                openGui(inventory);
                break;
            case 1:
                openGui(equipScreen);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                break;

        }

    }

}
