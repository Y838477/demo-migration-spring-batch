package com.example.batch.job;

import com.example.batch.model.Author;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AuthorCsvReader {

    public ItemReader<Author> reader(Resource resource) {
        FlatFileItemReader<Author> reader = new FlatFileItemReader<>(lineMapper());
        reader.setResource(resource);
        reader.setLinesToSkip(1); // Skip header line

        return reader;
    }

    private LineMapper<Author> lineMapper() {
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

        return lineMapper;
    }
}
