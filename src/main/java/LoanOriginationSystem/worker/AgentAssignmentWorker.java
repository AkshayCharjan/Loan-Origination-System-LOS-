package LoanOriginationSystem.worker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import LoanOriginationSystem.service.AgentAssignmentService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class AgentAssignmentWorker {

    private final ExecutorService executor;
    private final AgentAssignmentService assignmentService;

    public AgentAssignmentWorker(
            AgentAssignmentService assignmentService,
            @Value("${loan.agent-assignment.thread-count:5}") int threadCount) {
        this.assignmentService = assignmentService;
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    @PostConstruct
    public void startWorkers() {
        int threadCount = ((ThreadPoolExecutor) executor).getCorePoolSize();
        for (int i = 0; i < threadCount; i++) {
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
