package LoanOriginationSystem.repository;

import LoanOriginationSystem.dto.LoanStatusCountProjection;
import LoanOriginationSystem.dto.TopCustomerProjection;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {

    @Query(
            value = """
            SELECT *
            FROM loan
            WHERE application_status = 'APPLIED'
            ORDER BY created_at
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """,
            nativeQuery = true
    )
    List<Loan> fetchLoansForProcessing(@Param("limit") int limit);

    @Query(
            value = """
            SELECT *
            FROM loan
            WHERE application_status = 'UNDER_REVIEW'
            ORDER BY created_at
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """,
            nativeQuery = true
    )
    List<Loan> fetchLoansForReview(@Param("limit") int limit);

    @Query("""
           SELECT l.applicationStatus AS applicationStatus, COUNT(l) AS count
           FROM Loan l
           GROUP BY l.applicationStatus
           """)
    List<LoanStatusCountProjection> countLoansByStatus();

    @Query("""
           SELECT l.customerName AS customerName, COUNT(l) AS loanCount
           FROM Loan l
           WHERE l.applicationStatus IN (
                LoanOriginationSystem.enums.ApplicationStatus.APPROVED_BY_SYSTEM,
                LoanOriginationSystem.enums.ApplicationStatus.APPROVED_BY_AGENT
                )
           GROUP BY l.customerName
           ORDER BY COUNT(l) DESC
           """)
    List<TopCustomerProjection> findTopCustomers(Pageable pageable);

    Page<Loan> findByApplicationStatus(ApplicationStatus status, Pageable pageable);
}
