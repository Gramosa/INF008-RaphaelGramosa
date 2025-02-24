package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
        // Basicamente aceita apeans input que de match com regex \d, ou seja numeros
        userIdField.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*") ? change : null;
        }));
        userIdField.setPromptText("User ID");
        
        TextField bookIsbnField = new TextField();
        bookIsbnField.setPromptText("Book ISBN");
        
        // Botão para registrar empréstimo
        Button borrowButton = new Button("Borrow Book");
        borrowButton.setOnAction(e -> {
            String userId = userIdField.getText();
            String bookIsbn = bookIsbnField.getText();
            if(userId.isBlank() || bookIsbn.isBlank()){
                uiController.showPopup("All fields must be given!");
            }
            else if (loanModule.addLoan(Integer.parseInt(userId), bookIsbn)) {
                uiController.showPopup("Loan registered successfully!");
            }
        });
        
        // Layout da aba
        VBox layout = new VBox(10, userIdField, bookIsbnField, borrowButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        
        // Criar a aba no UIController
        uiController.createTabOnRight("Borrow Book", layout);
    }

    public void buildReturnBookTab(){
        TextField bookIsbnField = new TextField();
        bookIsbnField.setPromptText("Enter Book ISBN");

        Button returnButton = new Button("Return Book");
        returnButton.setOnAction(e -> {
            String bookIsbn = bookIsbnField.getText();

            if (loanModule.finishLoan(bookIsbn)) {
                uiController.showPopup("Book returned successfully!");
            }
        });

        // Layout da aba
        VBox layout = new VBox(10, bookIsbnField, returnButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("Return Book", layout);
    }

    public void buildListLoansTab(){

    }

    public void buildUserLoansTransaction(){
        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter User ID");
        userIdField.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*") ? change : null;
        }));
        
        CheckBox activeLoansCheck = new CheckBox("Active Loans");
        CheckBox archivedLoansCheck = new CheckBox("Archived Loans");
        
        // Botão para gerar relatório
        Button generateReportButton = new Button("Show Transactions");
        generateReportButton.setOnAction(e -> {
            Integer userId = Integer.parseInt(userIdField.getText());
            boolean active = activeLoansCheck.isSelected();
            boolean notActive = archivedLoansCheck.isSelected();
            
            if (!active && !notActive) {
                uiController.showPopup("Select at least one box!");
                return;
            }
            
            String report = loanModule.getUserLoansDetails(userId, active, notActive);
            uiController.createTabOnLeft("User Transactions", report);

        });
        
        // Layout da aba
        VBox layout = new VBox(10, userIdField, activeLoansCheck, archivedLoansCheck, generateReportButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        
        // Criar a aba no UIController
        uiController.createTabOnRight("User Report", layout);
    }
}
