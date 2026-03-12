package LoanOriginationSystem.worker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import LoanOriginationSystem.service.AgentAssignmentService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AgentAssignmentWorker {

    private final ExecutorService executor;
    private final AgentAssignmentService assignmentService;
    
    @Value("${loan.agent-assignment.thread-count:5}")
    private int threadCount;
    
    @Value("${loan.agent-assignment.processing-delay:1000}")
    private long processingDelay;

    public AgentAssignmentWorker(AgentAssignmentService assignmentService,
                                @Value("${loan.agent-assignment.thread-count:5}") int threadCount) {
        this.assignmentService = assignmentService;
        this.threadCount = threadCount;
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    @PostConstruct
    public void startWorkers() {
        for (int i = 0; i < threadCount; i++) {
            executor.submit(this::workerLoop);
        }
    }

    private void workerLoop() {
        while (true) {
            assignmentService.assignBatch();

            try {
                Thread.sleep(processingDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
