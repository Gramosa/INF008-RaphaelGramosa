package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.IPluginListener;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IEventData;
import br.edu.ifba.inf008.interfaces.IUIController;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.MenuItem;

public class LoanModule implements IPlugin, IPluginListener {
    private ArrayList<Loan> loans = new ArrayList<>(); //isbn, 
    private LoanModuleUI loanModuleUI;

    public boolean init() {
        loanModuleUI = new LoanModuleUI(this);

        ICore core = ICore.getInstance();
        IUIController uiController = core.getUIController();
        IPluginController pluginController = core.getPluginController();

        // Criar item de menu para abrir a aba "Loan Library"
        String menuText = "Loan Module";
        // MenuItem borrowBookMenuItem = uiController.createMenuItem(menuText, "Loan a book");
        // borrowBookMenuItem.setOnAction(e -> loanModuleUI.buildCreateLoanTab());

        // MenuItem searchLoanMenuItem = uiController.createMenuItem(menuText, "Search for a loan");
        // searchLoanMenuItem.setOnAction(e -> loanModuleUI.buildSearchLoanTab());

        // MenuItem listLoansMenuItem = uiController.createMenuItem(menuText, "List all loans");
        // listLoansMenuItem.setOnAction(e -> loanModuleUI.buildListLoansTab());

        pluginController.subscribe("request_user", this);

        return true;
    }

    public void onEvent(IEventData event){
        if(event.getEventName() == "request_user"){
            // LoanBorrowEvent loanBorrowEvent = (LoanBorrowEvent) event;
        }
    }

    // public boolean addLoan(Loan loan) {
    //     if (loans.containsKey(loan.getIsbn())) {
    //         return false;
    //     }
    //     loans.put(loan.getIsbn(), loan);
    //     return true;
    // }

    // public Loan getLoan(String isbn) {
    //     return loans.get(isbn);
    // }
}
