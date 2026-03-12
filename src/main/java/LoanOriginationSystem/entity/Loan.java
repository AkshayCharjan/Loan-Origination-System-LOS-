package LoanOriginationSystem.entity;

import LoanOriginationSystem.enums.ApplicationStatus;
import LoanOriginationSystem.enums.LoanType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class Loan {

    @GeneratedValue
    @UuidGenerator
    @Id
    private  UUID loanId;
    private String customerName;
    private String customerPhone;
    private int loanAmount;
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @Setter
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
    private LocalDateTime createdAt;

    public Loan(String customerName, String customerPhone, int loanAmount, LoanType loanType) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.loanAmount = loanAmount;
        this.loanType = loanType;
        this.applicationStatus = ApplicationStatus.APPLIED;
        this.createdAt = LocalDateTime.now();
    }
}
