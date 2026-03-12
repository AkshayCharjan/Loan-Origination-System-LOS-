package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.entity.LoanAssignment;
import LoanOriginationSystem.repository.LoanAssignmentRepository;
import LoanOriginationSystem.repository.LoanRepository;
import LoanOriginationSystem.service.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AgentDecisionService {
    private static final Logger log = LoggerFactory.getLogger(AgentDecisionService.class);

    private final LoanRepository loanRepository;
    private final LoanAssignmentRepository assignmentRepository;
    private final NotificationService notificationService;

    public AgentDecisionService(
            LoanRepository loanRepository,
            LoanAssignmentRepository assignmentRepository,
            NotificationService notificationService) {
        this.loanRepository = loanRepository;
        this.assignmentRepository = assignmentRepository;
        this.notificationService = notificationService;
    }

    public void decide(UUID agentId, UUID loanId, String decision){

        LoanAssignment assignment = assignmentRepository
                .findByLoan_LoanId(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not assigned"));

        if(!assignment.getAgent().getId().equals(agentId)){
            throw new RuntimeException("Agent not authorized");
        }

        Loan loan = assignment.getLoan();

        if("APPROVE".equalsIgnoreCase(decision)){
            loan.approveByAgent();
            notificationService.notifyCustomer(loan.getCustomerPhone(), loanId);
        }
        else{
            loan.rejectByAgent();
        }

        loanRepository.save(loan);
        log.info("Agent decision recorded: loanId={}, agentId={}, decision={}", loanId, agentId, decision);
    }
}
