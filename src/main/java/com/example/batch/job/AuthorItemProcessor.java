package com.example.batch.job;

import com.example.batch.model.Author;
import com.example.batch.model.Book;
import com.example.batch.repository.AuthorRepository;
import com.example.batch.service.OpenLibraryService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorItemProcessor implements ItemProcessor<Author, Author> {
    
    private final AuthorRepository authorRepository;
    private final OpenLibraryService openLibraryService;
    
    @Autowired
    public AuthorItemProcessor(AuthorRepository authorRepository, 
                               OpenLibraryService openLibraryService) {
        this.authorRepository = authorRepository;
        this.openLibraryService = openLibraryService;
    }
    
    @Override
    public Author process(Author author) throws Exception {
        // Check if author already exists in database
        authorRepository.findByName(author.getName())
                .ifPresent(existingAuthor -> {
                    author.setId(existingAuthor.getId());
                    author.setBooks(existingAuthor.getBooks());
                });
        
        // Fetch books from OpenLibrary API
        List<Book> books = openLibraryService.getBooksByAuthor(author.getName());
        
        // Add books to author
        for (Book book : books) {
            author.addBook(book);
        }
        
        System.out.println("Processed author: " + author.getName() + 
                          " with " + books.size() + " books");
        
        return author;
    }
}
