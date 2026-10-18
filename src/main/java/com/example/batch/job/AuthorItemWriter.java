package com.example.batch.job;

import com.example.batch.model.Author;
import com.example.batch.repository.AuthorRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorItemWriter implements ItemWriter<Author> {
    
    private final AuthorRepository authorRepository;
    
    @Autowired
    public AuthorItemWriter(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    
    @Override
    public void write(List<? extends Author> authors) throws Exception {
        for (Author author : authors) {
            // Save author with cascade to save all books
            authorRepository.save(author);
            System.out.println("Saved author: " + author.getName() + 
                              " (ID: " + author.getId() + ")" +
                              " with " + author.getBooks().size() + " books");
        }
    }
}
