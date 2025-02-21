package br.edu.ifba.inf008.plugins;

import java.util.ArrayList;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoanModuleUI {
    private LoanModule bookModule;
    private IUIController uiController;

    public LoanModuleUI(LoanModule bookModule) {
        this.bookModule = bookModule;
        uiController = ICore.getInstance().getUIController();
    }

}
