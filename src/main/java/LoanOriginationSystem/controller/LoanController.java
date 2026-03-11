package LoanOriginationSystem.controller;

import LoanOriginationSystem.dto.LoanRequestDTO;
import LoanOriginationSystem.dto.LoanStatusCountProjection;
import LoanOriginationSystem.service.LoanMonitoringService;
import LoanOriginationSystem.service.LoanRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanRegistrationService loanRegistrationService;
    private final LoanMonitoringService loanMonitoringService;

    public LoanController(
            LoanRegistrationService loanRegistrationService,
            LoanMonitoringService loanMonitoringService) {
        this.loanRegistrationService = loanRegistrationService;
        this.loanMonitoringService = loanMonitoringService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> submitLoan(
            @RequestBody LoanRequestDTO loanRequestDTO) {

        UUID loanId = loanRegistrationService.registerLoan(loanRequestDTO);
        return ResponseEntity.ok(
                Map.of(
                        "message", "Loan application submitted",
                        "loanId", loanId
                )
        );
    }

    @GetMapping("/status-count")
    public ResponseEntity<List<LoanStatusCountProjection>> getLoanStatusCounts(){

        return ResponseEntity.ok(
                loanMonitoringService.getLoanStatusCounts()
        );
    }
}
