package de.undefinedhuman.projectcreate.core.network.cache;

import java.util.ArrayList;

public class NetworkCache {

    private ArrayList<NetworkCommand> commands;

    public NetworkCache() {
        commands = new ArrayList<>();
    }

    public synchronized void addCommand(NetworkCommand command) {
        this.commands.add(command);
    }

    public synchronized void process() {
        commands.forEach(NetworkCommand::process);
        clear();
    }

    private void clear() {
        this.commands.clear();
    }

}
