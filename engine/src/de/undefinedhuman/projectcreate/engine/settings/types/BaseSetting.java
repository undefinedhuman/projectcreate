package de.undefinedhuman.projectcreate.engine.settings.types;

import com.badlogic.gdx.files.FileHandle;
import de.undefinedhuman.projectcreate.engine.file.FileWriter;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.settings.Setting;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Parser;
import de.undefinedhuman.projectcreate.engine.settings.interfaces.Serializer;

public abstract class BaseSetting<T> extends Setting<T> {

    protected Serializer<T> serializer;
    protected Parser<T> parser;

    public BaseSetting(String key, T defaultValue, Parser<T> parser, Serializer<T> serializer) {
        super(key, defaultValue);
        this.parser = parser;
        this.serializer = serializer;
    }

    @Override
    protected void loadValue(FileHandle parentDir, Object value) {
        if(!(value instanceof LineSplitter))
            return;
        LineSplitter splitter = (LineSplitter) value;
        setValue(parser.parse(splitter.getNextString()));
    }

    @Override
    protected void saveValue(FileWriter writer) {
        writer.writeString(serializer.serialize(getValue()));
    }

}
