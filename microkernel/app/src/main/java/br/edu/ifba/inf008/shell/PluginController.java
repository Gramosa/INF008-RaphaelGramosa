package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.App;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IEventData;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPluginListener;
import br.edu.ifba.inf008.interfaces.IPluginSerialization;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

public class PluginController implements IPluginController
{
    private final ArrayList<IPluginSerialization> serializablePlugins = new ArrayList<>();
    private final HashMap<String, IPluginListener> listeners = new HashMap<>(); //event, listeners

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

                System.out.println(pluginName);

                if (plugin instanceof IPluginSerialization) {
                    IPluginSerialization serializablePlugin = (IPluginSerialization) plugin;
                    serializablePlugins.add(serializablePlugin);
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getClass().getName() + " - " + e.getMessage());

            return false;
        }
    }

    @Override
    public void subscribe(String event, IPluginListener plugin) {
        listeners.put(event, plugin);
    }

    @Override
    public <T, R> R emit(IEventData<T> data){
        String eventName = data.getEventName();

        IPluginListener listener = listeners.get(eventName);
        if(listener == null){
            return null;
        }

        return listener.onEvent(data);
    }

    public void loadAllPlugins(){
        for(IPluginSerialization plugin : serializablePlugins){
            plugin.loadData();
        }
    }

    public void saveAllPlugins(){
        for(IPluginSerialization plugin : serializablePlugins){
            plugin.saveData();
        }
    }
}
