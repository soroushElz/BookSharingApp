package com.example.booksocialnetwork.Book;

import com.example.booksocialnetwork.Commons.BaseEntity;
import com.example.booksocialnetwork.Feedback.Feedback;
import com.example.booksocialnetwork.history.BookTransactionHistory;
import com.example.booksocialnetwork.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private boolean archived;
    private boolean shareable;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;
    @OneToMany(mappedBy = "book")
    List<Feedback> feedbacks;
    @OneToMany(mappedBy = "book")
    List<BookTransactionHistory> histories;
    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
       double roundedRate = Math.round(rate * 10.0) / 10.0;
        return roundedRate;
    }
}
