package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.entity.LoanAssignment;
import LoanOriginationSystem.enums.AgentStatus;
import LoanOriginationSystem.repository.AgentRepository;
import LoanOriginationSystem.repository.LoanAssignmentRepository;
import LoanOriginationSystem.repository.LoanRepository;
import LoanOriginationSystem.service.notification.NotificationService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AgentAssignmentService {
    private final ExecutorService agentAssigner;
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
        this.agentAssigner = Executors.newFixedThreadPool(5);
    }

    @PostConstruct
    private void startWorkers(){
        for(int i = 0; i < 5; i++){
            agentAssigner.submit(this::workerLoop);
        }
    }

    private void workerLoop(){
        while(true){
            assignAgents();
            try {
                Thread.sleep(1000); //sleep for 1 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Transactional
    private void assignAgents(){
        List<Loan> loansForReview = loanRepository.fetchLoansForReview(10);

        for(Loan loan: loansForReview) {
            Agent agent = agentRepository.findFirstByStatus(AgentStatus.AVAILABLE)
                    .orElse(null);

            if (agent != null) {
                agent.markBusy();

                LoanAssignment loanAssignment = new LoanAssignment();
                loanAssignment.setLoan(loan);
                loanAssignment.setAgent(agent);
                agentRepository.save(agent);
                loanAssignmentRepository.save(loanAssignment);

                notificationService.notifyAgent(agent.getId(), loan.getLoanId());

                if(agent.getManagerID() != null){
                    notificationService.notifyManager(agent.getManagerID(), loan.getLoanId());
                }
            }
        }
    }
}
