package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.LoanStatusCountProjection;
import LoanOriginationSystem.repository.LoanRepository;
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
}
