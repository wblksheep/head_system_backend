package com.haiyin.sprinkler.infrastructure.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class BatchConfig {

    @Bean
    public Job fileProcessingJob(JobRepository jobRepository, Step step){
        return new JobBuilder("fileProcessingJob", jobRepository)
                .start(step)
                .build();
    }
}
