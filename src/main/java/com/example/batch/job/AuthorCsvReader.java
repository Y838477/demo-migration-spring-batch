package com.example.batch.job;

import com.example.batch.model.Author;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AuthorCsvReader {
    
    public FlatFileItemReader<Author> reader(Resource resource) {
        FlatFileItemReader<Author> reader = new FlatFileItemReader<>();
        reader.setResource(resource);
        
        // Configure the line mapper
        DefaultLineMapper<Author> lineMapper = new DefaultLineMapper<>();
        
        // Tokenizer - expects CSV with name,nationality format
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("name", "nationality");
        tokenizer.setDelimiter(",");
        
        // Map tokens to Author object
        BeanWrapperFieldSetMapper<Author> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Author.class);
        
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        reader.setLineMapper(lineMapper);
        reader.setLinesToSkip(1); // Skip header line
        
        return reader;
    }
}
