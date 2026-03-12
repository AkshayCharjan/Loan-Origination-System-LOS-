package LoanOriginationSystem.service.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MockNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(MockNotificationService.class);

    @Override
    public void notifyAgent(UUID agentId, UUID loanId) {
        log.info("Notification sent to agent: agentId={}, loanId={}", agentId, loanId);
    }

    @Override
    public void notifyManager(UUID managerId, UUID loanId) {
        log.info("Notification sent to manager: managerId={}, loanId={}", managerId, loanId);
    }

    @Override
    public void notifyCustomer(String phone, UUID loanId) {
        log.info("SMS sent to customer: phone={}, loanId={}", phone, loanId);
    }
}
