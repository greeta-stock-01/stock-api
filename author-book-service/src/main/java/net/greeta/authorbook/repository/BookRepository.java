package net.greeta.authorbook.repository;

import net.greeta.authorbook.model.Author;
import net.greeta.authorbook.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthor(Author author);
}
