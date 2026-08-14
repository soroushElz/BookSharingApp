package com.example.booksocialnetwork.Book;

import com.example.booksocialnetwork.Commons.PageResponse;
import com.example.booksocialnetwork.Exception.OperationNotPermittedException;
import com.example.booksocialnetwork.history.BookTransactionHistory;
import com.example.booksocialnetwork.history.BookTransactionHistoryRepository;
import com.example.booksocialnetwork.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    public Integer save(BookRequest request, Authentication connectedUser) {
        Book book=bookMapper.toBook(request);
        User user=(User) connectedUser.getPrincipal();
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
       return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                 .orElseThrow(()->new EntityNotFoundException("No book found with ID::"+ bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books= bookRepository.findAllDisplayableBooks(pageable,user.getId());
        List<BookResponse> booksResponse=books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<BookResponse>(
                booksResponse,
                books.getNumber(),
                books.getSize(),
                (int) books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks =bookTransactionHistoryRepository.findAllBorrowedBooks(pageable,user.getId());
        List<BorrowedBookResponse> BorrowedBooks= allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<BorrowedBookResponse>(
                BorrowedBooks,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                (int) allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }


    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books= bookRepository.findAll(BookSpecification.withOwnerId(user.getId()),pageable);
        List<BookResponse> booksResponse= books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<BookResponse>(
                booksResponse,
                books.getNumber(),
                books.getSize(),
                (int) books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );

    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable,user.getId());
        List<BorrowedBookResponse> booksResponse= allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<BorrowedBookResponse>(
                booksResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                (int) allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer id, Authentication connectedUser) {
        Book book= bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + id));
        User user=(User) connectedUser.getPrincipal();
        if (Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You cannot update others books shareable status");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return id;
    }

    public Integer updateArchivedStatus(Integer id, Authentication connectedUser) {
        Book book= bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + id));
        User user=(User) connectedUser.getPrincipal();
        if (Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You cannot update others books Archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return id;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book= bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
        if(!book.isShareable() || book.isArchived()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }

        User user=(User) connectedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }
        final Boolean isBookBorrowedByUser = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId,user.getId());
        if (isBookBorrowedByUser){
            throw new OperationNotPermittedException("You already borrowed this book and it is still not returned or the return is not approved by the owner");
        }

        final Boolean isBookBorrowedByOtherUsers = bookTransactionHistoryRepository.isAlreadyBorrowed(bookId);
        if (isBookBorrowedByOtherUsers){
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }
     BookTransactionHistory bookTransactionHistory= BookTransactionHistory.builder()
             .user(user)
              .returned(false)
               .returnApproved(false)
                .book(book)
                 .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer id, Authentication connectedUser) {
        Book book= bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + id));
        if(!book.isShareable() || book.isArchived()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }

        User user=(User) connectedUser.getPrincipal();

       BookTransactionHistory bookTransactionHistory=bookTransactionHistoryRepository.findByBookIdAndUserId(id,user.getId())
               .orElseThrow(() ->  new OperationNotPermittedException("You did not borrow this book"));
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedBook(Integer id, Authentication connectedUser) {
        Book book= bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + id));
        if(!book.isShareable() || book.isArchived()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user=(User) connectedUser.getPrincipal();

        if(!Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You cannot approve the return of a book you do not own");
        }

        BookTransactionHistory bookTransactionHistory=bookTransactionHistoryRepository.findReturnedByBookIdAndOwnerId(id,user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve its return"));

        bookTransactionHistory.setReturned(true);
       return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }
}
