package LoanOriginationSystem.controller;

import LoanOriginationSystem.dto.TopCustomerProjection;
import LoanOriginationSystem.service.CustomerQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerQueryService customerQueryService;

    public CustomerController(CustomerQueryService customerQueryService) {
        this.customerQueryService = customerQueryService;
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopCustomerProjection>> getTopCustomers(){
        return ResponseEntity.ok( customerQueryService.getTopCustomers());
    }
}
