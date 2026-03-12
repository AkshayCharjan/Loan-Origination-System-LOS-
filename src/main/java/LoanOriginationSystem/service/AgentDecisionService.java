package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.entity.LoanAssignment;
import LoanOriginationSystem.enums.ApplicationStatus;
import LoanOriginationSystem.exception.InvalidDecisionException;
import LoanOriginationSystem.exception.LoanAlreadyDecidedException;
import LoanOriginationSystem.exception.LoanNotAssignedException;
import LoanOriginationSystem.repository.AgentRepository;
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
    private final AgentRepository agentRepository;

    public AgentDecisionService(
            LoanRepository loanRepository,
            LoanAssignmentRepository assignmentRepository,
            NotificationService notificationService, AgentRepository agentRepository) {
        this.loanRepository = loanRepository;
        this.assignmentRepository = assignmentRepository;
        this.notificationService = notificationService;
        this.agentRepository = agentRepository;
    }

    public void decide(UUID agentId, UUID loanId, String decision){
        
        if (!decision.equalsIgnoreCase("APPROVE") &&
            !decision.equalsIgnoreCase("REJECT")) {
            throw new InvalidDecisionException("Decision must be APPROVE or REJECT");
        }

        LoanAssignment assignment = assignmentRepository
                .findByLoan_LoanIdAndAgent_Id(loanId, agentId)
                .orElseThrow(() -> new LoanNotAssignedException(
                        "Loan " + loanId + " is not assigned to agent " + agentId));

        Loan loan = assignment.getLoan();

        if (loan.getApplicationStatus() == ApplicationStatus.APPROVED_BY_AGENT ||
            loan.getApplicationStatus() == ApplicationStatus.REJECTED_BY_AGENT) {
            throw new LoanAlreadyDecidedException(
                    "Loan " + loanId + " already has a decision"
            );
        }

        if("APPROVE".equalsIgnoreCase(decision)){
            loan.approveByAgent();
            notificationService.notifyCustomer(loan.getCustomerPhone(), loanId);
        }
        else{
            loan.rejectByAgent();
        }

        Agent agent = assignment.getAgent();
        agent.markAvailable();
        agentRepository.save(agent);
        loanRepository.save(loan);
        log.info("Agent decision recorded: loanId={}, customerName={}, agentId={}, agentName={}, decision={}",
                loanId, loan.getCustomerName(), agentId, agent.getName(), decision);
    }
}
