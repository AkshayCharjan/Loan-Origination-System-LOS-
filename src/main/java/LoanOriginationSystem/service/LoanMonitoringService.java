package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.LoanStatusCountProjection;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.enums.ApplicationStatus;
import LoanOriginationSystem.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanMonitoringService {

    private final LoanRepository loanRepository;

    public LoanMonitoringService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<LoanStatusCountProjection> getLoanStatusCounts() {
        return loanRepository.countLoansByStatus();
    }

    public Page<Loan> getLoansByStatus(ApplicationStatus status, int page, int size){
        return loanRepository.findByApplicationStatus(status, PageRequest.of(page, size));
    }

    public Page<Loan> getAllLoans(int page, int size){
        return loanRepository.findAll(PageRequest.of(page, size));
    }
}
