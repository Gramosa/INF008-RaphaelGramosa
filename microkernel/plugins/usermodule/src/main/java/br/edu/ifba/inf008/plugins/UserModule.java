package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.events.UserRequestEvent;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPluginListener;

import java.util.ArrayList;
import java.util.HashMap;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IEventData;
import br.edu.ifba.inf008.interfaces.IUIController;

import javafx.scene.control.MenuItem;

public class UserModule implements IPlugin, IPluginListener {
    private Integer userCount = 0;

    private HashMap<Integer, User> users = new HashMap<>(); // id, user
    private UserModuleUI userModuleUI;

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
    
        for (User user : users.values()) { // Percorre todos os usuários no HashMap
            String userName = user.getName().toLowerCase(); // Nome do usuário em minúsculas
            String searchName = name.toLowerCase(); // Texto da pesquisa em minúsculas
    
            if (anyMatch) {
                // Se o nome contém a string de busca
                if (userName.contains(searchName)) {
                    matchingUsers.add(user);
                }
            } else {
                // Se o nome for exatamente igual à string de busca
                if (userName.equals(searchName)) {
                    matchingUsers.add(user);
                }
            }
        }
    
        return matchingUsers; // Retorna a lista com os usuários encontrados
    }

    public boolean init() {
        userModuleUI = new UserModuleUI(this);
        
        ICore core = ICore.getInstance();
        IUIController uiController = core.getUIController();
        IPluginController pluginController = core.getPluginController();

        String menuText = "User Module";
        MenuItem newUserMenuItem = uiController.createMenuItem(menuText, "Createn a new user");
        newUserMenuItem.setOnAction(e -> userModuleUI.buildCreateUserTab());

        MenuItem searchUserMenuItem = uiController.createMenuItem(menuText, "Search an user");
        searchUserMenuItem.setOnAction(e -> userModuleUI.buildSearchUserTab());

        MenuItem listUserMenuItem = uiController.createMenuItem(menuText, "List all users");
        listUserMenuItem.setOnAction(e -> userModuleUI.buildListUsersTab());

        pluginController.subscribe("request_user", this);

        return true;
    }

    public void onEvent(IEventData event){
        String eventName = event.getEventName();
        if(eventName.equals("request_user")){
            UserRequestEvent userRequestEvent = (UserRequestEvent) event;
            User user = getUser(userRequestEvent.getUserId());
            if(user == null){
                // Continuar
            }
        }
    }
}
