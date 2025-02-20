package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.App;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPlugin;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class PluginController implements IPluginController
{
    private final HashMap<String, Set<Consumer<Object>>> listeners = new HashMap<>();

    public boolean init() {
        try {
            File currentDir = new File("./plugins");

            // Define a FilenameFilter to include only .jar files
            FilenameFilter jarFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jar");
                }
            };

            String []plugins = currentDir.list(jarFilter);
            int i;
            URL[] jars = new URL[plugins.length];
            for (i = 0; i < plugins.length; i++)
            {
                jars[i] = (new File("./plugins/" + plugins[i])).toURL();
            }
            URLClassLoader ulc = new URLClassLoader(jars, App.class.getClassLoader());
            for (i = 0; i < plugins.length; i++)
            {
                String pluginName = plugins[i].split("\\.")[0];
                IPlugin plugin = (IPlugin) Class.forName("br.edu.ifba.inf008.plugins." + pluginName, true, ulc).newInstance();
                plugin.init();
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getClass().getName() + " - " + e.getMessage());

            return false;
        }
    }

    public void subscribe(String event, Consumer<Object> listener){
        if(!listeners.containsKey(event)){
            listeners.put(event, new HashSet<>());
        }
        listeners.get(event).add(listener);
    }

    public void emit(String event, Object data){
        if(listeners.containsKey(event)){
            for(Consumer<Object> listener : listeners.get(event)){
                listener.accept(data);
            }
        }
    }
}
