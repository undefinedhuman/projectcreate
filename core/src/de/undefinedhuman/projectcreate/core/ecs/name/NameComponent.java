package de.undefinedhuman.projectcreate.core.ecs.name;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;
import de.undefinedhuman.projectcreate.engine.gui.text.Text;
import de.undefinedhuman.projectcreate.engine.network.NetworkSerializable;

public class NameComponent implements Component, NetworkSerializable {

    private String name;
    private Text text;

    public NameComponent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if(text != null) this.text.setText(name);
    }

    public Text getText() {
        if(text != null)
            return text;
        text = new Text(name);
        text.setColor(Color.DARK_GRAY);
        return text;
    }

    @Override
    public void send(LineWriter writer) {
        writer.writeString(name);
    }

    @Override
    public void receive(LineSplitter splitter) {
        this.name = splitter.getNextString();
    }
}
