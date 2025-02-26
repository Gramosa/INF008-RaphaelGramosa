package br.edu.ifba.inf008.plugins;

import java.time.LocalDate;
import java.util.ArrayList;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class BookModuleUI {
    private BookModule bookModule;
    private IUIController uiController;

    public BookModuleUI(BookModule bookModule) {
        this.bookModule = bookModule;
        uiController = ICore.getInstance().getUIController();
    }

    public void buildCreateBookTab() {
        // Criando os campos de entrada
        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        DatePicker pubDatePicker = new DatePicker();
        pubDatePicker.setPromptText("Publication date");

        TextField genreField = new TextField();
        genreField.setPromptText("genre");

        // Botão para adicionar um livro
        Button addButton = new Button("Add book");
        addButton.setOnAction(e -> {
            String isbn = isbnField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            LocalDate pubDate = pubDatePicker.getValue();
            String genre = genreField.getText();

            if (isbn == null || isbn.isBlank() ||
                title == null || title.isBlank() ||
                author == null || author.isBlank() ||
                pubDate == null ||
                genre == null || genre.isBlank()) {
                    uiController.showPopup("Please all fields must have valid values!");
                    return;
            }

            if(!pubDate.isBefore(LocalDate.now())){
                uiController.showPopup("Book cannot be created in the future");
                return;
            }

            Book book = new Book(isbn, title, author, pubDate, genre);

            if (bookModule.addBook(book)) {
                uiController.showPopup(String.format("Book \"%s\" added with success!", titleField.getText()));
            } else {
                uiController.showPopup("ISBN already exists!");
            }
        });

        // Layout da aba
        VBox layout = new VBox(10, isbnField, titleField, authorField, pubDatePicker, genreField, addButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("Create book", layout);
    }

    public void buildSearchBookTab(){
        // Criando os campos de entrada
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        // Botão para buscar um livro
        Button searchButton = new Button("Search Book");
        searchButton.setOnAction(e -> {
            String title = titleField.getText();

            ArrayList<Book> matchingBooks = bookModule.getBooksByTitle(title, true);
            if (!matchingBooks.isEmpty()) {
                for(Book book : matchingBooks){
                    uiController.createTabOnLeft("Books Query", book.toString());
                }
                uiController.showPopup("Search match!");
            } else {
                uiController.showPopup("Not a single book found!");
            }
        });

        // Layout da aba
        VBox layout = new VBox(10, titleField, searchButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("Search Book", layout);
    }

    public void buildListBooksTab() {
        // Botão para listar os livros
        CheckBox availibleBooksCheck = new CheckBox("Only not borrowed books");
        Button listButton = new Button("List Books");
        
        listButton.setOnAction(e -> {
            boolean active = availibleBooksCheck.isSelected();
            ArrayList<Book> availibleBooks = bookModule.getAllBooks(active);
            
            for (Book book : availibleBooks) {
                uiController.createTabOnLeft("Books Query", book.toString());
            }
            uiController.showPopup(String.format("%d books found!", availibleBooks.size()));
        });

        // Layout da aba
        VBox layout = new VBox(10, availibleBooksCheck, listButton);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        // Criar a aba no UIController
        uiController.createTabOnRight("List Books", layout);
    }
}
