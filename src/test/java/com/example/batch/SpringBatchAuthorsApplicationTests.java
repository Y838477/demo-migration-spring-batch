package com.example.batch;

import com.example.batch.model.Author;
import com.example.batch.model.Book;
import com.example.batch.repository.AuthorRepository;
import com.example.batch.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringBatchAuthorsApplicationTests {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }

    @Test
    void testAuthorRepository() {
        // Create a test author
        Author testAuthor = new Author("Test Author", "Test Nationality");
        authorRepository.save(testAuthor);
        
        // Verify author was saved
        assertNotNull(testAuthor.getId());
        
        // Find author by name
        Author foundAuthor = authorRepository.findByName("Test Author").orElse(null);
        assertNotNull(foundAuthor);
        assertEquals("Test Author", foundAuthor.getName());
        
        // Clean up
        authorRepository.delete(foundAuthor);
    }

    @Test
    void testBookRepository() {
        // Create test author first
        Author testAuthor = new Author("Book Test Author", "Test");
        authorRepository.save(testAuthor);
        
        // Create test book
        Book testBook = new Book("Test Book", 2023, "test-id-123");
        testBook.setAuthor(testAuthor);
        bookRepository.save(testBook);
        
        // Verify book was saved
        assertNotNull(testBook.getId());
        
        // Find books by author
        assertFalse(bookRepository.findByAuthorId(testAuthor.getId()).isEmpty());
        
        // Clean up
        bookRepository.delete(testBook);
        authorRepository.delete(testAuthor);
    }
}
