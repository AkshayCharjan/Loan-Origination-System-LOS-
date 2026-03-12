package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class LoanProcessorService {
    private static final Logger log = LoggerFactory.getLogger(LoanProcessorService.class);
    private final Random random = new Random();
    private final LoanRepository loanRepository;

    public LoanProcessorService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Transactional
    public void processBatch(){
        List<Loan> loans = loanRepository.fetchLoansForProcessing(10);

        for (Loan loan : loans) {
            try {
                Thread.sleep(25000); //simulate System checks
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }

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
    }
}
