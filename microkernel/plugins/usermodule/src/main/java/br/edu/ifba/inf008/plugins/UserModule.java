package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPluginListener;
import br.edu.ifba.inf008.interfaces.IPluginSerialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IEventData;
import br.edu.ifba.inf008.interfaces.IUIController;

import javafx.scene.control.MenuItem;

public class UserModule implements IPlugin, IPluginListener, IPluginSerialization {
    private static final String fileName = "UserModule.dat";
    private Integer userCount = 0;

    private HashMap<Integer, User> users = new HashMap<>(); // id, user
    private UserModuleUI userModuleUI;

    private IPluginController pluginController;

    public boolean init() {
        userModuleUI = new UserModuleUI(this);
        
        ICore core = ICore.getInstance();
        IUIController uiController = core.getUIController();
        pluginController = core.getPluginController();

        String menuText = "User Module";
        MenuItem newUserMenuItem = uiController.createMenuItem(menuText, "Add new user");
        newUserMenuItem.setOnAction(e -> userModuleUI.buildCreateUserTab());

        MenuItem searchUserMenuItem = uiController.createMenuItem(menuText, "Search an user");
        searchUserMenuItem.setOnAction(e -> userModuleUI.buildSearchUserTab());

        MenuItem listUserMenuItem = uiController.createMenuItem(menuText, "List all users");
        listUserMenuItem.setOnAction(e -> userModuleUI.buildListUsersTab());

        pluginController.subscribe("get_user", this);
        pluginController.subscribe("get_user_string", this);
        return true;
    }

    @Override
    public void saveData(){
        HashMap<String, Serializable> pluginData = new HashMap<>();

        pluginData.put("userCount", userCount);
        pluginData.put("users", users);

        save(pluginData, fileName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadData() {
        HashMap<String, Serializable> pluginData = load(fileName);
        if(pluginData.isEmpty()){
            return;
        }
        userCount = (Integer) pluginData.get("userCount");
        users = (HashMap<Integer, User>) pluginData.get("users");
    }

    public boolean addUser(String name){
        if(name == null || name.equals("")){
            return false;
        }
        userCount++;
        users.put(userCount, new User(userCount, name));

        return true;
    }

    public User getUser(int id){
        return users.get(id);
    }

    public ArrayList<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    public ArrayList<User> getUsersByName(String name, boolean anyMatch) {
        ArrayList<User> matchingUsers = new ArrayList<>();
        String searchName = name.toLowerCase();

        for (User user : users.values()) {
            String userName = user.getName().toLowerCase();
    
            if (anyMatch) {
                if (userName.contains(searchName)) {
                    matchingUsers.add(user);
                }
            } else {
                if (userName.equals(searchName)) {
                    matchingUsers.add(user);
                }
            }
        }
    
        return matchingUsers; // Retorna a lista com os usuários encontrados
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, R> R onEvent(IEventData<T> event){
        String eventName = event.getEventName();
        if(eventName.equals("get_user")){
            Integer userId = (Integer) event.getData();
            return (R) getUserResponse(userId);
        }
        else if(eventName.equals("get_user_string")){
            Integer userId = (Integer) event.getData();
            return (R) getUserResponseString(userId);
        }
        else{
            return null;
        }
    }

    private ArrayList<String> getUserResponse(Integer userId){
        User user = getUser(userId);
        if(user == null){
            return new ArrayList<>();
        }

        return user.toArrayList();
    }

    private String getUserResponseString(Integer userId){
        User user = getUser(userId);
        if(user == null){
            return "";
        }

        return user.toString();
    }
}
