package LoanOriginationSystem.repository;

import LoanOriginationSystem.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {

}
