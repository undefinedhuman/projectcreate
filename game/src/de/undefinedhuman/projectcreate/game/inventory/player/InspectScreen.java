package de.undefinedhuman.projectcreate.game.inventory.player;

import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.engine.resources.font.Font;
import de.undefinedhuman.projectcreate.game.gui.Gui;
import de.undefinedhuman.projectcreate.game.gui.text.Text;
import de.undefinedhuman.projectcreate.game.gui.texture.GuiTemplate;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.CenterConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.PixelConstraint;
import de.undefinedhuman.projectcreate.game.gui.transforms.constraints.RelativeConstraint;
import de.undefinedhuman.projectcreate.game.item.ItemManager;

public class InspectScreen extends Gui {

    private static volatile InspectScreen instance;

    private Text nameText, descriptionText;
    private Gui previewImage;

    private InspectScreen() {
        super(GuiTemplate.SMALL_PANEL);
        // setSize(Tools.getInventoryWidth(GuiTemplate.SMALL_PANEL, 5), Tools.getInventoryHeight(GuiTemplate.SMALL_PANEL, 10));
        Item item = ItemManager.getInstance().getItem(2);
        previewImage = new Gui(item.previewTexture.getValue());
        previewImage.set(new CenterConstraint(), new RelativeConstraint(0.85f), new PixelConstraint(32), new PixelConstraint(32));
        addChild(previewImage);
        nameText = new Text(item.name).setFont(Font.Title).setLineLength(new RelativeConstraint(0.75f));
        nameText.setColor(Color.RED);
        nameText.setPosition(new CenterConstraint(), new RelativeConstraint(0.75f));
        this.descriptionText = new Text(item.desc).setFont(Font.Title).setLineLength(new RelativeConstraint(0.8f));
        descriptionText.setPosition(new CenterConstraint(), new RelativeConstraint(0.675f));
        addChild(nameText, descriptionText);
        setVisible(false);
    }

    public void openInspectScreen(Item item) {
        previewImage.setTexture(item.previewTexture.getValue());
        nameText.setText(item.name);
        nameText.setColor(Color.RED);
        descriptionText.setText(item.desc);
    }

    public static InspectScreen getInstance() {
        if (instance == null) {
            synchronized (InspectScreen.class) {
                if (instance == null)
                    instance = new InspectScreen();
            }
        }
        return instance;
    }

}
