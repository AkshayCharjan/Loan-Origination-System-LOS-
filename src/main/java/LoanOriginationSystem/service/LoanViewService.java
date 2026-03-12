package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.LoanAssignmentViewDTO;
import LoanOriginationSystem.repository.LoanAssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanViewService {

    private final LoanAssignmentRepository loanAssignmentRepository;

    public LoanViewService(LoanAssignmentRepository loanAssignmentRepository) {
        this.loanAssignmentRepository = loanAssignmentRepository;
    }

    public List<LoanAssignmentViewDTO> getLoanAssignments(){
        return loanAssignmentRepository.fetchLoanAssignments();
    }
}
