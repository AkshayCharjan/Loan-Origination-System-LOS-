package LoanOriginationSystem.entity;

import LoanOriginationSystem.enums.AgentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    @Setter
    private AgentStatus status;
}
