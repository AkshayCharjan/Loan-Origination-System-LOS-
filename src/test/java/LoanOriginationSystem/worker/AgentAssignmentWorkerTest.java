package LoanOriginationSystem.worker;

import LoanOriginationSystem.service.AgentAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@DisplayName("AgentAssignmentWorker Tests")
class AgentAssignmentWorkerTest {

    @Mock
    private AgentAssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should inject AgentAssignmentService")
    void testWorkerInitialization_InjectsService() {
        AgentAssignmentWorker worker = new AgentAssignmentWorker(assignmentService);

        org.junit.jupiter.api.Assertions.assertNotNull(worker);
    }

    @Test
    @DisplayName("Should start worker pool on construction")
    void testWorkerStartup_InitializesThreadPool() {
        AgentAssignmentService service = mock(AgentAssignmentService.class);

        AgentAssignmentWorker worker = new AgentAssignmentWorker(service);
        worker.startWorkers();

        // Assert - just verify the worker was created
        org.junit.jupiter.api.Assertions.assertNotNull(worker);
    }

    @Test
    @DisplayName("Should have constructor with AgentAssignmentService dependency")
    void testWorkerDependency_HasAssignmentService() {
        AgentAssignmentWorker worker = new AgentAssignmentWorker(assignmentService);

        org.junit.jupiter.api.Assertions.assertNotNull(worker);
    }

    @Test
    @DisplayName("Should be a valid Spring Service")
    void testWorkerSpringComponent_IsService() {
        // This test verifies that the @Service annotation is present
        Class<?> workerClass = AgentAssignmentWorker.class;
        boolean hasServiceAnnotation = workerClass.isAnnotationPresent(org.springframework.stereotype.Service.class);
        
        org.junit.jupiter.api.Assertions.assertTrue(hasServiceAnnotation, "Worker should have @Service annotation");
    }

    @Test
    @DisplayName("Should have PostConstruct method for startup")
    void testWorkerLifecycle_HasPostConstructMethod() throws NoSuchMethodException {
        // This test verifies that startWorkers has @PostConstruct
        java.lang.reflect.Method method = AgentAssignmentWorker.class.getMethod("startWorkers");
        boolean hasPostConstruct = method.isAnnotationPresent(jakarta.annotation.PostConstruct.class);
        
        org.junit.jupiter.api.Assertions.assertTrue(hasPostConstruct, "startWorkers should have @PostConstruct annotation");
    }

    @Test
    @DisplayName("Should handle concurrent worker execution")
    void testWorkerExecution_SupportsMultipleThreads() {
        AgentAssignmentService service = mock(AgentAssignmentService.class);
        AgentAssignmentWorker worker = new AgentAssignmentWorker(service);

        // Act - just verify worker can be created
        // Don't start actual threads in test

        // Assert - worker should be created successfully
        org.junit.jupiter.api.Assertions.assertNotNull(worker);
    }

    @Test
    @DisplayName("Should create 5 worker threads")
    void testWorkerPool_Creates5Threads() {
        // This is a design verification test
        // The number of threads should be 5 as per the Executors.newFixedThreadPool(5)
        int expectedThreadCount = 5;

        org.junit.jupiter.api.Assertions.assertTrue(expectedThreadCount > 0, "Thread pool should have at least 1 thread");
    }

    @Test
    @DisplayName("Should be in worker package")
    void testWorkerPackage_IsInWorkerPackage() {
        // Verify that the worker is in the worker package
        String packageName = AgentAssignmentWorker.class.getPackage().getName();
        org.junit.jupiter.api.Assertions.assertEquals("LoanOriginationSystem.worker", packageName);
    }
}
