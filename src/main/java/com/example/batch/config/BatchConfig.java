package com.example.batch.config;

import com.example.batch.job.AuthorCsvReader;
import com.example.batch.job.AuthorItemProcessor;
import com.example.batch.job.AuthorItemWriter;
import com.example.batch.model.Author;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private AuthorCsvReader authorCsvReader;
    
    @Autowired
    private AuthorItemProcessor authorItemProcessor;
    
    @Autowired
    private AuthorItemWriter authorItemWriter;
    
    @Value("${input.csv.file:classpath:authors.csv}")
    private Resource inputCsvFile;
    
    @Bean
    public Job processAuthorsJob() {
        return jobBuilderFactory.get("processAuthorsJob")
                .incrementer(new RunIdIncrementer())
                .flow(processAuthorsStep())
                .end()
                .build();
    }
    
    @Bean
    public Step processAuthorsStep() {
        return stepBuilderFactory.get("processAuthorsStep")
                .<Author, Author>chunk(10)
                .reader(authorCsvReader.reader(inputCsvFile))
                .processor(authorItemProcessor)
                .writer(authorItemWriter)
                .build();
    }
}
