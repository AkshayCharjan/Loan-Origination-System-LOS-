package LoanOriginationSystem.controller;

import LoanOriginationSystem.dto.AgentDecisionRequest;
import LoanOriginationSystem.dto.AgentRequest;
import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.service.AgentDecisionService;
import LoanOriginationSystem.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {

    private final AgentDecisionService decisionService;
    private final AgentService agentService;

    public AgentController(AgentDecisionService decisionService, AgentService agentService) {
        this.decisionService = decisionService;
        this.agentService = agentService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAgent(@RequestBody AgentRequest request){
        UUID agentId = agentService.createAgent(request);
        return ResponseEntity.ok(
                Map.of( "message", "Agent created",
                        "agentId", agentId)
        );
    }

    @GetMapping
    public ResponseEntity<List<Agent>> getAllAgents(){
        return ResponseEntity.ok(agentService.getAllAgents());
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
