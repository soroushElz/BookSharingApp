package com.example.booksocialnetwork.history;

import com.example.booksocialnetwork.Book.Book;
import com.example.booksocialnetwork.Commons.BaseEntity;
import com.example.booksocialnetwork.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookTransactionHistory extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;
    private Boolean returned;
    private Boolean returnApproved;
}
