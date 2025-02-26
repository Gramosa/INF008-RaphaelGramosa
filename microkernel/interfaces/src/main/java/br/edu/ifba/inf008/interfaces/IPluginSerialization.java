package br.edu.ifba.inf008.interfaces;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.net.URLClassLoader;
import java.util.HashMap;

public interface IPluginSerialization {
    abstract void saveData();
    abstract void loadData(URLClassLoader ulc);

    default void save(HashMap<String, Serializable> data, String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
            
            oos.writeObject(data);
            
            oos.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default HashMap<String, Serializable> load(String filename, URLClassLoader ulc) { 
        HashMap<String, Serializable> data = new HashMap<>();
        
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            
            // Cria o ObjectInputStream que usa o ClassLoader fornecido (ulc) para desserialização
            ObjectInputStream ois = new ObjectInputStream(fileIn) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    // Usa o ClassLoader passado (ulc) para carregar as classes durante a desserialização
                    return Class.forName(desc.getName(), true, ulc);
                }
            };

            // Lê os dados da desserialização
            data = (HashMap<String, Serializable>) ois.readObject();
            
            ois.close();
            fileIn.close();
        }
        catch (FileNotFoundException e) {
            // Ignorar, pois no primeiro load o arquivo não vai existir
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }
}
