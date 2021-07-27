package de.undefinedhuman.projectcreate.editor.types;

import de.undefinedhuman.projectcreate.editor.types.entity.EntityEditor;
import de.undefinedhuman.projectcreate.editor.types.item.ItemEditor;
import de.undefinedhuman.projectcreate.editor.types.world.WorldEditor;

public enum EditorType {
    ENTITY(EntityEditor.class),
    ITEM(ItemEditor.class),
    WORLD(WorldEditor.class);

    private Class<? extends Editor> editorClass;

    EditorType(Class<? extends Editor> editorClass) {
        this.editorClass = editorClass;
    }

    public Editor newInstance() {
        Editor editor = null;
        try {
            editor = editorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return editor;
    }
}
