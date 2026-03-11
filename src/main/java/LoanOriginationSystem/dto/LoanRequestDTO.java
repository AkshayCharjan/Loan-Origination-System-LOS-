package LoanOriginationSystem.dto;

import LoanOriginationSystem.LoanType;
import lombok.Getter;

@Getter
public class LoanRequestDTO {
    private String customerName;
    private String customerPhone;
    private int loanAmount;
    private LoanType loanType;

    public LoanRequestDTO(String customerName, String customerPhone, int loanAmount, LoanType loanType) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.loanAmount = loanAmount;
        this.loanType = loanType;
    }
}
