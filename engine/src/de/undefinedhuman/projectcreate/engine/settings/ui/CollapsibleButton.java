package de.undefinedhuman.projectcreate.engine.settings.ui;

import de.undefinedhuman.projectcreate.engine.settings.ui.event.BooleanChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class CollapsibleButton extends JLabel {

    private boolean visible = true;

    private ArrayList<BooleanChangeListener> listeners = new ArrayList<>();

    public CollapsibleButton(Color background) {
        super("▼");
        setOpaque(true);
        setBackground(background);
        setHorizontalAlignment(SwingConstants.CENTER);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                visible = !visible;
                setText(visible ? "▼" : "►");
                revalidate();
                repaint();
                for(BooleanChangeListener changeListener : listeners)
                    changeListener.notify(visible);
            }
        });
    }

    public CollapsibleButton(Color background, BooleanChangeListener... listener) {
        this(background);
        addChangeListener(listener);
    }

    public CollapsibleButton addChangeListener(BooleanChangeListener... changeListeners) {
        listeners.addAll(Arrays.asList(changeListeners));
        return this;
    }

    public void delete() {
        listeners.clear();
    }

}
