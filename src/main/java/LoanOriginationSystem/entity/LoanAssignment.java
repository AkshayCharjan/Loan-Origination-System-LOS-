package LoanOriginationSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
public class LoanAssignment {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Setter
    @OneToOne
    @JoinColumn(name = "loan_id", nullable = false, unique = true)
    private Loan loan;

    @Setter
    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;
}
