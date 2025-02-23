package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoanModuleUI {
    private LoanModule loanModule;
    private IUIController uiController;

    public LoanModuleUI(LoanModule loanModule) {
        this.loanModule = loanModule;
        uiController = ICore.getInstance().getUIController();
    }

    public void buildBorrowBookTab() {
        // Campos de entrada
        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");
        
        TextField bookIsbnField = new TextField();
        bookIsbnField.setPromptText("Book ISBN");
        
        // Botão para registrar empréstimo
        Button borrowButton = new Button("Borrow Book");
        borrowButton.setOnAction(e -> {
            try {
                Integer userId = Integer.parseInt(userIdField.getText());
                String bookIsbn = bookIsbnField.getText();
                
                if (loanModule.addLoan(userId, bookIsbn)) {
                    uiController.showPopup("Loan registered successfully!");
                }
            } 
            catch (NumberFormatException ex) {
                uiController.showPopup("Invalid User ID.");
            }
        });
        
        // Layout da aba
        VBox layout = new VBox(10, userIdField, bookIsbnField, borrowButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        
        // Criar a aba no UIController
        uiController.createTabOnRight("Borrow Book", layout);
    }
}
