package de.undefinedhuman.projectcreate.network.components;

import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;

public interface NetworkComponent {

    void send(LineWriter writer);
    void receive(LineSplitter splitter);

}
