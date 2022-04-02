package de.undefinedhuman.projectcreate.server.plugin;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.version.Version;
import de.undefinedhuman.projectcreate.server.version.VersionSerializationAdapter;
import marcono1234.gson.recordadapter.RecordTypeAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(RecordTypeAdapterFactory.DEFAULT)
            .registerTypeAdapter(Version.class, new VersionSerializationAdapter())
            .create();

    private static final String[] BLOCKED_PACKAGE_PATHS = new String[] {
            "de.undefinedhuman.projectcreate.server",
            "de.undefinedhuman.projectcreate.engine",
            "de.undefinedhuman.projectcreate.core"
    };

    public static PluginConfig loadPluginConfig(File pluginFile) {
        if(pluginFile == null) {
            Log.debug("Plugin file instance is null!");
            return null;
        }
        JarFile pluginJarFile;
        try {
            pluginJarFile = new JarFile(pluginFile);
        } catch (IOException e) {
            Log.debug("Error while parsing jar file of " + pluginFile.getName() + "as JarFile!");
            return null;
        }
        JarEntry pluginJson = pluginJarFile.getJarEntry("plugin.json");
        if(pluginJson == null) {
            Log.debug(pluginJarFile.getName() + " must contain plugin.json!");
            return null;
        }
        PluginConfig config;
        try {
            InputStream input = pluginJarFile.getInputStream(pluginJson);
            config = GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), PluginConfig.class);
        } catch (JsonSyntaxException | IOException ex) {
            Log.debug("Error while parsing plugin.json for plugin " + pluginJarFile.getName() + "!");
            return null;
        }
        return config;
    }

    public static Plugin loadPlugin(PluginConfig config, File pluginFile) {
        if(config == null || pluginFile == null) {
            Log.debug("Plugin or config file instance are null!");
            return null;
        }
        if(Arrays.stream(BLOCKED_PACKAGE_PATHS).anyMatch(path -> config.main().startsWith(path)))
            return null;
        Class<? extends Plugin> pluginClass;
        try {
            URL url = pluginFile.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{ url });
            pluginClass = classLoader.loadClass(config.main()).asSubclass(Plugin.class);
        } catch (ClassNotFoundException | MalformedURLException e) {
            Log.debug("Error while loading main class of plugin " + pluginFile.getName() + "!");
            return null;
        }
        Plugin plugin;
        try {
            plugin = ClassReflection.newInstance(pluginClass);
        } catch (ReflectionException e) {
            Log.debug("Error while creating instance for plugin " + pluginFile.getName() + "!");
            return null;
        }
        return plugin;
    }

}
