package LoanOriginationSystem.entity;

import LoanOriginationSystem.enums.AgentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Entity
public class Agent {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Setter
    private String name;

    @Setter
    private String email;

    private UUID managerID;

    @Enumerated(EnumType.STRING)
    private AgentStatus status;

    public void markAvailable() {
        this.status = AgentStatus.AVAILABLE;
    }

    public void markBusy() {
        this.status = AgentStatus.BUSY;
    }
}
