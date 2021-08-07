package de.undefinedhuman.projectcreate.engine.network;

import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;

public interface NetworkSerializable {
    void send(LineWriter writer);
    void receive(LineSplitter splitter);
}
