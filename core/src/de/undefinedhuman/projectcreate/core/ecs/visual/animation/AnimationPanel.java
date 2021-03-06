package de.undefinedhuman.projectcreate.core.ecs.visual.animation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.panels.AsepriteUtils;
import de.undefinedhuman.projectcreate.engine.settings.panels.BatchPanel;

public class AnimationPanel extends BatchPanel<Animation> {

    public AnimationPanel(String name) {
        super(name, Animation.class);
    }

    @Override
    public void loadBatch(FsFile dataFile) {
        JsonValue base = AsepriteUtils.JSON_READER.parse(dataFile);
        JsonValue frameData = base.get("frames");
        JsonValue metaData = base.get("meta");

        if(metaData == null || frameData == null)
            return;

        for (JsonValue frameTag : metaData.get("frameTags")) {
            String name = frameTag.getString("name");
            if (value.containsKey(name))
                continue;
            Animation animation = createNewInstance();
            animation.bounds.setValue(new Vector2(frameTag.getInt("from"), frameTag.getInt("to")));
            animation.frameTime.setValue(frameData.get(frameTag.getInt("from")).getInt("duration")/1000f);
            addPanelObject(name, animation);
        }
    }
}
