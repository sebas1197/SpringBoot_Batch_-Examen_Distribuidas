/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.distribuidas.banquito.consolidacion.tasks;

import ec.edu.espe.distribuidas.banquito.consolidacion.config.ApplicationValues;
import ec.edu.espe.distribuidas.banquito.consolidacion.model.Consolidacion;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 *
 * @author Admin
 */
@Slf4j
public class Consolidado implements Tasklet, StepExecutionListener {

    private final ApplicationValues applicationValues;
    private final MongoTemplate mongoTemplate;
    private List<Integer> totalBilletes10 = new ArrayList<>();
    private List<Integer> totalBilletes20 = new ArrayList<>();
    private String billetes10_file = "billetes10.txt";
    private String billetes20_file = "billetes20.txt";
    private Integer total;

    public Consolidado(ApplicationValues applicationValues, MongoTemplate mongoTemplate) {
        this.applicationValues = applicationValues;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void beforeStep(StepExecution se) {
        try {
            
            Path fileBilletes = Paths.get(this.applicationValues.getDataPath() + this.billetes10_file);
            List<String> billetes = Files.readAllLines(fileBilletes);

            for (String i : billetes) {
                totalBilletes10.add(Integer.parseInt(i));
            }

            Path fileBilletes2 = Paths.get(this.applicationValues.getDataPath() + this.billetes20_file);
            List<String> billetes2 = Files.readAllLines(fileBilletes2);

            for (String j : billetes2) {
                totalBilletes20.add(Integer.parseInt(j));
            }
            
            
        } catch (Exception e) {
            log.error("Error {}", e);
        }
    }

    @Override
    public RepeatStatus execute(StepContribution sc, ChunkContext cc) throws Exception {
        
        Consolidacion consolidacion = new Consolidacion();
        consolidacion.setTotalBilletes10(totalBilletes10.size());
        consolidacion.setTotalBilletes20(totalBilletes20.size());
        consolidacion.setMontoDisponible(consolidacion.getTotalBilletes10() * 10 + consolidacion.getTotalBilletes20() * 20);

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution se) {
        return ExitStatus.COMPLETED;
    }

}
