package de.undefinedhuman.projectcreate.core.ecs.sprite;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import de.undefinedhuman.projectcreate.engine.settings.panels.BatchPanel;

import java.io.File;

public class SpritePanel extends BatchPanel<SpriteLayer> {

    public SpritePanel(String name) {
        super(name, SpriteLayer.class);
    }

    @Override
    public void loadBatch(File file) {
        FileHandle dataFile = new FileHandle(file);
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
            spriteLayer.texture.setTexture(new FileHandle(file).parent().path() + "/layers/" + name + ".png", Files.FileType.Absolute);
            spriteLayer.frameCount.setValue(frameData.size);
            spriteLayer.renderLevel.setValue(renderLevel++);
            addPanelObject(name, spriteLayer);
        }
    }

}
