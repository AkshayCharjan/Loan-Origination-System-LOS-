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

    public void markAssignedToAgent() {
        this.applicationStatus = ApplicationStatus.ASSIGNED_TO_AGENT;
    }

    public void approveBySystem() {
        this.applicationStatus = ApplicationStatus.APPROVED_BY_SYSTEM;
    }

    public void rejectBySystem() {
        this.applicationStatus = ApplicationStatus.REJECTED_BY_SYSTEM;
    }

    public void sendForReview() {
        this.applicationStatus = ApplicationStatus.UNDER_REVIEW;
    }

    public void approveByAgent() {
        this.applicationStatus = ApplicationStatus.APPROVED_BY_AGENT;
    }

    public void rejectByAgent() {
        this.applicationStatus = ApplicationStatus.REJECTED_BY_AGENT;
    }
}
