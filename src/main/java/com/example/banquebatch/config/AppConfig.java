package com.example.banquebatch.config;

import com.example.banquebatch.batch.BatchLauncher;
import com.example.banquebatch.batch.TransactionProcessor;
import com.example.banquebatch.batch.TransactionWriter;
import com.example.banquebatch.entities.Transaction;
import com.example.banquebatch.entities.TransactionDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

@EnableBatchProcessing
@EnableScheduling
@Configuration
public class AppConfig {


    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Autowired
    PlatformTransactionManager dataSourceTransactionManager;


    @Bean
    public ItemReader<? extends TransactionDto> reader() {
        FlatFileItemReader<TransactionDto> reader = new FlatFileItemReader<>();
        reader.setResource((new FileSystemResource("src/main/resources/transactions.txt")));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"idTransaction", "idCompte", "montant", "dateTransaction"});
                setDelimiter(",");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(TransactionDto.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemWriter<Transaction> writer() {
        return new TransactionWriter();
    }

    @Bean
    public ItemProcessor<TransactionDto, Transaction> processor() {
        return new TransactionProcessor();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<TransactionDto, Transaction>chunk(2)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                //.taskExecutor(taskExecutor()) is used to proccess the chunk in parallel
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        return threadPoolTaskExecutor;
    }

    @Bean(name = "importTransactions")
    public Job importTransactions(JobBuilderFactory jobs) {
        return jobs.get("importTransactions")
                .start(step1())
                .build();
    }

    @Bean(name = "transactionJobRepository")
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(dataSourceTransactionManager);
        return factory.getObject();
    }

    @Bean(name = "joblancher")
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }

    @Bean
    public BatchLauncher launchBatch() {
        return new BatchLauncher();
    }

    //@Scheduled(cron = "*,0,0,1,*,*")
    public void scheduleFixedDelayTask() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        launchBatch().run();
    }


}
