package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;

import javafx.scene.control.MenuItem;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class BookLibrary implements IPlugin
{
    public boolean init() {
        ICore core = ICore.getInstance();
        IUIController uiController = core.getUIController();

        MenuItem menuItem = uiController.createMenuItem("Menu 999", "Meu item de menu de livro");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("I've been clicked!");
            }
        });

        uiController.createTab("Aaaaaaaaa", new Rectangle(200,200, Color.LIGHTSTEELBLUE));

        return true;
    }
}
