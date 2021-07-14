package de.undefinedhuman.projectcreate.engine.settings.ui.listener;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.function.Supplier;

public class ResizeListener implements ComponentListener {

    private Supplier<String> componentText;
    private int heightPadding, fontSizeAddition;

    public ResizeListener(Supplier<String> componentText) {
        this(0, 0, componentText);
    }

    public ResizeListener(int heightPadding, int fontSizeAddition, Supplier<String> componentText) {
        this.componentText = componentText;
        this.heightPadding = heightPadding;
        this.fontSizeAddition = fontSizeAddition;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Component component = e.getComponent();
        String text = componentText.get();
        if(text.equalsIgnoreCase(""))
            return;
        int stringWidth = component.getFontMetrics(component.getFont()).stringWidth(componentText.get());
        double widthRatio = (double) component.getWidth() / (double) stringWidth;
        int newFontSize = (int) (component.getFont().getSize() * widthRatio) + fontSizeAddition;
        e.getComponent().setFont(component.getFont().deriveFont((float) Math.min(newFontSize, component.getHeight() - heightPadding)));
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}
