package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.repository.LoanRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LoanProcessorService {
    private static final Logger log = LoggerFactory.getLogger(LoanProcessorService.class);
    private final ExecutorService loanProcessor;
    private final Random random = new Random();
    private final LoanRepository loanRepository;

    public LoanProcessorService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
        this.loanProcessor = Executors.newFixedThreadPool(5);
    }

    @PostConstruct
    private void startWorkers(){
        for(int i = 0; i < 5; i++){
            loanProcessor.submit(this::processLoans);
        }
    }

    private void processLoans() {
        while(true) {
            processBatch();
        }
    }

    @Transactional
    private void processBatch(){
        try {
            List<Loan> loans = loanRepository.fetchLoansForProcessing(10);

            for (Loan loan : loans) {
                Thread.sleep(25000); //simulate System checks

                int decision = random.nextInt(3);
                if (decision == 0) {
                    loan.approveBySystem();
                } else if (decision == 1) {
                    loan.rejectBySystem();
                } else {
                    loan.sendForReview();
                }

                loanRepository.save(loan);
                log.info("Loan processor decision: loanId={}, decision={}", loan.getLoanId(),
                        loan.getApplicationStatus());
            }
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Loan processor interrupted");
        }
    }
}
