package dao;

import models.Agent;
import java.util.List;

public interface AgentDAO {
    void create(Agent agent);
    Agent findById(int idAgent);
    Agent findByEmail(String email);
    List<Agent> findAll();
    void update(Agent agent);
    void delete(int idAgent);
}