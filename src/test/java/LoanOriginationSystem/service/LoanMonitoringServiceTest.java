package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.LoanStatusCountProjection;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.enums.ApplicationStatus;
import LoanOriginationSystem.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("LoanMonitoringService Tests")
class LoanMonitoringServiceTest {

    private LoanMonitoringService loanMonitoringService;

    @Mock
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanMonitoringService = new LoanMonitoringService(loanRepository);
    }

    @Test
    @DisplayName("Should get loan status counts")
    void testGetLoanStatusCounts_CallsRepository() {
        List<LoanStatusCountProjection> counts = new ArrayList<>();
        when(loanRepository.countLoansByStatus()).thenReturn(counts);

        List<LoanStatusCountProjection> result = loanMonitoringService.getLoanStatusCounts();

        verify(loanRepository).countLoansByStatus();
        assertEquals(counts, result);
    }

    @Test
    @DisplayName("Should return empty list when no status counts exist")
    void testGetLoanStatusCounts_EmptyList() {
        when(loanRepository.countLoansByStatus()).thenReturn(new ArrayList<>());

        List<LoanStatusCountProjection> result = loanMonitoringService.getLoanStatusCounts();

        assertTrue(result.isEmpty());
        verify(loanRepository).countLoansByStatus();
    }

    @Test
    @DisplayName("Should get loans by status with pagination")
    void testGetLoansByStatus_CallsRepository() {
        int page = 0;
        int size = 10;
        ApplicationStatus status = ApplicationStatus.APPLIED;
        Page<Loan> loanPage = new PageImpl<>(new ArrayList<>());
        
        when(loanRepository.findByApplicationStatus(status, PageRequest.of(page, size)))
            .thenReturn(loanPage);

        Page<Loan> result = loanMonitoringService.getLoansByStatus(status, page, size);

        verify(loanRepository).findByApplicationStatus(status, PageRequest.of(page, size));
        assertEquals(loanPage, result);
    }

    @Test
    @DisplayName("Should get loans by different statuses")
    void testGetLoansByStatus_DifferentStatuses() {
        int page = 0;
        int size = 10;
        Page<Loan> loanPage = new PageImpl<>(new ArrayList<>());
        
        when(loanRepository.findByApplicationStatus(any(ApplicationStatus.class), any()))
            .thenReturn(loanPage);

        loanMonitoringService.getLoansByStatus(ApplicationStatus.APPROVED_BY_SYSTEM, page, size);
        loanMonitoringService.getLoansByStatus(ApplicationStatus.REJECTED_BY_SYSTEM, page, size);
        loanMonitoringService.getLoansByStatus(ApplicationStatus.ASSIGNED_TO_AGENT, page, size);

        verify(loanRepository, times(3)).findByApplicationStatus(any(ApplicationStatus.class), any());
    }

    @Test
    @DisplayName("Should get all loans with pagination")
    void testGetAllLoans_CallsRepository() {
        int page = 0;
        int size = 10;
        Page<Loan> loanPage = new PageImpl<>(new ArrayList<>());
        
        when(loanRepository.findAll(PageRequest.of(page, size))).thenReturn(loanPage);

        Page<Loan> result = loanMonitoringService.getAllLoans(page, size);

        verify(loanRepository).findAll(PageRequest.of(page, size));
        assertEquals(loanPage, result);
    }

    @Test
    @DisplayName("Should handle different page sizes")
    void testGetAllLoans_DifferentPageSizes() {
        Page<Loan> loanPage = new PageImpl<>(new ArrayList<>());
        when(loanRepository.findAll(any(PageRequest.class))).thenReturn(loanPage);

        loanMonitoringService.getAllLoans(0, 5);
        loanMonitoringService.getAllLoans(0, 20);
        loanMonitoringService.getAllLoans(0, 50);

        verify(loanRepository, times(3)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Should handle multiple page requests")
    void testGetAllLoans_MultiplePagination() {
        Page<Loan> page1 = new PageImpl<>(new ArrayList<>());
        Page<Loan> page2 = new PageImpl<>(new ArrayList<>());
        
        when(loanRepository.findAll(PageRequest.of(0, 10))).thenReturn(page1);
        when(loanRepository.findAll(PageRequest.of(1, 10))).thenReturn(page2);

        loanMonitoringService.getAllLoans(0, 10);
        loanMonitoringService.getAllLoans(1, 10);

        verify(loanRepository).findAll(PageRequest.of(0, 10));
        verify(loanRepository).findAll(PageRequest.of(1, 10));
    }
}
