package br.edu.ifba.inf008.plugins;

import java.io.Serializable;
import java.time.LocalDate;

public class Loan implements Serializable{
    private String bookIsbn;
    private Integer userId;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public Loan(String bookIsbn, Integer userId, LocalDate borrowDate){
        this.bookIsbn = bookIsbn;
        this.userId = userId;
        this.borrowDate = borrowDate;
    }

    @Override
    public String toString(){
        return String.format(
            "ISBN: %s. Title: %d. Loan date: %s.",
            bookIsbn, userId, borrowDate
            );
    }

    public Integer getUserId(){
        return userId;
    }

    public String getBookIsbn(){
        return bookIsbn;
    }

    public LocalDate getReturnDate(){
        return returnDate;
    }

    public LocalDate getLoanDate(){
        return borrowDate;
    }

    public void finishLoan(){
        returnDate = LocalDate.now();
    }
}
