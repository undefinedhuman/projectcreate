package de.undefinedhuman.sandboxgame.network.components;

import de.undefinedhuman.sandboxgame.engine.file.LineSplitter;
import de.undefinedhuman.sandboxgame.engine.file.LineWriter;

public interface NetworkComponent {

    void send(LineWriter writer);

    void receive(LineSplitter splitter);

}
