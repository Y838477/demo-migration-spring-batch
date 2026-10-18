package com.example.batch.service;

import com.example.batch.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenLibraryService {
    
    private final WebClient webClient;
    private final String openLibraryBaseUrl;
    
    public OpenLibraryService(WebClient.Builder webClientBuilder, 
                              @Value("${openlibrary.base-url:https://openlibrary.org}") String openLibraryBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(openLibraryBaseUrl).build();
        this.openLibraryBaseUrl = openLibraryBaseUrl;
    }
    
    /**
     * Search for an author by name and retrieve their works
     */
    public List<Book> getBooksByAuthor(String authorName) {
        try {
            // First, search for the author
            String searchUrl = "/search/authors.json?q=" + authorName.replace(" ", "+");
            
            OpenLibraryAuthorSearchResult searchResult = webClient.get()
                    .uri(searchUrl)
                    .retrieve()
                    .bodyToMono(OpenLibraryAuthorSearchResult.class)
                    .block();
            
            if (searchResult == null || searchResult.getDocs() == null || searchResult.getDocs().isEmpty()) {
                return new ArrayList<>();
            }
            
            // Get the first author's key
            String authorKey = searchResult.getDocs().get(0).getKey();
            
            // Now get the author's works
            String worksUrl = authorKey + "/works.json?limit=50";
            OpenLibraryWorksResult worksResult = webClient.get()
                    .uri(worksUrl)
                    .retrieve()
                    .bodyToMono(OpenLibraryWorksResult.class)
                    .block();
            
            if (worksResult == null || worksResult.getEntries() == null) {
                return new ArrayList<>();
            }
            
            // Convert works to Book entities
            return worksResult.getEntries().stream()
                    .map(work -> {
                        String title = work.getTitle();
                        Integer publicationYear = work.getFirstPublishYear();
                        String workKey = work.getKey();
                        String openLibraryId = extractIdFromKey(workKey);
                        
                        return new Book(title, publicationYear, openLibraryId);
                    })
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            System.err.println("Error fetching books for author " + authorName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    private String extractIdFromKey(String key) {
        if (key == null) return null;
        String[] parts = key.split("/");
        return parts[parts.length - 1];
    }
    
    // Inner classes for OpenLibrary API response parsing
    
    public static class OpenLibraryAuthorSearchResult {
        private int numFound;
        private int start;
        private List<OpenLibraryAuthorDoc> docs;
        
        public int getNumFound() { return numFound; }
        public void setNumFound(int numFound) { this.numFound = numFound; }
        public int getStart() { return start; }
        public void setStart(int start) { this.start = start; }
        public List<OpenLibraryAuthorDoc> getDocs() { return docs; }
        public void setDocs(List<OpenLibraryAuthorDoc> docs) { this.docs = docs; }
    }
    
    public static class OpenLibraryAuthorDoc {
        private String key;
        private String name;
        private String birthDate;
        private String deathDate;
        
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getBirthDate() { return birthDate; }
        public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
        public String getDeathDate() { return deathDate; }
        public void setDeathDate(String deathDate) { this.deathDate = deathDate; }
    }
    
    public static class OpenLibraryWorksResult {
        private List<OpenLibraryWorkEntry> entries;
        
        public List<OpenLibraryWorkEntry> getEntries() { return entries; }
        public void setEntries(List<OpenLibraryWorkEntry> entries) { this.entries = entries; }
    }
    
    public static class OpenLibraryWorkEntry {
        private String key;
        private String title;
        private List<Integer> firstPublishYear;
        
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Integer getFirstPublishYear() {
            return firstPublishYear != null && !firstPublishYear.isEmpty() 
                ? firstPublishYear.get(0) 
                : null;
        }
        public void setFirstPublishYear(List<Integer> firstPublishYear) {
            this.firstPublishYear = firstPublishYear;
        }
    }
}
