package com.example.booksocialnetwork.Feedback;

import com.example.booksocialnetwork.Book.Book;
import com.example.booksocialnetwork.Commons.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Feedback extends BaseEntity {
    @Column
    private double note;
    private String comment;
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;
}
