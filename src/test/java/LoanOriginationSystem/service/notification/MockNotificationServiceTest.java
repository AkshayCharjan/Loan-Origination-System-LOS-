package LoanOriginationSystem.service.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MockNotificationService Tests")
class MockNotificationServiceTest {

    private MockNotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new MockNotificationService();
    }

    @Test
    @DisplayName("Should create notification service instance")
    void testMockNotificationService_CreateInstance() {
        MockNotificationService service = new MockNotificationService();

        assertNotNull(service);
    }

    @Test
    @DisplayName("Should not throw exception when notifying agent")
    void testNotifyAgent_DoesNotThrow() {
        UUID agentId = UUID.randomUUID();
        UUID loanId = UUID.randomUUID();

        assertDoesNotThrow(() -> notificationService.notifyAgent(agentId, loanId));
    }

    @Test
    @DisplayName("Should not throw exception when notifying manager")
    void testNotifyManager_DoesNotThrow() {
        UUID managerId = UUID.randomUUID();
        UUID loanId = UUID.randomUUID();

        assertDoesNotThrow(() -> notificationService.notifyManager(managerId, loanId));
    }

    @Test
    @DisplayName("Should not throw exception when notifying customer")
    void testNotifyCustomer_DoesNotThrow() {
        String phone = "1234567890";
        UUID loanId = UUID.randomUUID();

        assertDoesNotThrow(() -> notificationService.notifyCustomer(phone, loanId));
    }

    @Test
    @DisplayName("Should handle multiple agent notifications")
    void testNotifyAgent_MultipleNotifications() {
        UUID agentId1 = UUID.randomUUID();
        UUID agentId2 = UUID.randomUUID();
        UUID loanId = UUID.randomUUID();

        assertDoesNotThrow(() -> {
            notificationService.notifyAgent(agentId1, loanId);
            notificationService.notifyAgent(agentId2, loanId);
        });
    }

    @Test
    @DisplayName("Should implement NotificationService interface")
    void testMockNotificationService_ImplementsInterface() {
        boolean isInstance = notificationService instanceof NotificationService;

        assertTrue(isInstance);
    }
}
