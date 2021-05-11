package de.undefinedhuman.projectcreate.launcher.ui;

import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.launcher.icon.IconManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class IconButton extends JButton implements MouseListener {

    private static final float HOVER_SCALE = 0.125f;

    private String iconName;
    private Vector2i position = new Vector2i();

    private Vector2i iconSize = new Vector2i();

    public IconButton(String iconName, int x, int y, Vector2i iconSize, ActionListener listener) {
        this.iconName = iconName;
        this.position.set(x, y);
        this.iconSize.set(iconSize);
        setIcon(IconManager.getInstance().getIcon(iconName, iconSize.x, iconSize.y));
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setBorderPainted(false);
        setBounds(x, y, iconSize.x, iconSize.y);
        addMouseListener(this);
        addActionListener(listener);
    }

    public void setIcon(String iconName) {
        this.iconName = iconName;
        updateIcon();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(!isEnabled())
            return;
        setBounds((int) (position.x - iconSize.x*HOVER_SCALE/2), (int) (position.y - iconSize.y*HOVER_SCALE/2), (int) (iconSize.x * (1f+HOVER_SCALE)), (int) (iconSize.y * (1f+HOVER_SCALE)));
        updateIcon();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(!isEnabled())
            return;
        setBounds(position.x, position.y, iconSize.x, iconSize.y);
        updateIcon();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setBounds(position.x, position.y, iconSize.x, iconSize.y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    private void updateIcon() {
        setIcon(IconManager.getInstance().getIcon(iconName, getWidth(), getHeight()));
    }

}
