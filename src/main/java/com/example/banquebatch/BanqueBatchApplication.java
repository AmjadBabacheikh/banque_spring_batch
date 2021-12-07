package com.example.banquebatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BanqueBatchApplication {

    public static void main(String[] args) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        ApplicationContext ctx = SpringApplication.run(BanqueBatchApplication.class,args);
        Job job = (Job) ctx.getBean("importTransactions");
		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
		JobExecution jobex = jobLauncher.run(job, new JobParameters());
        System.out.println(jobex.getStatus());
    }

}
