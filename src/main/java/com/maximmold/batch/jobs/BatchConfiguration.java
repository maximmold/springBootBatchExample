package com.maximmold.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * Created by maxmoldenhauer on 5/17/17.
 */
@Configuration
public class BatchConfiguration {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job firstJob(){
        return jobBuilderFactory.get("firstJob")
                .incrementer(new RunIdIncrementer())
                .start(firstStep())
                .build();
    }

    @Bean
    public Step firstStep() {
        return stepBuilderFactory.get("step1")
                .<Gene, Gene>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemReader<Gene> reader() {
        FlatFileItemReader<Gene> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Gene>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer(){
                    {
                        setNames(new String[]{"geneCode", "geneName", "blah","blah","blah","blah","blah","blah","blah","blah","blah"});
                    }
                });

                setFieldSetMapper(new BeanWrapperFieldSetMapper<Gene>(){
                    {
                        setTargetType(Gene.class);
                        setStrict(false);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public ItemProcessor<Gene, Gene> processor() {
        return new GeneItemProcessor();
    }

    @Bean
    public ItemWriter<Gene> writer() {
        JdbcBatchItemWriter<Gene> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO gene (gene_code, gene_name) VALUES (:geneCode, :geneName)");
        writer.setDataSource(dataSource);
        return writer;
    }
}
