package de.undefinedhuman.projectcreate.core.utils;

import de.undefinedhuman.projectcreate.core.items.Item;
import de.undefinedhuman.projectcreate.core.items.ItemManager;
import de.undefinedhuman.projectcreate.engine.resources.RessourceUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemLabelUtils {

    public static void renderItemInJLabel(JLabel label, Integer itemID) {
        if(!ItemManager.getInstance().hasItem(itemID) && !RessourceUtils.existItem(itemID)) {
            label.setText("ERROR! ITEM " + itemID + " DOES NOT EXIST!");
            return;
        }
        Item item = ItemManager.getInstance().getItem(itemID);
        label.setText(itemID + " " + item.name.getValue());
        BufferedImage texture = item.iconTexture.getBufferedImage();
        if(texture != null)
            label.setIcon(new ImageIcon(texture.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
    }

}
