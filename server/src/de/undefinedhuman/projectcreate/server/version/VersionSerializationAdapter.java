package de.undefinedhuman.projectcreate.server.version;

import com.google.gson.*;
import de.undefinedhuman.projectcreate.engine.utils.version.Version;

import java.lang.reflect.Type;

public class VersionSerializationAdapter implements JsonDeserializer<Version>, JsonSerializer<Version> {
    @Override
    public Version deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Version.parse(json.getAsJsonPrimitive().getAsString());
    }

    @Override
    public JsonElement serialize(Version src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
