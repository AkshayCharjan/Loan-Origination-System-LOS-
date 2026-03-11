package LoanOriginationSystem.dto;

import LoanOriginationSystem.enums.ApplicationStatus;

public interface LoanStatusCountProjection {

    ApplicationStatus getApplicationStatus();
    Long getCount();
}
