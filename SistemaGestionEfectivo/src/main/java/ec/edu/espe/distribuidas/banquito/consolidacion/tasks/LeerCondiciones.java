/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.distribuidas.banquito.consolidacion.tasks;

import ec.edu.espe.distribuidas.banquito.consolidacion.config.ApplicationValues;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.springframework.batch.item.ExecutionContext;

/**
 *
 * @author Admin
 */
@Slf4j
public class LeerCondiciones implements Tasklet, StepExecutionListener {

    private final ApplicationValues applicationValues;

    public LeerCondiciones(ApplicationValues applicationValues) {
        this.applicationValues = applicationValues;
    }

    @Override
    public void beforeStep(StepExecution se) {

    }

    @Override
    public RepeatStatus execute(StepContribution sc, ChunkContext cc) throws Exception {
        Properties props = new Properties();
        try {
            Path path = Path.of(this.applicationValues.getConfigFile());
            props.load(new FileInputStream(this.applicationValues.getConfigFile()));

            Integer billetes10;
            try {
                billetes10 = Integer.parseInt(props.getProperty("billetes10"));
                ExecutionContext jobContext = sc.getStepExecution().getExecutionContext();
                jobContext.put("records", billetes10);
            } catch (NumberFormatException e) {
                log.error("Invalido");
            }
            
            Integer billetes20;
            try {
                billetes20 = Integer.parseInt(props.getProperty("billetes20"));
                ExecutionContext jobContext = sc.getStepExecution().getExecutionContext();
                jobContext.put("records", billetes20);
            } catch (NumberFormatException e) {
                log.error("Invalido");
            }
            
            Integer dineroTotal;
            try {
                dineroTotal = Integer.parseInt(props.getProperty("dineroTotal"));
                ExecutionContext jobContext = sc.getStepExecution().getExecutionContext();
                jobContext.put("records", dineroTotal);
            } catch (NumberFormatException e) {
                log.error("Invalido");
            }

        } catch (IOException e) {
            log.error("Propertie file does not exists");
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution se) {
        log.info("Finalizo la ejecucion");
        return ExitStatus.COMPLETED;
    }

}
