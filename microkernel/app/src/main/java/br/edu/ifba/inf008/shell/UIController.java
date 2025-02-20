package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.interfaces.ICore;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.geometry.Side;
import javafx.scene.Node;

public class UIController extends Application implements IUIController
{
    private ICore core;
    private MenuBar menuBar;
    private TabPane tabPane;
    private Stage popup;

    private static UIController uiController;

    public UIController() {
    }

    @Override
    public void init() {
        uiController = this;
    }

    public static UIController getInstance() {
        return uiController;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");

        menuBar = new MenuBar();

        VBox vBox = new VBox(menuBar);

        tabPane = new TabPane();
        tabPane.setSide(Side.TOP);

        vBox.getChildren().addAll(tabPane);

        Scene scene = new Scene(vBox, 960, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

        initializePopup(primaryStage);

        Core.getInstance().getPluginController().init();
    }

    public MenuItem createMenuItem(String menuText, String menuItemText) {
        // Criar o menu caso ele nao exista
        Menu newMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText() == menuText) {
                newMenu = menu;
                break;
            }
        }
        if (newMenu == null) {
            newMenu = new Menu(menuText);
            menuBar.getMenus().add(newMenu);
        }

        // Criar o menu item neste menu
        MenuItem menuItem = new MenuItem(menuItemText);
        newMenu.getItems().add(menuItem);

        return menuItem;
    }

    public boolean createTab(String tabText, Node contents) {
        Tab tab = new Tab();
        tab.setText(tabText);
        tab.setContent(contents);
        tabPane.getTabs().add(tab);

        return true;
    }

    private void initializePopup(Stage ownerStage) {
        popup = new Stage();
        popup.setTitle("Popup Temporário");

        Label label = new Label("Esta mensagem vai desaparecer em 3 segundos!");
        Scene popupScene = new Scene(label, 300, 100);
        popup.setScene(popupScene);

        popup.setX(ownerStage.getX() + ownerStage.getWidth() - 300); // Lado direito
        popup.setY(ownerStage.getY() + 50); // Distância do topo (ajuste conforme necessário)
    }

    public void showPopup(String contentText) {
        Label label = (Label) popup.getScene().getRoot();
        label.setText(contentText);

        popup.show();

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(3), e -> popup.hide())  // Esconde o popup após 3 segundos
        );

        timeline.setCycleCount(1);  // Apenas uma execução
        timeline.play();  // Inicia o Timeline
    }
}
