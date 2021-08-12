package de.undefinedhuman.projectcreate.core.network.utils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

import java.util.HashMap;

public class PacketUtils {

    public static String createComponentData(Iterable<Component> components) {
        StringBuilder builder = new StringBuilder();
        LineWriter writer = new LineWriter(true);
        components.forEach(component -> {
            if(!(component instanceof NetworkSerializable))
                return;
            writer.clear().writeString(component.getClass().getSimpleName());
            ((NetworkSerializable) component).send(writer);
            builder.append(writer.getData()).append("\n");
        });
        return builder.toString();
    }

    public static HashMap<String, LineSplitter> parseComponentData(String data) {
        HashMap<String, LineSplitter> parsedComponentData = new HashMap<>();
        LineSplitter splitter = new LineSplitter("", true);
        String[] lines = data.split("\n");
        for(String line : lines) {
            splitter.setData(line);
            String componentClass = splitter.getNextString();
            if(componentClass.equalsIgnoreCase(""))
                continue;
            parsedComponentData.put(componentClass, splitter.getDataAsLineSplitter());
        }
        return parsedComponentData;
    }

    public static void setComponentData(Entity entity, HashMap<String, LineSplitter> componentData) {
        for(Component component : entity.getComponents()) {
            if(!(component instanceof NetworkSerializable))
                continue;
            LineSplitter splitter = componentData.get(component.getClass().getSimpleName());
            if(splitter == null) continue;
            ((NetworkSerializable) component).receive(splitter);
        }
    }

}
