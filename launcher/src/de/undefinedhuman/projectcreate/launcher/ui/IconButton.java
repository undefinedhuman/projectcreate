package de.undefinedhuman.projectcreate.launcher.ui;

import de.undefinedhuman.projectcreate.engine.utils.math.Vector2i;
import de.undefinedhuman.projectcreate.launcher.icon.IconManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class IconButton extends JButton implements MouseListener {

    private static final Vector2i SIZE = new Vector2i(32, 32);
    private static final Vector2i SIZE_HOVER = new Vector2i(36, 36);
    private static final Vector2i POSITION_OFFSET = new Vector2i().set(SIZE_HOVER).sub(SIZE).div(2);

    private String iconName;
    private Vector2i position = new Vector2i();

    public IconButton(String iconName, int x, int y, ActionListener listener) {
        this.iconName = iconName;
        this.position.set(x, y);
        setIcon(IconManager.getInstance().getIcon(iconName, SIZE.x, SIZE.y));
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setBorderPainted(false);
        setBounds(x, y, SIZE.x, SIZE.y);
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
        setBounds(position.x - POSITION_OFFSET.x, position.y - POSITION_OFFSET.y, SIZE_HOVER.x, SIZE_HOVER.y);
        updateIcon();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(!isEnabled())
            return;
        setBounds(position.x, position.y, SIZE.x, SIZE.y);
        updateIcon();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setBounds(position.x, position.y, SIZE.x, SIZE.y);
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
