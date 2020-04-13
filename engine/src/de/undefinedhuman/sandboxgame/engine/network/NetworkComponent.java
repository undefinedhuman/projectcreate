package de.undefinedhuman.sandboxgame.engine.network;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

public interface NetworkComponent {
    void send(LineWriter writer);
    void receive(LineSplitter splitter);
}
