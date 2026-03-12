package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.TopCustomerProjection;
import LoanOriginationSystem.repository.LoanRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerQueryService {

    private final LoanRepository loanRepository;

    public CustomerQueryService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<TopCustomerProjection> getTopCustomers(){
        return loanRepository.findTopCustomers(PageRequest.of(0, 3));
    }
}
