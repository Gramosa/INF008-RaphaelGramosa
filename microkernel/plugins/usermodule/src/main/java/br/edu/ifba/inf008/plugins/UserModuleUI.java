package br.edu.ifba.inf008.plugins;

import java.util.ArrayList;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UserModuleUI{
    private UserModule userModule;
    private IUIController uiController;

    public UserModuleUI(UserModule bookModule) {
        this.userModule = bookModule;
        uiController = ICore.getInstance().getUIController();
    }

    public void buildCreateUserTab(){
        // Criando os campos de entrada
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        // Botão para adicionar um livro
        Button addButton = new Button("Add user");
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            if(userModule.addUser(name)){
                uiController.showPopup(String.format("User \"%s\" added with success!", name));
            }
            else {
                uiController.showPopup("All fields must have content!");
            }
        });
        

        // Layout da aba
        VBox layout = new VBox(10, nameField, addButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("Create user", layout);
    }

    public void buildSearchUserTab(){
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        Button searchButton = new Button("Search User");
        searchButton.setOnAction(e -> {
            String name = nameField.getText();

            ArrayList<User> matchingUsers = userModule.getUsersByName(name, true);
            if (!matchingUsers.isEmpty()) {
                for(User book : matchingUsers){
                    uiController.createTabOnLeft("User Query", book.toString());
                }
                uiController.showPopup("Search match!");
            } else {
                uiController.showPopup("Not a single user found!");
            }
        });

        VBox layout = new VBox(10, nameField, searchButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("Search User", layout);
    }

    public void buildListUsersTab() {
        // Botão para listar os livros
        Button listButton = new Button("List Users");
        listButton.setOnAction(e -> {
            for (User user : userModule.getAllUsers()) {
                uiController.createTabOnLeft("Users Query", user.toString());
            }
            uiController.showPopup(String.format("%d users found!",userModule.getAllUsers().size()));
        });

        // Layout da aba
        VBox layout = new VBox(10, listButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("List Users", layout);
    }
}
