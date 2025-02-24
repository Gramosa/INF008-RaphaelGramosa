package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.IUIController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class UIController extends Application implements IUIController {
    // Atributos estáticos e privados
    private static UIController uiController;
    private MenuBar menuBar;
    private TabPane rightTabPane;
    private TabPane leftTabPane;
    private Stage popup;

    // Construtor
    public UIController() {}

    // Inicialização
    @Override
    public void init() {
        uiController = this;
    }

    public static UIController getInstance() {
        return uiController;
    }

    // Método principal da aplicação
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main Screen");

        // Inicializa componentes principais
        menuBar = buildMenuBar();
        rightTabPane = buildRightTabPane();
        leftTabPane = buildLeftTabPane();
        BorderPane mainBorderPane = buildMainBorderPane();

        // Organiza os componentes
        VBox mainLayout = new VBox(menuBar, mainBorderPane);
        VBox.setVgrow(mainBorderPane, Priority.ALWAYS);

        // Configura a cena e o stage
        Scene scene = new Scene(mainLayout, 960, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicializa o popup
        initializePopup(primaryStage);

        // Inicializa o plugin controller
        Core.getInstance().getPluginController().init();
    }

    // ---------------------- Construção da UI ----------------------

    private MenuBar buildMenuBar() {
        return new MenuBar();
    }

    private TabPane buildRightTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        return tabPane;
    }

    private TabPane buildLeftTabPane() {
        TabPane leftTabPane = new TabPane();
        leftTabPane.setSide(Side.TOP);
        //leftTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return leftTabPane;
    }

    private BorderPane buildMainBorderPane() {
        BorderPane mainBorderPane = new BorderPane();
        HBox panelsHBox = buildPanelsHBox();
        mainBorderPane.setCenter(panelsHBox);
        return mainBorderPane;
    }

    private HBox buildPanelsHBox() {
        HBox panelsHBox = new HBox();
        panelsHBox.setAlignment(Pos.CENTER);
        panelsHBox.setSpacing(10);

        // Painéis esquerdo e direito
        BorderPane leftPanel = buildLeftPanel();
        BorderPane rightPanel = buildRightPanel();

        // Proporção dos painéis
        leftPanel.prefWidthProperty().bind(panelsHBox.widthProperty().multiply(0.65));
        rightPanel.prefWidthProperty().bind(panelsHBox.widthProperty().multiply(0.35));

        panelsHBox.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(panelsHBox, Priority.ALWAYS);
        return panelsHBox;
    }

    private BorderPane buildLeftPanel() {
        BorderPane leftPanel = new BorderPane();
        leftPanel.setCenter(leftTabPane);
        return leftPanel;
    }

    private BorderPane buildRightPanel() {
        BorderPane rightPanel = new BorderPane();
        rightPanel.setTop(rightTabPane);
        //rightPanel.setCenter(new Label("Painel Direito"));
        return rightPanel;
    }

    // ---------------------- Manipulação de Abas ----------------------

    public boolean createTabOnRight(String tabText, Node contents) {
        Tab tab = new Tab(tabText, contents);
        rightTabPane.getTabs().add(tab);
        return true;
    }

    public boolean createTabOnLeft(String tabName, String resultText) {
        // Procura uma aba existente com o nome especificado
        Tab targetTab = null;
        for (Tab tab : leftTabPane.getTabs()) {
            if (tab.getText().equals(tabName)) {
                targetTab = tab;
                break;
            }
        }

        // Se a aba não existe, cria uma nova com um VBox rolável
        if (targetTab == null) {
            VBox contentBox = new VBox();
            contentBox.setSpacing(10);
            contentBox.setPadding(new Insets(10));

            ScrollPane scrollPane = new ScrollPane(contentBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            targetTab = new Tab(tabName, scrollPane);
            leftTabPane.getTabs().add(targetTab);
        }

        // Obtém o VBox que contém os itens
        ScrollPane sp = (ScrollPane) targetTab.getContent();
        VBox contentBox = (VBox) sp.getContent();

        // Verifica se o item adicionado é igual ao ultimo item, se for não adiciona
        // Evita que o usuario faça um spam no botão
        if (!contentBox.getChildren().isEmpty()){
            Label lastLabel = (Label) contentBox.getChildren().get(contentBox.getChildren().size() - 1);
            if(lastLabel.getText().equals(resultText)){
                return false;
            }
        }

        // Cria o novo Label com quebra de linha e estilo, e o adiciona ao VBox
        Label resultLabel = new Label(resultText);
        resultLabel.setWrapText(true);
        resultLabel.setStyle("-fx-background-color: lightgray; -fx-padding: 5; "
                            + "-fx-border-radius: 5; -fx-background-radius: 5;");
        contentBox.getChildren().add(resultLabel);

        return true;
    }
        

    // ---------------------- Manipulação de Menus ----------------------

    public MenuItem createMenuItem(String menuText, String menuItemText) {
        // Verifica se o menu já existe; se não, cria-o
        Menu targetMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText().equals(menuText)) {
                targetMenu = menu;
                break;
            }
        }
        if (targetMenu == null) {
            targetMenu = new Menu(menuText);
            menuBar.getMenus().add(targetMenu);
        }

        // Cria o MenuItem e o adiciona ao menu correspondente
        MenuItem menuItem = new MenuItem(menuItemText);
        targetMenu.getItems().add(menuItem);

        return menuItem;
    }

    // ---------------------- Popup ----------------------

    public void showPopup(String contentText) {
        Label label = (Label) popup.getScene().getRoot().lookup("#popupLabel");
        label.setText(contentText);

        popup.show();
        popup.sizeToScene();

        // Ajusta a posição corretamente
        Stage ownerStage = (Stage) popup.getOwner();
        double x = ownerStage.getX() + ownerStage.getWidth() - popup.getWidth() - 20;
        double y = ownerStage.getY() + ownerStage.getHeight() - popup.getHeight() - 50;

        popup.setX(x);
        popup.setY(y);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(3), e -> popup.hide())
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void initializePopup(Stage ownerStage) {
        popup = new Stage();
        popup.setTitle("Notificação");
        popup.initOwner(ownerStage);
        popup.initStyle(StageStyle.UNDECORATED);

        Label label = new Label();
        label.setId("popupLabel");
        label.setPadding(new Insets(10));
        label.setWrapText(true);
        label.setStyle("-fx-text-fill: white;");

        VBox layout = new VBox(label);
        layout.setStyle("-fx-background-color: black; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;");
        layout.setAlignment(Pos.CENTER);

        Scene popupScene = new Scene(layout);
        popup.setScene(popupScene);
    }
}
