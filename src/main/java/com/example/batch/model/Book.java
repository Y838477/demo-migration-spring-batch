package com.example.batch.model;

import javax.persistence.*;

@Entity
@Table(name = "BOOKS")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "TITLE", nullable = false)
    private String title;
    
    @Column(name = "PUBLICATION_YEAR")
    private Integer publicationYear;
    
    @Column(name = "OPEN_LIBRARY_ID")
    private String openLibraryId;
    
    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID", nullable = false)
    private Author author;
    
    public Book() {
    }
    
    public Book(String title, Integer publicationYear, String openLibraryId) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.openLibraryId = openLibraryId;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getPublicationYear() {
        return publicationYear;
    }
    
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
    
    public String getOpenLibraryId() {
        return openLibraryId;
    }
    
    public void setOpenLibraryId(String openLibraryId) {
        this.openLibraryId = openLibraryId;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    public void setAuthor(Author author) {
        this.author = author;
    }
}
