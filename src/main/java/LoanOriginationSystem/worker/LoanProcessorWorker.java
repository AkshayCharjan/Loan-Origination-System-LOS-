package LoanOriginationSystem.worker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import LoanOriginationSystem.service.LoanProcessorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LoanProcessorWorker {

    private final ExecutorService executor;
    private final LoanProcessorService processorService;
    
    @Value("${loan.processor.thread-count:5}")
    private int threadCount;

    public LoanProcessorWorker(LoanProcessorService processorService,
                              @Value("${loan.processor.thread-count:5}") int threadCount) {
        this.processorService = processorService;
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
            processorService.processBatch();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
