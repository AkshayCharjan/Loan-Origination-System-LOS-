package LoanOriginationSystem.repository;

import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.enums.AgentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AgentRepository extends JpaRepository<Agent, UUID> {

    Optional<Agent> findFirstByStatus(AgentStatus agentStatus);
}