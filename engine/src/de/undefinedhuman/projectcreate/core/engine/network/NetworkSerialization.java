package de.undefinedhuman.projectcreate.core.engine.network;

import de.undefinedhuman.projectcreate.core.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.core.engine.file.LineWriter;

public interface NetworkSerialization {
    void send(LineWriter writer);
    void receive(LineSplitter splitter);
}
