package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.App;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IEventData;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPluginListener;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PluginController implements IPluginController
{
    private final HashMap<String, Set<IPluginListener>> listeners = new HashMap<>();

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

    public void subscribe(String event, IPluginListener plugin) {
        if(!listeners.containsKey(event)){
            listeners.put(event, new HashSet<>());
        }
        listeners.get(event).add(plugin);
    }

    public boolean emit(String event, IEventData data){
        Set<IPluginListener> eventListeners = listeners.get(event);
        if(eventListeners == null || eventListeners.isEmpty()){
            return false;
        }
        
        for(IPluginListener pluginCommunicator : eventListeners){
            pluginCommunicator.onEvent(data);
        }
        return true;
    }
}
