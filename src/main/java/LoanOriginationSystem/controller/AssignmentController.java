package LoanOriginationSystem.controller;

import LoanOriginationSystem.dto.LoanAssignmentViewDTO;
import LoanOriginationSystem.service.LoanViewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
public class AssignmentController {

    private final LoanViewService loanViewService;

    public AssignmentController(LoanViewService loanViewService) {
        this.loanViewService = loanViewService;
    }

    @GetMapping()
    public List<LoanAssignmentViewDTO> getAssignments(){
        return loanViewService.getLoanAssignments();
    }
}
