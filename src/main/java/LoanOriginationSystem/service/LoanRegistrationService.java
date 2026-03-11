package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.LoanRequestDTO;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.repository.LoanRepository;
import org.springframework.stereotype.Service;

@Service
public class LoanRegistrationService {
    private final LoanRepository loanRepository;

    public LoanRegistrationService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void registerLoan(LoanRequestDTO loanRequest){
        Loan loan = new Loan(
                loanRequest.getCustomerName(),
                loanRequest.getCustomerPhone(),
                loanRequest.getLoanAmount(),
                loanRequest.getLoanType()
        );

        loanRepository.save(loan);
    }
}
