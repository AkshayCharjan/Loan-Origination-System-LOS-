package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.LoanRequestDTO;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoanRegistrationService {
    private static final Logger log = LoggerFactory.getLogger(LoanRegistrationService.class);
    private final LoanRepository loanRepository;

    public LoanRegistrationService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public UUID registerLoan(LoanRequestDTO loanRequest){
        Loan loan = new Loan(
                loanRequest.getCustomerName(),
                loanRequest.getCustomerPhone(),
                loanRequest.getLoanAmount(),
                loanRequest.getLoanType()
        );

        loanRepository.save(loan);
        log.info("Loan created: loanId={}, customer={}, amount={}", loan.getLoanId(),
                loan.getCustomerName(), loan.getLoanAmount());
        return loan.getLoanId();
    }
}
