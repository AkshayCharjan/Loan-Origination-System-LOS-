package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.entity.LoanAssignment;
import LoanOriginationSystem.enums.AgentStatus;
import LoanOriginationSystem.repository.AgentRepository;
import LoanOriginationSystem.repository.LoanAssignmentRepository;
import LoanOriginationSystem.repository.LoanRepository;
import LoanOriginationSystem.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentAssignmentService {
    private static final Logger log = LoggerFactory.getLogger(AgentAssignmentService.class);
    private final AgentRepository agentRepository;
    private final LoanRepository loanRepository;
    private final LoanAssignmentRepository loanAssignmentRepository;
    private final NotificationService notificationService;

    public AgentAssignmentService(
            AgentRepository agentRepository,
            LoanRepository loanRepository,
            LoanAssignmentRepository loanAssignmentRepository,
            NotificationService notificationService){
        this.agentRepository = agentRepository;
        this.loanRepository = loanRepository;
        this.loanAssignmentRepository = loanAssignmentRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void assignBatch(){
        List<Loan> loansForReview = loanRepository.fetchLoansForReview(10);

        for(Loan loan: loansForReview) {
            log.info("Loan picked for agent review: loanId={}", loan.getLoanId());
            
            Agent agent = agentRepository.findFirstByStatus(AgentStatus.AVAILABLE)
                    .orElse(null);

            if (agent == null) {
                log.warn("No available agents for loanId={}", loan.getLoanId());
                break;
            }

            // create assignment
            LoanAssignment assignment = new LoanAssignment();
            assignment.setLoan(loan);
            assignment.setAgent(agent);

            loanAssignmentRepository.save(assignment);

            loan.markAssignedToAgent();
            loanRepository.save(loan);

            // mark agent busy
            agent.markBusy();
            agentRepository.save(agent);

            log.info("Loan assigned: loanId={}, agentId={}", loan.getLoanId(), agent.getId());

            //notifications
            notificationService.notifyAgent(agent.getId(), loan.getLoanId());

            if (agent.getManagerID() != null) {
                notificationService.notifyManager(agent.getManagerID(), loan.getLoanId());
            }
        }
    }
}
