package de.undefinedhuman.projectcreate.server.plugin;

import de.undefinedhuman.projectcreate.engine.file.FsFile;

public class PluginLoader {

    public static PluginConfig loadPluginConfig(FsFile pluginFile) {
        return null;
//        PluginConfig config = null;
//        JarFile jarFile;
//
//        try {
//            jarFile = new JarFile(pluginFile.file());
//        } catch (IOException ex) {
//            Log.error("Error while creating jar file from plugin file! Path: " + pluginFile.path(), ex);
//            return null;
//        }
//
//        try {
//            JarEntry pluginJson = jarFile.getJarEntry("plugin.json");
//        } catch (IllegalStateException ex) {
//
//        }
//
//        if(pluginJson == null)
//            Log.error("CAN NOT FIND plugin.json in plugin " + pluginDirectory.name() + "!");
//
//        InputStream stream = null;
//
//        try {
//            stream = jarFile.getInputStream(pluginJson);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(stream == null)
//            return;
//        try {
//            config = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), PluginConfig.class);
//        } catch (JsonSyntaxException ex) {
//            ex.printStackTrace();
//        }
//
//        if(config == null) return;
    }

}
