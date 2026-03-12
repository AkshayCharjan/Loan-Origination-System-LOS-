package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.AgentRequest;
import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.repository.AgentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AgentService {
    private static final Logger log = LoggerFactory.getLogger(AgentService.class);

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public UUID createAgent(AgentRequest request){

        Agent agent = new Agent();
        agent.setName(request.getName());
        agent.setEmail(request.getEmail());
        agent.markAvailable();
        agentRepository.save(agent);

        log.info("Agent created: agentId={}, name={}, email={}", agent.getId(), agent.getName(),
                agent.getEmail());

        return agent.getId();
    }

    public List<Agent> getAllAgents(){
        return agentRepository.findAll();
    }
}