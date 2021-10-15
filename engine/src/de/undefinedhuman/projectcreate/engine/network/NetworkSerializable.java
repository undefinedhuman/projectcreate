package de.undefinedhuman.projectcreate.engine.network;

import de.undefinedhuman.projectcreate.engine.file.LineSplitter;
import de.undefinedhuman.projectcreate.engine.file.LineWriter;

public interface NetworkSerializable {
    void serialize(LineWriter writer);
    void parse(LineSplitter splitter);
}
