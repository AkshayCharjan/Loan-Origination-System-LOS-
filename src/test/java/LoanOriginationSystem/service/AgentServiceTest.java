package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.AgentRequest;
import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.enums.AgentStatus;
import LoanOriginationSystem.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("AgentService Tests")
class AgentServiceTest {

    private AgentService agentService;

    @Mock
    private AgentRepository agentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        agentService = new AgentService(agentRepository);
    }

    @Test
    @DisplayName("Should save agent with name and email")
    void testCreateAgent_SavesAgent() {
        AgentRequest request = new AgentRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");

        agentService.createAgent(request);

        verify(agentRepository).save(any(Agent.class));
    }

    @Test
    @DisplayName("Should save agent exactly once")
    void testCreateAgent_SavesOnce() {
        AgentRequest request = new AgentRequest();
        request.setName("Alice Brown");
        request.setEmail("alice@example.com");

        agentService.createAgent(request);

        verify(agentRepository, times(1)).save(any(Agent.class));
    }

    @Test
    @DisplayName("Should get all agents from repository")
    void testGetAllAgents_CallsRepository() {
        List<Agent> agents = new ArrayList<>();
        when(agentRepository.findAll()).thenReturn(agents);

        List<Agent> result = agentService.getAllAgents();

        verify(agentRepository).findAll();
        assertEquals(agents, result);
    }

    @Test
    @DisplayName("Should return empty list when no agents exist")
    void testGetAllAgents_EmptyList() {
        when(agentRepository.findAll()).thenReturn(new ArrayList<>());

        List<Agent> result = agentService.getAllAgents();

        assertTrue(result.isEmpty());
        verify(agentRepository).findAll();
    }

    @Test
    @DisplayName("Should return all agents")
    void testGetAllAgents_ReturnsAllAgents() {
        List<Agent> agents = new ArrayList<>();
        agents.add(createTestAgent("Agent 1"));
        agents.add(createTestAgent("Agent 2"));
        agents.add(createTestAgent("Agent 3"));
        
        when(agentRepository.findAll()).thenReturn(agents);

        List<Agent> result = agentService.getAllAgents();

        assertEquals(3, result.size());
        assertEquals(agents, result);
    }

    @Test
    @DisplayName("Should set agent status to AVAILABLE on creation")
    void testCreateAgent_SetAvailableStatus() {
        AgentRequest request = new AgentRequest();
        request.setName("Bob Wilson");
        request.setEmail("bob@example.com");

        agentService.createAgent(request);

        ArgumentCaptor<Agent> agentCaptor = ArgumentCaptor.forClass(Agent.class);
        verify(agentRepository).save(agentCaptor.capture());
        
        Agent savedAgent = agentCaptor.getValue();
        assertEquals(AgentStatus.AVAILABLE, savedAgent.getStatus());
    }

    private Agent createTestAgent(String name) {
        Agent agent = new Agent();
        agent.setName(name);
        agent.setEmail(name.toLowerCase() + "@example.com");
        agent.markAvailable();
        return agent;
    }
}
