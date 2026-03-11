package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.LoanRequestDTO;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoanRegistrationService {
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
        return loan.getLoanId();
    }
}
