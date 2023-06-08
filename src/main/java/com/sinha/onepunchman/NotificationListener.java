package com.sinha.onepunchman;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener implements JobExecutionListener {

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("JOb COMPLETED................");
    }
}
