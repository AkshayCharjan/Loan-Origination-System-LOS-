package LoanOriginationSystem.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("LoanProcessorService Tests")
class LoanProcessorServiceTest {

    private LoanProcessorService loanProcessorService;

    @Mock
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanProcessorService = new LoanProcessorService(loanRepository, 0);
    }

    @Test
    @DisplayName("Should process batch and fetch loans for processing")
    void testProcessBatch_FetchesLoans() {
        List<Loan> loans = new ArrayList<>();
        when(loanRepository.fetchLoansForProcessing(10)).thenReturn(loans);

        loanProcessorService.processBatch();

        verify(loanRepository).fetchLoansForProcessing(10);
    }

    @Test
    @DisplayName("Should handle empty loan list")
    void testProcessBatch_EmptyLoanList() {
        List<Loan> emptyLoans = new ArrayList<>();
        when(loanRepository.fetchLoansForProcessing(10)).thenReturn(emptyLoans);

        loanProcessorService.processBatch();

        verify(loanRepository).fetchLoansForProcessing(10);
        verify(loanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should save loans after processing")
    void testProcessBatch_SavesProcessedLoans() throws InterruptedException {
        Loan loan1 = createTestLoan("Customer 1");
        Loan loan2 = createTestLoan("Customer 2");
        List<Loan> loans = List.of(loan1, loan2);
        
        when(loanRepository.fetchLoansForProcessing(10)).thenReturn(loans);

        loanProcessorService.processBatch();

        verify(loanRepository, times(2)).save(any(Loan.class));
    }

    @Test
    @DisplayName("Should change loan status during processing")
    void testProcessBatch_ChangesLoanStatus() {
        Loan loan = createTestLoan("Test Customer");
        assertEquals(ApplicationStatus.APPLIED, loan.getApplicationStatus());
        
        List<Loan> loans = List.of(loan);
        when(loanRepository.fetchLoansForProcessing(10)).thenReturn(loans);

        loanProcessorService.processBatch();

        assertNotEquals(ApplicationStatus.APPLIED, loan.getApplicationStatus());
        assertTrue(loan.getApplicationStatus() == ApplicationStatus.APPROVED_BY_SYSTEM ||
                   loan.getApplicationStatus() == ApplicationStatus.REJECTED_BY_SYSTEM ||
                   loan.getApplicationStatus() == ApplicationStatus.UNDER_REVIEW);
    }

    @Test
    @DisplayName("Should process multiple loans in batch")
    void testProcessBatch_ProcessesMultipleLoans() {
        List<Loan> loans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            loans.add(createTestLoan("Customer " + i));
        }
        when(loanRepository.fetchLoansForProcessing(10)).thenReturn(loans);

        loanProcessorService.processBatch();

        assertEquals(5, loans.size());
        verify(loanRepository, times(5)).save(any(Loan.class));
    }

    @Test
    @DisplayName("Should handle loan with various loan types")
    void testProcessBatch_HandlesVariousLoanTypes() {
        LoanType[] types = {LoanType.PERSONAL, LoanType.HOME, LoanType.AUTO, LoanType.BUSINESS};
        List<Loan> loans = new ArrayList<>();
        
        for (LoanType type : types) {
            Loan loan = new Loan("Test Customer", "9876543210", 100000, type);
            loans.add(loan);
        }
        
        when(loanRepository.fetchLoansForProcessing(10)).thenReturn(loans);

        loanProcessorService.processBatch();

        verify(loanRepository, times(4)).save(any(Loan.class));
        for (Loan loan : loans) {
            assertNotNull(loan.getApplicationStatus());
        }
    }

    // Helper method to create test loan
    private Loan createTestLoan(String customerName) {
        return new Loan(customerName, "9876543210", 500000, LoanType.PERSONAL);
    }
}
