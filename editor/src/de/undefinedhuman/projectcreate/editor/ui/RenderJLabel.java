package de.undefinedhuman.projectcreate.editor.ui;

import javax.swing.*;

@FunctionalInterface
public interface RenderJLabel<T> {
    void render(JLabel label, T t);
}
