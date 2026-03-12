package LoanOriginationSystem.service.notification;

import java.util.UUID;

public interface NotificationService {
    void notifyAgent(UUID agentId, UUID loanId);
    void notifyManager(UUID managerId, UUID loanId);
    void notifyCustomer(String phone, UUID loanId);
}
