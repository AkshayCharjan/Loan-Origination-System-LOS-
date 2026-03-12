package LoanOriginationSystem.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AgentAssignmentWorker {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final AgentAssignmentService assignmentService;

    public AgentAssignmentWorker(AgentAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostConstruct
    public void startWorkers() {
        for (int i = 0; i < 5; i++) {
            executor.submit(this::workerLoop);
        }
    }

    private void workerLoop() {
        while (true) {
            assignmentService.assignBatch();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
