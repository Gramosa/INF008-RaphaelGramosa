package br.edu.ifba.inf008.events;

import br.edu.ifba.inf008.interfaces.IEventData;

public class BookBorrowEvent implements IEventData{
    private Integer userId;
    private String bookIsbn;

    BookBorrowEvent(Integer userId, String bookIsbn){
        this.userId = userId;
        this.bookIsbn = bookIsbn;
    }

    public String getEventName(){
        return "borrow_book";
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public Integer getUserId() {
        return userId;
    }
}
