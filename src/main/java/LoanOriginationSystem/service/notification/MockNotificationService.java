package LoanOriginationSystem.service.notification;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MockNotificationService implements NotificationService {

    @Override
    public void notifyAgent(UUID agentId, UUID loanId) {
        System.out.println("Push notification: Agent " + agentId + ". You have been assigned loan:" + loanId);
    }

    @Override
    public void notifyManager(UUID managerId, UUID loanId) {
        System.out.println("Push notification: Manager " + managerId + ". Your agent has been assigned loan " + loanId);
    }

    @Override
    public void notifyCustomer(String phone, UUID loanId) {
        System.out.println("SMS: Customer " + phone + ". Your loan has been approved: " + loanId);
    }
}
