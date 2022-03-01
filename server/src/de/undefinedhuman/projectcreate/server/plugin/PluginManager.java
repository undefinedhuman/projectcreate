package de.undefinedhuman.projectcreate.server.plugin;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import de.undefinedhuman.projectcreate.engine.file.FsFile;
import de.undefinedhuman.projectcreate.engine.file.Paths;
import de.undefinedhuman.projectcreate.engine.log.Log;
import de.undefinedhuman.projectcreate.engine.utils.manager.Manager;
import de.undefinedhuman.projectcreate.engine.utils.version.Version;
import de.undefinedhuman.projectcreate.server.utils.commands.Command;
import de.undefinedhuman.projectcreate.server.utils.commands.CommandManager;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager implements Manager {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(RecordTypeAdapterFactory.DEFAULT)
            .registerTypeAdapter(Version.class, new VersionSerializationAdapter())
            .create();

    private ArrayList<Plugin> plugins = new ArrayList<>();
    private List<Plugin> unmodifiablePlugins = Collections.unmodifiableList(plugins);

    @Override
    public void init() {
    }

    @Override
    public void delete() {
    }

    public void registerCommand(String name, Command command) {
        CommandManager.getInstance().addCommand(name, command);
    }

    public static void loadPlugin(FsFile pluginsDirectory) {

        if(!pluginsDirectory.exists() || !pluginsDirectory.isDirectory())
            new FsFile(Paths.getInstance().getDirectory(), Paths.PLUGIN_PATH).mkdirs();

        for(FileHandle pluginDirectory : pluginsDirectory.list((dir, name) -> name.trim().length() > 4 && name.endsWith(".jar"))) {

            PluginConfig config = null;

            JarFile jarFile = null;

            try {
                jarFile = new JarFile(pluginDirectory.file());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(jarFile == null)
                continue;
            JarEntry pluginJson = jarFile.getJarEntry("plugin.json");
            if(pluginJson == null)
                Log.error("CAN NOT FIND plugin.json in plugin " + pluginDirectory.name() + "!");

            InputStream stream = null;

            try {
                stream = jarFile.getInputStream(pluginJson);
            } catch (NullPointerException | IOException ex) {
                Log.error("Cannot find plugin json!!", ex);
            }
            if(stream == null)
                 return;
             try {
                 config = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), PluginConfig.class);
             } catch (JsonSyntaxException ex) {
                 ex.printStackTrace();
             }

            if(config == null) return;

            Log.info(config.name());
            Log.info(config.author());
            Log.info(config.main());
            Log.info(config.version());

            File file = pluginDirectory.file();
            URL url = null;
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // IMPLEMENT SO PLUGIN DOES NOT START WITH SOME SERVER CLASS
            if(config.main().startsWith("de.undefinedhuman.projectcreate.server")
                    || config.main().startsWith("de.undefinedhuman.projectcreate.engine")
                    || config.main().startsWith("de.undefinedhuman.projectcreate.core"))
                return;

            URLClassLoader classLoader = new URLClassLoader(new URL[]{ url });
            Class<? extends Plugin> pluginClass = null;
            try {
                pluginClass = classLoader.loadClass(config.main()).asSubclass(Plugin.class);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(pluginClass == null) return;
            Plugin plugin = null;
            try {
                plugin = ClassReflection.newInstance(pluginClass);
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
            if(plugin == null) return;
            plugin.init();
            plugin.load();
            plugin.delete();

        }
    }

}
