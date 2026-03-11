package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.entity.LoanAssignment;
import LoanOriginationSystem.enums.AgentStatus;
import LoanOriginationSystem.repository.AgentRepository;
import LoanOriginationSystem.repository.LoanRepository;
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

    public AgentAssignmentService( AgentRepository agentRepository, LoanRepository loanRepository) {
        this.agentRepository = agentRepository;
        this.loanRepository = loanRepository;
        this.agentAssigner = Executors.newFixedThreadPool(5);
    }

    @PostConstruct
    private void startWorkers(){
        for(int i = 0; i < 5; i++){
            agentAssigner.submit(this::assignAgents);
        }
    }

    private void workerLoop(){
        while(true){
                assignAgents();
        }
    }

    @Transactional
    private void assignAgents(){
        List<Loan> loansForReview = loanRepository.fetchLoansForReview(10);

        for(Loan loan: loansForReview) {
            Agent agent = agentRepository.findFirstByStatus(AgentStatus.AVAILABLE)
                    .orElse(null);

            if (agent != null) {
                agent.setStatus(AgentStatus.BUSY);

                LoanAssignment loanAssignment = new LoanAssignment();
                loanAssignment.setLoan(loan);
                loanAssignment.setAgent(agent);
                agentRepository.save(agent);
                loanRepository.save(loan);
            }
        }
    }



}
