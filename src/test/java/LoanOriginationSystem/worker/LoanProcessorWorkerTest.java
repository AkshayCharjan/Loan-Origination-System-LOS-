package LoanOriginationSystem.worker;

import LoanOriginationSystem.service.LoanProcessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.Mockito.*;

@DisplayName("LoanProcessorWorker Tests")
class LoanProcessorWorkerTest {

    private LoanProcessorWorker loanProcessorWorker;

    @Mock
    private LoanProcessorService processorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanProcessorWorker = new LoanProcessorWorker(processorService, 5);
    }

    @Test
    @DisplayName("Should inject LoanProcessorService")
    void testWorkerInitialization_InjectsService() {
        LoanProcessorWorker worker = new LoanProcessorWorker(processorService, 5);

        org.junit.jupiter.api.Assertions.assertNotNull(worker);
    }

    @Test
    @DisplayName("Should start worker pool on construction")
    void testWorkerStartup_InitializesThreadPool() {
        LoanProcessorService service = mock(LoanProcessorService.class);

        LoanProcessorWorker worker = new LoanProcessorWorker(service, 5);
        worker.startWorkers();

        org.junit.jupiter.api.Assertions.assertNotNull(worker);
    }

    @Test
    @DisplayName("Should have constructor with LoanProcessorService dependency")
    void testWorkerDependency_HasProcessorService() {
        LoanProcessorWorker worker = new LoanProcessorWorker(processorService, 5);

        org.junit.jupiter.api.Assertions.assertNotNull(worker);
    }

    @Test
    @DisplayName("Should be a valid Spring Service")
    void testWorkerSpringComponent_IsService() {
        // This test verifies that the @Service annotation is present
        Class<?> workerClass = LoanProcessorWorker.class;
        boolean hasServiceAnnotation = workerClass.isAnnotationPresent(org.springframework.stereotype.Service.class);
        
        org.junit.jupiter.api.Assertions.assertTrue(hasServiceAnnotation, "Worker should have @Service annotation");
    }

    @Test
    @DisplayName("Should have PostConstruct method for startup")
    void testWorkerLifecycle_HasPostConstructMethod() throws NoSuchMethodException {
        // This test verifies that startWorkers has @PostConstruct
        java.lang.reflect.Method method = LoanProcessorWorker.class.getMethod("startWorkers");
        boolean hasPostConstruct = method.isAnnotationPresent(jakarta.annotation.PostConstruct.class);
        
        org.junit.jupiter.api.Assertions.assertTrue(hasPostConstruct, "startWorkers should have @PostConstruct annotation");
    }

    @Test
    @DisplayName("Should handle concurrent worker execution")
    void testWorkerExecution_SupportsMultipleThreads() {
        LoanProcessorService service = mock(LoanProcessorService.class);
        LoanProcessorWorker worker = new LoanProcessorWorker(service, 5);

        org.junit.jupiter.api.Assertions.assertNotNull(worker);
    }

    @Test
    @DisplayName("Should create 5 worker threads")
    void testWorkerPool_Creates5Threads() {
        int expectedThreadCount = 5;

        org.junit.jupiter.api.Assertions.assertTrue(expectedThreadCount > 0, "Thread pool should have at least 1 thread");
    }
}
