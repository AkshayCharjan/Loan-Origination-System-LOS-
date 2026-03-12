package LoanOriginationSystem.dto;

import LoanOriginationSystem.enums.LoanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class LoanRequestDTO {
    
    @NotBlank(message = "Customer name cannot be blank")
    private String customerName;
    
    @NotBlank(message = "Customer phone cannot be blank")
    private String customerPhone;
    
    @Positive(message = "Loan amount must be positive")
    private int loanAmount;
    
    @NotNull(message = "Loan type cannot be null")
    private LoanType loanType;

    public LoanRequestDTO(String customerName, String customerPhone, int loanAmount, LoanType loanType) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.loanAmount = loanAmount;
        this.loanType = loanType;
    }
}
