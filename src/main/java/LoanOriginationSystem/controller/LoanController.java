package LoanOriginationSystem.controller;

import LoanOriginationSystem.dto.LoanRequestDTO;
import LoanOriginationSystem.dto.LoanStatusCountProjection;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.enums.ApplicationStatus;
import LoanOriginationSystem.service.LoanMonitoringService;
import LoanOriginationSystem.service.LoanRegistrationService;
import org.springframework.data.domain.Page;
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
        return ResponseEntity.ok( Map.of(
                        "message", "Loan application submitted",
                        "loanId", loanId)
        );
    }

    @GetMapping("/status-count")
    public ResponseEntity<List<LoanStatusCountProjection>> getLoanStatusCounts(){

        return ResponseEntity.ok(
                loanMonitoringService.getLoanStatusCounts()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Loan>> getAllLoans(
            @RequestParam int page,
            @RequestParam int size){
        return ResponseEntity.ok(loanMonitoringService.getAllLoans(page, size));
    }

    @GetMapping
    public ResponseEntity<Page<Loan>> getLoansByStatus(
            @RequestParam ApplicationStatus status,
            @RequestParam int page,
            @RequestParam int size){

        return ResponseEntity.ok(loanMonitoringService.getLoansByStatus(status, page, size)
        );
    }
}
