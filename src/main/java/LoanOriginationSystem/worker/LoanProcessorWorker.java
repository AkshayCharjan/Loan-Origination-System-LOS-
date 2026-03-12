package LoanOriginationSystem.worker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import LoanOriginationSystem.service.LoanProcessorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class LoanProcessorWorker {

    private final ExecutorService executor;
    private final LoanProcessorService processorService;

    public LoanProcessorWorker(
            LoanProcessorService processorService,
            @Value("${loan.processor.thread-count:5}") int threadCount) {
        this.processorService = processorService;
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
            processorService.processBatch();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
