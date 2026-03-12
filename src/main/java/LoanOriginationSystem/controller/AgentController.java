package LoanOriginationSystem.controller;

import LoanOriginationSystem.dto.AgentDecisionRequest;
import LoanOriginationSystem.service.AgentDecisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {

    private final AgentDecisionService decisionService;

    public AgentController(AgentDecisionService decisionService) {
        this.decisionService = decisionService;
    }

    @PutMapping("/{agentId}/loans/{loanId}/decision")
    public ResponseEntity<String> decideLoan(
            @PathVariable UUID agentId,
            @PathVariable UUID loanId,
            @RequestBody AgentDecisionRequest request){

        decisionService.decide(agentId, loanId, request.getDecision());
        return ResponseEntity.ok("Decision recorded");
    }
}
