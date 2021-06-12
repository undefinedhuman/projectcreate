package de.undefinedhuman.projectcreate.editor.editor;

import de.undefinedhuman.projectcreate.editor.editor.entity.EntityEditor;
import de.undefinedhuman.projectcreate.editor.editor.item.ItemEditor;
import de.undefinedhuman.projectcreate.editor.editor.world.WorldEditor;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public enum EditorType {
    ENTITY(EntityEditor.class),
    ITEM(ItemEditor.class),
    WORLD(WorldEditor.class);

    private Class<? extends Editor> editorClass;

    EditorType(Class<? extends Editor> editorClass) {
        this.editorClass = editorClass;
    }

    public Editor newInstance(Container container, int width, int height) {
        Editor editor = null;
        try {
            editor = editorClass.getConstructor(Container.class, int.class, int.class).newInstance(container, width, height);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return editor;
    }
}
