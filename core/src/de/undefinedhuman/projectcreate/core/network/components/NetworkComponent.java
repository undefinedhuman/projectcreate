package de.undefinedhuman.projectcreate.core.network.components;

import de.undefinedhuman.projectcreate.core.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.core.engine.file.LineWriter;

public interface NetworkComponent {

    void send(LineWriter writer);
    void receive(LineSplitter splitter);

}
