package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.LoanRequestDTO;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.enums.ApplicationStatus;
import LoanOriginationSystem.enums.LoanType;
import LoanOriginationSystem.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("LoanRegistrationService Tests")
class LoanRegistrationServiceTest {

    private LoanRegistrationService loanRegistrationService;

    @Mock
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanRegistrationService = new LoanRegistrationService(loanRepository);
    }

    @Test
    @DisplayName("Should register loan with customer details")
    void testRegisterLoan_SavesWithCustomerDetails() {
        LoanRequestDTO dto = new LoanRequestDTO("John Doe", "9876543210", 500000, LoanType.PERSONAL);

        loanRegistrationService.registerLoan(dto);

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCaptor.capture());
        
        Loan savedLoan = loanCaptor.getValue();
        assertEquals("John Doe", savedLoan.getCustomerName());
        assertEquals("9876543210", savedLoan.getCustomerPhone());
        assertEquals(500000, savedLoan.getLoanAmount());
        assertEquals(LoanType.PERSONAL, savedLoan.getLoanType());
    }

    @Test
    @DisplayName("Should set loan status to APPLIED on registration")
    void testRegisterLoan_InitialStatusIsApplied() {
        LoanRequestDTO dto = new LoanRequestDTO("Jane Smith", "1234567890", 1000000, LoanType.HOME);

        loanRegistrationService.registerLoan(dto);

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCaptor.capture());
        
        Loan savedLoan = loanCaptor.getValue();
        assertEquals(ApplicationStatus.APPLIED, savedLoan.getApplicationStatus());
    }

    @Test
    @DisplayName("Should save loan exactly once")
    void testRegisterLoan_SavesOneLoan() {
        LoanRequestDTO dto = new LoanRequestDTO("Test User", "5555555555", 250000, LoanType.AUTO);

        loanRegistrationService.registerLoan(dto);

        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    @DisplayName("Should handle different loan types")
    void testRegisterLoan_DifferentLoanTypes() {
        LoanRequestDTO personalLoan = new LoanRequestDTO("User1", "1111111111", 100000, LoanType.PERSONAL);
        LoanRequestDTO homeLoan = new LoanRequestDTO("User2", "2222222222", 5000000, LoanType.HOME);
        LoanRequestDTO autoLoan = new LoanRequestDTO("User3", "3333333333", 500000, LoanType.AUTO);
        LoanRequestDTO businessLoan = new LoanRequestDTO("User4", "4444444444", 2000000, LoanType.BUSINESS);

        loanRegistrationService.registerLoan(personalLoan);
        loanRegistrationService.registerLoan(homeLoan);
        loanRegistrationService.registerLoan(autoLoan);
        loanRegistrationService.registerLoan(businessLoan);

        verify(loanRepository, times(4)).save(any(Loan.class));
    }

    @Test
    @DisplayName("Should handle multiple loan registrations")
    void testRegisterLoan_HandleMultipleRegistrations() {
        LoanRequestDTO dto1 = new LoanRequestDTO("Customer 1", "1111111111", 100000, LoanType.PERSONAL);
        LoanRequestDTO dto2 = new LoanRequestDTO("Customer 2", "2222222222", 200000, LoanType.HOME);

        loanRegistrationService.registerLoan(dto1);
        loanRegistrationService.registerLoan(dto2);

        verify(loanRepository, times(2)).save(any(Loan.class));
    }
}
