package LoanOriginationSystem.repository;

import LoanOriginationSystem.dto.LoanAssignmentViewDTO;
import LoanOriginationSystem.entity.LoanAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanAssignmentRepository extends JpaRepository<LoanAssignment, UUID> {

    Optional<LoanAssignment> findByLoan_LoanId(UUID loanId);

    @Query("""
           SELECT new LoanOriginationSystem.dto.LoanAssignmentViewDTO(
               l.loanId,
               l.customerName,
               l.loanAmount,
               l.applicationStatus,
               a.id,
               a.name,
               a.email
           )
           FROM LoanAssignment la
           JOIN la.loan l
           JOIN la.agent a
           """)
    List<LoanAssignmentViewDTO> fetchLoanAssignments();
}
