package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.events.EventData;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IPluginController;
import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IEventData;
import br.edu.ifba.inf008.interfaces.IUIController;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.MenuItem;

public class LoanModule implements IPlugin {
    private HashMap<Integer, ArrayList<Loan>> loans = new HashMap<>(); //userId, loans
    private LoanModuleUI loanModuleUI;

    private IPluginController pluginController;
    private IUIController uiController;

    public boolean init() {
        pluginController = ICore.getInstance().getPluginController();
        uiController = ICore.getInstance().getUIController();
        loanModuleUI = new LoanModuleUI(this);

        ICore core = ICore.getInstance();
        IUIController uiController = core.getUIController();
        pluginController = core.getPluginController();

        // Criar item de menu para abrir a aba "Loan Library"
        String menuText = "Loan Module";
        MenuItem borrowBookMenuItem = uiController.createMenuItem(menuText, "Loan a book");
        borrowBookMenuItem.setOnAction(e -> loanModuleUI.buildBorrowBookTab());

        // MenuItem searchLoanMenuItem = uiController.createMenuItem(menuText, "Search for a loan");
        // searchLoanMenuItem.setOnAction(e -> loanModuleUI.buildSearchLoanTab());

        // MenuItem listLoansMenuItem = uiController.createMenuItem(menuText, "List all loans");
        // listLoansMenuItem.setOnAction(e -> loanModuleUI.buildListLoansTab());
        
        return true;
    }

    public boolean addLoan(Integer userId, String bookIsbn) {
        if(userId == null || bookIsbn == null || bookIsbn.equals("")){
            uiController.showPopup("All fields must have content!");
            return false;
        }

        if(getLoanFromIsbn(bookIsbn) != null){
            uiController.showPopup("Livro ja foi emmprestado!");
            return false;
        }
        
        if(!checkEntityExistence(new EventData<>("check_user", userId), userId.toString(), "User")){
            return false;
        }

        if(!checkEntityExistence(new EventData<>("check_book", bookIsbn), bookIsbn, "Book")){
            return false;
        }

        if(!loans.containsKey(userId)){
            loans.put(userId, new ArrayList<>());
        }

        loans.get(userId).add(new Loan(bookIsbn, userId));
        return true;
    }

    public ArrayList<Loan> getLoansOfUser(Integer userId) {
        return loans.get(userId);
    }

    public Loan getLoanFromIsbn(String isbn) {
        for (ArrayList<Loan> userLoans : loans.values()) {
            for (Loan loan : userLoans) {
                if (loan.getBookIsbn().equals(isbn)) {
                    return loan;
                }
            }
        }
        return null;
    }

    private <T> Boolean checkEntityExistence(IEventData<T> eventData, String identifier, String entityName) {
        Boolean exists = pluginController.emit(eventData);
        if (exists == null) {
            uiController.showPopup("No answer from " + entityName + " module");
            return false;
        } else if (!exists) {
            uiController.showPopup(String.format("%s with identifier %s does not exist", entityName, identifier));
            return false;
        }
        return true;
}
}
