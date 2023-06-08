package com.sinha.onepunchman;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Hero> reader() {
        return new FlatFileItemReaderBuilder<Hero>()
                .name("heroProcessor")
                .resource(new ClassPathResource("heroes-code.csv"))
                .delimited()
                .names(new String[]{"id", "rank", "className", "name", "gender", "powers"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
                    setTargetType(Hero.class);
                }})
                .build();
    }


    @Bean
    public HeroProcessor processor() {
        return new HeroProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Hero> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Hero>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO heroes (id, hero_rank, class_name, name, gender, powers) VALUES (:id, :rank, :className, :name, :gender, :powers)")
                .dataSource(dataSource)
                .build();
    }


    @Bean
    public Job importJob(JobRepository jobRepository, JobExecutionListener jobExecutionListener, Step step) {
        return new JobBuilder("importJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .flow(step)
                .end()
                .build();
    }

    @Bean Step stepEach(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Hero> writer){
        return new StepBuilder("stepEach", jobRepository)
                .<Hero, Hero>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

}
