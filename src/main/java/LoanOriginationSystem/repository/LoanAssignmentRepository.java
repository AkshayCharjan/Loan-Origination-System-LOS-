package LoanOriginationSystem.repository;

import LoanOriginationSystem.entity.LoanAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoanAssignmentRepository extends JpaRepository<LoanAssignment, UUID> {

    Optional<LoanAssignment> findByLoan_LoanId(UUID loanId);
}
