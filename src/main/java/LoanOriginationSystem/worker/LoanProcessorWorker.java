package LoanOriginationSystem.worker;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import LoanOriginationSystem.service.LoanProcessorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LoanProcessorWorker {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final LoanProcessorService processorService;

    public LoanProcessorWorker(LoanProcessorService processorService) {
        this.processorService = processorService;
    }

    @PostConstruct
    public void startWorkers() {
        for (int i = 0; i < 5; i++) {
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
