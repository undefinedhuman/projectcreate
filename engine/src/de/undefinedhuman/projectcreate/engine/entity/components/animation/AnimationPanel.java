package de.undefinedhuman.projectcreate.engine.entity.components.animation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import de.undefinedhuman.projectcreate.engine.settings.panels.BatchPanel;

import java.io.File;

public class AnimationPanel extends BatchPanel<Animation> {

    public AnimationPanel(String name, Animation panelObject) {
        super(name, panelObject);
    }

    @Override
    public void loadBatch(File file) {
        FileHandle dataFile = new FileHandle(file);
        JsonValue base = reader.parse(dataFile);
        JsonValue frameData = base.get("frames");
        JsonValue metaData = base.get("meta");

        if(metaData == null || frameData == null)
            return;

        for (JsonValue frameTag : metaData.get("frameTags")) {
            String name = frameTag.getString("name");
            if (panelObjects.containsKey(name))
                continue;
            Animation animation = createNewInstance();
            animation.bounds.setValue(new Vector2(frameTag.getInt("from"), frameTag.getInt("to")));
            animation.frameTime.setValue(frameData.get(frameTag.getInt("from")).getInt("duration")/1000f);
            addPanelObject(name, animation);
        }
    }
}
