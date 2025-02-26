package br.edu.ifba.inf008.interfaces;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public interface IPluginSerialization {
    abstract void saveData();
    abstract void loadData();

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

    default HashMap<String, Serializable> load(String filename) {
        HashMap<String, Serializable> data = new HashMap<>();
        
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            
            data = (HashMap<String, Serializable>) ois.readObject();
            
            ois.close();
            fileIn.close();
        }
        catch (FileNotFoundException e) {
            //ignorar, pq no primeiro load de fato não vai existir
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }
}
