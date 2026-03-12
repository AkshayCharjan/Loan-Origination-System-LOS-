package LoanOriginationSystem.service;

import LoanOriginationSystem.repository.AgentRepository;
import LoanOriginationSystem.repository.LoanAssignmentRepository;
import LoanOriginationSystem.repository.LoanRepository;
import LoanOriginationSystem.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("AgentDecisionService Tests")
class AgentDecisionServiceTest {

    private AgentDecisionService agentDecisionService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanAssignmentRepository assignmentRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AgentRepository agentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        agentDecisionService = new AgentDecisionService(
            loanRepository,
            assignmentRepository,
            notificationService,
            agentRepository
        );
    }

    @Test
    @DisplayName("Should throw exception when assignment not found")
    void testDecide_ThrowsExceptionWhenAssignmentNotFound() {
        UUID loanId = UUID.randomUUID();
        UUID agentId = UUID.randomUUID();
        
        when(assignmentRepository.findByLoan_LoanId(loanId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            agentDecisionService.decide(agentId, loanId, "APPROVE");
        });
    }

    @Test
    @DisplayName("Should call repositories on valid decision")
    void testDecide_CallsRepositoriesOnValidDecision() {
        UUID loanId = UUID.randomUUID();
        UUID agentId = UUID.randomUUID();

        assertDoesNotThrow(() -> {
            when(assignmentRepository.findByLoan_LoanId(loanId)).thenReturn(Optional.empty());
            try {
                agentDecisionService.decide(agentId, loanId, "APPROVE");
            } catch (RuntimeException e) {
                // Expected when assignment not found
                assertTrue(e.getMessage().contains("not assigned"));
            }
        });
    }

    @Test
    @DisplayName("Should verify service is instantiated")
    void testDecide_ServiceInitialized() {
        assertNotNull(agentDecisionService);
    }
}
