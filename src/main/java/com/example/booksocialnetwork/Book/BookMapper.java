package com.example.booksocialnetwork.Book;

import com.example.booksocialnetwork.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest request){
        return Book.builder()
                .id(request.Id())
                .title(request.title())
                .archived(false)
                .shareable(request.shareable())
                .isbn(request.isbn())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .build();

    }

    public BookResponse toBookResponse(Book book){

        return BookResponse.builder()
                .owner(book.getOwner().getName())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .authorName(book.getAuthorName())
                .title(book.getAuthorName())
                .rate(book.getRate())
                .isbn(book.getIsbn())
                .id(book.getId())
                .synopsis(book.getSynopsis())
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returnApproved(history.getReturnApproved())
                .returned(history.getReturned())
                .build();
    }
}
