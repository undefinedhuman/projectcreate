package de.undefinedhuman.projectcreate.editor.editor;

import de.undefinedhuman.projectcreate.editor.editor.entity.EntityEditor;
import de.undefinedhuman.projectcreate.editor.editor.item.ItemEditor;

public enum EditorType {
    ENTITY(EntityEditor.class),
    ITEM(ItemEditor.class);
    // WORLD(WorldEditor.class);

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
