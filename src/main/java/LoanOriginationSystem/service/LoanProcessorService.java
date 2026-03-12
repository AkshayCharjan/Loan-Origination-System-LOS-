package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.enums.ApplicationStatus;
import LoanOriginationSystem.repository.LoanRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class LoanProcessorService {
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

    @Transactional
    private void processLoans() {
        while(true) {
            processBatch();
        }
    }

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
            }
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Loan processor interrupted");
        }
    }
}
