package de.undefinedhuman.projectcreate.core.ecs.sprite;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.utils.JsonValue;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.settings.panels.BatchPanel;

public class SpritePanel extends BatchPanel<SpriteLayer> {

    public SpritePanel(String name) {
        super(name, SpriteLayer.class);
    }

    @Override
    public void loadBatch(FsFile dataFile) {
        JsonValue base = reader.parse(dataFile);
        JsonValue frameData = base.get("frames");
        JsonValue metaData = base.get("meta");

        if(metaData == null || frameData == null)
            return;

        int renderLevel = 0;
        for (JsonValue layer : metaData.get("layers")) {
            String name = layer.getString("name");
            if (value.containsKey(name) || (layer.has("data") && layer.getString("data").equalsIgnoreCase("IGNORE")))
                continue;
            SpriteLayer spriteLayer = createNewInstance();
            spriteLayer.texture.setTexture(dataFile.parent().path() + "/layers/" + name + ".png", Files.FileType.Absolute);
            spriteLayer.frameCount.setValue(frameData.size);
            spriteLayer.renderLevel.setValue(renderLevel++);
            addPanelObject(name, spriteLayer);
        }
    }

}
