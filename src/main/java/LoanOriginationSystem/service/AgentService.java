package LoanOriginationSystem.service;

import LoanOriginationSystem.dto.AgentRequest;
import LoanOriginationSystem.entity.Agent;
import LoanOriginationSystem.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AgentService {

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

        return agent.getId();
    }

    public List<Agent> getAllAgents(){
        return agentRepository.findAll();
    }
}