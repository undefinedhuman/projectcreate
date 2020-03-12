package de.undefinedhuman.sandboxgame.inventory;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.sandboxgame.engine.ressources.font.Font;
import de.undefinedhuman.sandboxgame.gui.Gui;
import de.undefinedhuman.sandboxgame.gui.text.Text;
import de.undefinedhuman.sandboxgame.gui.texture.GuiTemplate;
import de.undefinedhuman.sandboxgame.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.sandboxgame.engine.items.Item;
import de.undefinedhuman.sandboxgame.engine.items.ItemManager;
import de.undefinedhuman.sandboxgame.utils.Tools;

public class InspectScreen extends Gui {

    private Text nameText, descriptionText;
    private Gui previewImage;

    public InspectScreen() {

        super(GuiTemplate.SMALL_PANEL);
        setScale(Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 5), Tools.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10));
        Item item = new ItemManager().getItem(13);
        previewImage = new Gui(item.inspectTexture);
        previewImage.set("r0.5", "r0.85", "p32", "p32").setCentered();
        addChild(previewImage);
        nameText = new Text(item.name).setFont(Font.Title).setLineLength(new RelativeConstraint(0.75f));
        nameText.setColor(Color.RED).setGuiScale(0.75f);
        nameText.setPosition("r0.5", "r0.75").setCentered();
        this.descriptionText = new Text(item.desc).setFont(Font.Title).setLineLength(new RelativeConstraint(0.8f));
        descriptionText.setGuiScale(0.5f);
        descriptionText.setPosition("r0.5", "r0.675f").setCenteredX();
        addChild(nameText, descriptionText);
        setVisible(false);

    }

    public void openInspectScreen(Item item) {
        previewImage.setTexture(item.inspectTexture);
        nameText.setText(item.name);
        nameText.setColor(Color.RED);
        descriptionText.setText(item.desc);
    }

}
