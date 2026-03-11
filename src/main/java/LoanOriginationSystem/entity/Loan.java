package LoanOriginationSystem.entity;

import LoanOriginationSystem.LoanType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Entity
public class Loan {

    @GeneratedValue
    @UuidGenerator
    @Id
    private  UUID loanId;
    private String customerName;
    private String customerPhone;
    private int loanAmount;
    private LoanType loanType;
    private LocalDateTime createdAt;

    public Loan(String customerName, String customerPhone, int loanAmount, LoanType loanType) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.loanAmount = loanAmount;
        this.loanType = loanType;
        this.createdAt = LocalDateTime.now();
    }
}
