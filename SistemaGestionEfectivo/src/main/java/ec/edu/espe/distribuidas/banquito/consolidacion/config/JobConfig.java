/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.distribuidas.banquito.consolidacion.config;

import ec.edu.espe.distribuidas.banquito.consolidacion.tasks.Consolidado;
import ec.edu.espe.distribuidas.banquito.consolidacion.tasks.LeerCondiciones;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 *
 * @author Admin
 */
@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class JobConfig {
 
    @Autowired
    private JobBuilderFactory jobs;
    
    @Autowired
    private StepBuilderFactory steps;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private ApplicationValues applicationValues;
    
    @Bean
    protected Step leerCondiciones(){
        return steps
                .get("leerCondiciones")
                .tasklet(new LeerCondiciones(this.applicationValues))
                .build();
    }
  
       @Bean
    public Job consolidarInformacion(){
        return jobs
                .get("consolidacion")
                .start(leerCondiciones())
                .build();
    }
    
}
