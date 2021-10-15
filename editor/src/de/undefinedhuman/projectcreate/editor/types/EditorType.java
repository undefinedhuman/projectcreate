package de.undefinedhuman.projectcreate.editor.types;

import de.undefinedhuman.projectcreate.editor.types.entity.EntityEditor;
import de.undefinedhuman.projectcreate.editor.types.item.ItemEditor;
import de.undefinedhuman.projectcreate.editor.types.world.WorldEditor;
import de.undefinedhuman.projectcreate.engine.log.Log;

public enum EditorType {
    ENTITY(EntityEditor.class),
    ITEM(ItemEditor.class),
    WORLD(WorldEditor.class);

    private final Class<? extends Editor> editorClass;

    EditorType(Class<? extends Editor> editorClass) {
        this.editorClass = editorClass;
    }

    public Editor newInstance() {
        Editor editor = null;
        try {
            editor = editorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Log.error("Can not create editor instance for type: " + this.name(), e);
        }
        return editor;
    }
}
