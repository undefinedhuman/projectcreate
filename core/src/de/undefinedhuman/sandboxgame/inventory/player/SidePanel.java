package de.undefinedhuman.sandboxgame.inventory.player;

import com.badlogic.gdx.math.Vector2;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.event.ClickEvent;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.inventory.InventoryManager;
import de.undefinedhuman.sandboxgame.utils.Variables;

public class SidePanel extends Gui {

    private Gui[] buttons = new Gui[10];
    private String[] textures = new String[] {  "gui/Inventory Icon.png", "gui/Chestplate-Preview.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png", "Unknown.png" };

    public SidePanel() {

        super(GuiTemplate.SMALL_PANEL);
        Vector2 invScale = Variables.getInventoryScale(new Vector2(GuiTemplate.SMALL_PANEL.cornerSize, GuiTemplate.SMALL_PANEL.cornerSize),1,10);
        set("r1","r0.5","p" + invScale.x,"p" + invScale.y).setOffsetX("p-25").setCenteredX(-1).setCenteredY();

        for(int i = 0; i < buttons.length; i++) {

            int k = buttons.length-i-1;
            Gui child = new Gui(textures[k]);
            child.set("r0.5","r0.5","p" + Variables.ITEM_SIZE,"p" + Variables.ITEM_SIZE).setCentered();
            buttons[k] = new Gui(GuiTemplate.SLOT).addChild(child);
            buttons[k]
                    .setScale("p" + Variables.SLOT_SIZE,"p" + Variables.SLOT_SIZE)
                    .setPosition("p" + getTemplate().cornerSize,"p" + (getTemplate().cornerSize + (Variables.SLOT_SIZE + Variables.SLOT_SPACE) * i));
            buttons[k].addEvent(new ClickEvent() {
                @Override
                public void onClick() {
                    InventoryManager.instance.handleClick(k);
                }
            });
            addChild(buttons[k]);

        }

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
