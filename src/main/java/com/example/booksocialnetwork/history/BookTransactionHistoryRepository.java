package com.example.booksocialnetwork.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {


    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
            """)
    public Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable,@Param("userId") Integer id) ;

    @Query("""
                SELECT history
                FROM BookTransactionHistory history
                WHERE history.returned=true
                AND history.book.owner.id=:userId
                """)
    public Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable,@Param("userId") Integer id);


    @Query("""
          SELECT 
          (COUNT(*) > 0) As isBorrowed
          FROM BookTransactionHistory history
          WHERE history.book.id= :bookId
          AND history.user.id= :userId  
          AND history.returnApproved=false  
           """ )
    public Boolean isAlreadyBorrowedByUser(@Param("bookId") Integer bookId,@Param("userId") Integer id) ;

    @Query("""
          SELECT 
          (COUNT(*) > 0) As isBorrowed
          FROM BookTransactionHistory history
          WHERE history.book.id= :bookId 
          AND history.returnApproved=false  
           """ )
    Boolean isAlreadyBorrowed(@Param("bookId") Integer bookId);

    @Query("""
            SELECT history
             FROM BookTransactionHistory history
             WHERE history.book.id=:bookId
             AND history.user.id=:userId
             AND history.returnApproved=false
             AND history.returned=false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(@Param("bookId") Integer id,@Param("userId") Integer id1);

    @Query("""
            SELECT history
             FROM BookTransactionHistory history
             WHERE history.book.id=:bookId
             AND history.book.owner.id=:userId
             AND history.returnApproved=false
             AND history.returned=true
            """)
    Optional<BookTransactionHistory> findReturnedByBookIdAndOwnerId(@Param("bookId") Integer id,@Param("userId") Integer id1);
}
