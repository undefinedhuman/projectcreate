package de.undefinedhuman.projectcreate.engine.settings.ui.listener;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Supplier;

public class ResizeListener extends ComponentAdapter {

    private Supplier<Component[]> components;
    private Supplier<String> componentText;
    private int padding;

    public ResizeListener(Supplier<Component[]> components, Supplier<String> componentText) {
        this(components, 0, componentText);
    }

    public ResizeListener(Supplier<Component[]> components, int padding, Supplier<String> componentText) {
        this.components = components;
        this.padding = padding;
        this.componentText = componentText;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Component component = e.getComponent();
        for(Component children : components.get()) {
            String text = componentText.get();
            if(text.equalsIgnoreCase(""))
                return;
            int stringWidth = component.getFontMetrics(component.getFont()).stringWidth(text);
            double widthRatio = (double) component.getWidth() / (double) stringWidth;
            float newFontSize = (int) (component.getFont().getSize() * widthRatio);
            children.setFont(component.getFont().deriveFont(Math.min(newFontSize, component.getHeight() - padding)));
        }
    }

}
