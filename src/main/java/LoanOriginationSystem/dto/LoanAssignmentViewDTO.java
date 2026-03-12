package LoanOriginationSystem.dto;

import LoanOriginationSystem.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class LoanAssignmentViewDTO {

    private UUID loanId;
    private String customerName;
    private int loanAmount;
    private ApplicationStatus applicationStatus;

    private UUID agentId;
    private String agentName;
    private String agentEmail;
}
