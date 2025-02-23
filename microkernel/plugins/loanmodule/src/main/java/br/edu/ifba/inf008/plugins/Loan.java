package br.edu.ifba.inf008.plugins;

import java.time.LocalDateTime;

public class Loan {
    private String bookIsbn;
    private Integer userId;
    private LocalDateTime date;

    public Loan(String bookIsbn, Integer userId){
        this.bookIsbn = bookIsbn;
        this.userId = userId;
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString(){
        return String.format(
            "ISBN: %s. Title: %d. Loan date: %s.",
            bookIsbn, userId, date
            );
    }

    public String getBookIsbn(){
        return bookIsbn;
    }
}
