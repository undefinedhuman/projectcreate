package de.undefinedhuman.projectcreate.server.plugin;

import de.undefinedhuman.projectcreate.engine.utils.version.Version;

record PluginConfig(String name, String main, String author, Version version) {}
