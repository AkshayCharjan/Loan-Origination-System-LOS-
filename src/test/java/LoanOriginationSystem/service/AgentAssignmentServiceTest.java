package LoanOriginationSystem.service;

import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.entity.Loan;
import LoanOriginationSystem.enums.AgentStatus;
import LoanOriginationSystem.enums.LoanType;
import LoanOriginationSystem.repository.AgentRepository;
import LoanOriginationSystem.repository.LoanAssignmentRepository;
import LoanOriginationSystem.repository.LoanRepository;
import LoanOriginationSystem.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("AgentAssignmentService Tests")
class AgentAssignmentServiceTest {

    private AgentAssignmentService agentAssignmentService;

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanAssignmentRepository loanAssignmentRepository;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        agentAssignmentService = new AgentAssignmentService(
            agentRepository,
            loanRepository,
            loanAssignmentRepository,
            notificationService
        );
    }

    @Test
    @DisplayName("Should fetch loans for review")
    void testAssignBatch_FetchesLoansForReview() {

        List<Loan> loansForReview = new ArrayList<>();
        when(loanRepository.fetchLoansForReview(10)).thenReturn(loansForReview);

        agentAssignmentService.assignBatch();

        verify(loanRepository).fetchLoansForReview(10);
    }

    @Test
    @DisplayName("Should handle empty loans list")
    void testAssignBatch_EmptyLoansList() {
        List<Loan> emptyLoans = new ArrayList<>();
        when(loanRepository.fetchLoansForReview(10)).thenReturn(emptyLoans);

        agentAssignmentService.assignBatch();

        verify(loanRepository).fetchLoansForReview(10);
        verify(agentRepository, never()).findFirstByStatus(any());
    }

    @Test
    @DisplayName("Should find available agent when loans exist")
    void testAssignBatch_FindsAvailableAgent() {
        Loan loan = new Loan("Test Customer", "9876543210", 500000, LoanType.PERSONAL);
        List<Loan> loans = List.of(loan);
        
        Agent agent = new Agent();
        agent.setName("Test Agent");
        agent.setEmail("agent@test.com");
        agent.markAvailable();
        
        when(loanRepository.fetchLoansForReview(10)).thenReturn(loans);
        when(agentRepository.findFirstByStatus(AgentStatus.AVAILABLE)).thenReturn(Optional.of(agent));

        agentAssignmentService.assignBatch();

        verify(agentRepository).findFirstByStatus(AgentStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Should handle no available agents")
    void testAssignBatch_NoAvailableAgents() {
        Loan loan = new Loan("Test Customer", "9876543210", 500000, LoanType.PERSONAL);
        List<Loan> loans = List.of(loan);
        
        when(loanRepository.fetchLoansForReview(10)).thenReturn(loans);
        when(agentRepository.findFirstByStatus(AgentStatus.AVAILABLE)).thenReturn(Optional.empty());

        agentAssignmentService.assignBatch();

        verify(agentRepository).findFirstByStatus(AgentStatus.AVAILABLE);
        verify(loanAssignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create assignment when agent found")
    void testAssignBatch_CreatesAssignment() {
        Loan loan = new Loan("Test Customer", "9876543210", 500000, LoanType.PERSONAL);
        loan.sendForReview();
        List<Loan> loans = List.of(loan);
        
        Agent agent = new Agent();
        agent.setName("Test Agent");
        agent.setEmail("agent@test.com");
        agent.markAvailable();
        
        when(loanRepository.fetchLoansForReview(10)).thenReturn(loans);
        when(agentRepository.findFirstByStatus(AgentStatus.AVAILABLE)).thenReturn(Optional.of(agent));

        agentAssignmentService.assignBatch();

        verify(loanAssignmentRepository).save(any());
    }

    @Test
    @DisplayName("Should call service with correct batch size")
    void testAssignBatch_CorrectBatchSize() {
        when(loanRepository.fetchLoansForReview(10)).thenReturn(new ArrayList<>());

        agentAssignmentService.assignBatch();

        verify(loanRepository).fetchLoansForReview(10);
    }
}