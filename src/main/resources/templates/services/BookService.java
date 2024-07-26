package ru.spring.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spring.mvc.models.Book;
import ru.spring.mvc.models.Person;
import ru.spring.mvc.repositories.BookRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return bookRepository.findAll(Sort.by("year"));
        } else {
            return bookRepository.findAll();
        }
    }

    public List<Book> findWithPagination(boolean sortByYear,
                                         Integer page,
                                         Integer booksPerPage) {
        if (sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    @Transactional(readOnly = true)
    public Book findById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public void update(int id ,Book updatedBook) {
        Book book = bookRepository.findById(id).orElse(null);

        updatedBook.setId(id);
        updatedBook.setOwner(book.getOwner());

        bookRepository.save(updatedBook);
    }

    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public Person findOwnerByBookId(int id) {
        return bookRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    public void assign(int bookId, Person person) {
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setOwner(person);
            book.setTakenAt(new Date());
        });
    }

    public void release(int bookId) {
        bookRepository.findById(bookId).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
        });
    }

    public List<Book> search(String query) {
        return bookRepository.findByTitleStartingWith(query);
    }
}
