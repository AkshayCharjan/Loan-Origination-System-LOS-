package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.enums.ApplicationStatus;
import LoanOriginationSystem.repository.LoanRepository;
import jakarta.annotation.PostConstruct;
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
        for(int i = 0; i < 5; i++){
            loanProcessor.submit(this::processLoan);
        }
    }

    @PostConstruct
    private void startWorkers(){
        for(int i = 0; i < 5; i++){
            loanProcessor.submit(this::processLoan);
        }
    }

    private void processLoan() {
        while(true) {
            try {
                List<Loan> loans = loanRepository.fetchLoansForProcessing(10);

                for (Loan loan : loans) {
                    Thread.sleep(25000); //simulate System checks

                    int decision = random.nextInt(3);
                    if (decision == 0) {
                        loan.setApplicationStatus(ApplicationStatus.APPROVED_BY_SYSTEM);
                    } else if (decision == 1) {
                        loan.setApplicationStatus(ApplicationStatus.REJECTED_BY_SYSTEM);
                    } else {
                        loan.setApplicationStatus(ApplicationStatus.UNDER_REVIEW);
                    }

                    loanRepository.save(loan);
                }
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Loan processor interrupted");
                break;
            }
        }
    }
}
