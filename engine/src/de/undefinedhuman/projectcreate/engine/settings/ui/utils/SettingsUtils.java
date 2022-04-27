package de.undefinedhuman.projectcreate.engine.settings.ui.utils;

import de.undefinedhuman.projectcreate.engine.settings.ui.listener.DragAdapter;

import javax.swing.*;
import java.awt.*;

public class SettingsUtils {

    public static JComponent setLocation(int x, int y, JComponent component) {
        component.setLocation(x, y);
        return component;
    }

    public static JComponent setPreferredSize(int width, int height, JComponent component) {
        component.setPreferredSize(new Dimension(width, height));
        return component;
    }

    public static JComponent setDragListener(DragAdapter dragAdapter, JComponent component) {
        component.addMouseListener(dragAdapter);
        component.addMouseMotionListener(dragAdapter);
        component.setAutoscrolls(true);
        return component;
    }

    public static JComponent setSize(int width, int height, JComponent component) {
        component.setSize(width, height);
        return component;
    }

    public static JComponent setBounds(int x, int y, int width, int height, JComponent component) {
        component.setBounds(x, y, width, height);
        return component;
    }

    public static void repaint(JComponent... components) {
        for(JComponent component : components) {
            if(component == null)
                continue;
            component.revalidate();
            component.repaint();
        }
    }

    public static void setCustomUIComponentProperties() {
        UIManager.put("Button.arc", 0);
        UIManager.put("Component.arc", 0);
        UIManager.put("CheckBox.arc", 0);
        UIManager.put("ProgressBar.arc", 0);

        UIManager.put("Component.arrowType", "chevron");
        UIManager.put("Component.focusWidth", 1);
    }

}
