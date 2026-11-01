package com.example.batch.config;

import com.example.batch.job.AuthorCsvReader;
import com.example.batch.job.AuthorItemProcessor;
import com.example.batch.job.AuthorItemWriter;
import com.example.batch.model.Author;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Import(DataSourceConfig.class)
public class BatchConfig {

    @Value("${input.csv.file}")
    private Resource inputCsvFile;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private AuthorCsvReader authorCsvReader;

    @Autowired
    private AuthorItemProcessor authorItemProcessor;

    @Autowired
    private AuthorItemWriter authorItemWriter;


    @Bean
    public Job processAuthorsJob() {
        return new JobBuilder("processAuthorsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(processAuthorsStep())
                .end()
                .build();
    }

    @Bean
    public Step processAuthorsStep() {
        return new StepBuilder("processAuthorsStep", jobRepository)
                .<Author, Author>chunk(10, transactionManager)
                .reader(authorCsvReader.reader(inputCsvFile))
                .processor(authorItemProcessor)
                .writer(authorItemWriter)
                .build();
    }
}
