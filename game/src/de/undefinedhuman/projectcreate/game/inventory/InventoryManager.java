package de.undefinedhuman.projectcreate.game.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.undefinedhuman.projectcreate.core.crafting.RecipeItem;
import de.undefinedhuman.projectcreate.engine.utils.Manager;
import de.undefinedhuman.projectcreate.engine.utils.ds.MultiMap;
import de.undefinedhuman.projectcreate.game.camera.CameraManager;
import de.undefinedhuman.projectcreate.game.equip.EquipScreen;
import de.undefinedhuman.projectcreate.game.gui.Gui;
import de.undefinedhuman.projectcreate.game.gui.GuiManager;
import de.undefinedhuman.projectcreate.game.gui.transforms.Axis;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.offset.CenterOffset;
import de.undefinedhuman.projectcreate.game.gui.transforms.offset.PixelOffset;
import de.undefinedhuman.projectcreate.game.inventory.player.*;
import de.undefinedhuman.projectcreate.game.item.ItemManager;
import de.undefinedhuman.projectcreate.game.item.listener.ItemChangeListener;
import de.undefinedhuman.projectcreate.game.utils.Tools;

import java.util.Collection;
import java.util.Set;

public class InventoryManager extends Manager {

    public static InventoryManager instance;

    private Gui[] slots = new Gui[4];
    private int maxSlot = -1;

    private MultiMap<Integer, ItemChangeListener> listeners = new MultiMap<>();

    private DragAndDrop dragAndDrop;

    public InventoryManager() {
        if (instance == null) instance = this;
        dragAndDrop = new DragAndDrop(CameraManager.guiCamera);
        GuiManager.instance.addGui(new Selector(), new SidePanel(), new InspectScreen(), new EquipScreen(), new PlayerInventory());
        dragAndDrop.addTarget(PlayerInventory.instance, Selector.instance, EquipScreen.getInstance());
    }

    @Override
    public void init() {
        super.init();
        addTempItems(Selector.instance, 1, 2);
        addTempItems(PlayerInventory.instance, ItemManager.getInstance().getItems().keySet());
        Selector.instance.setSelected(0);
    }

    @Override
    public void resize(int width, int height) {
        dragAndDrop.resize(width, height);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        dragAndDrop.update(camera);
        dragAndDrop.render(batch, camera);
    }

    private void addTempItems(Inventory inventory, int... ids) {
        for (int id : ids)
            inventory.addItem(id, ItemManager.getInstance().getItem(id).maxAmount.getValue());
    }

    private void addTempItems(Inventory inventory, Set<Integer> ids) {
        for (int id : ids) inventory.addItem(id, ItemManager.getInstance().getItem(id).maxAmount.getValue());
    }

    public void addGuiToSlot(Gui gui) {
        if (gui == null) return;
        if (!SidePanel.instance.isVisible()) SidePanel.instance.setVisible(true);
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
        if (!SidePanel.instance.isVisible()) SidePanel.instance.setVisible(true);
        if (!Tools.isInRange(id, 0, 3)) return;
        if (slots[id] == null) this.slots[id] = gui;
        for (int i = 0; i < slots.length; i++) if (slots[i] != null) maxSlot = i;
        setGuiVisible(id, gui);
    }

    public void setGuiVisible(int id, Gui gui) {
        gui.setPosition(new RelativeConstraint(1), new CenterConstraint()).setOffset(new PixelOffset(getWidth(id)), new CenterOffset());
        gui.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gui.setVisible(true);
    }

    private int getWidth(int id) {
        int width = (int) (-25 - 10 * (id + 1) - SidePanel.instance.getBaseValue(Axis.WIDTH));
        for (int i = 0; i <= id; i++) if (slots[i] != null) width -= slots[i].getBaseValue(Axis.WIDTH);
        return width;
    }

    private void removeGui(int id) {
        if (id == -1) return;
        slots[id].setVisible(false);
        slots[id] = null;
    }

    public void removeAllGui() {
        for (int i = 0; i < slots.length; i++) {
            slots[i].setVisible(false);
            slots[i] = null;
        }
        maxSlot = -1;
    }

    public synchronized boolean isFull(int id, int amount) {
        return PlayerInventory.instance.isFull(id, amount) && Selector.instance.isFull(id, amount);
    }

    public synchronized int addItem(int id, int amount) {
        int i = Selector.instance.addItem(id, amount);
        if (i != 0) i = PlayerInventory.instance.addItem(id, i);
        notifyListeners(id, amount - i);
        return i;
    }

    public synchronized int removeItem(int id, int amount) {
        int i = Selector.instance.removeItem(id, amount);
        if (i != 0) i = PlayerInventory.instance.removeItem(id, i);
        notifyListeners(id, amount - i);
        return i;
    }

    public synchronized boolean craftItem(int id, int quantity, Collection<RecipeItem> ingredients) {
        for(RecipeItem ingredient : ingredients)
            if(amountOf(ingredient.getID()) < ingredient.quantity.getValue())
                return false;
        if(isFull(id, quantity))
            return false;
        for(RecipeItem ingredient : ingredients)
            removeItem(ingredient.getID(), ingredient.quantity.getValue());
        addItem(id, quantity);
        return true;
    }

    public synchronized int amountOf(int id) {
        return PlayerInventory.instance.amountOf(id) + Selector.instance.amountOf(id);
    }

    public void addListener(int id, ItemChangeListener listener) {
        this.listeners.add(id, listener);
    }

    public void removeListener(int id, ItemChangeListener listener) {
        this.listeners.removeValue(id, listener);
    }

    private void notifyListeners(int id, int amount) {
        for(ItemChangeListener listener : listeners.getValuesWithKey(id))
            listener.notify(amount);
    }

    public void openGui(Gui gui) {
        if (gui.isVisible()) removeGuiFromSlot(gui);
        else addGuiToSlot(gui);
    }

    public boolean isInventoryOpened() {
        return PlayerInventory.instance.isVisible();
    }

    public boolean canUseItem() {
        return !(dragAndDrop.isMoving() || overGui());
    }

    public boolean overGui() {
        return Tools.isOverGuis(CameraManager.guiCamera, Selector.instance, SidePanel.instance, slots[0], slots[1], slots[2], slots[3]);
    }

    public void handleClick(int id) {

        switch (id) {

            case 0:
                openGui(PlayerInventory.instance);
                break;
            case 1:
                openGui(EquipScreen.getInstance());
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
