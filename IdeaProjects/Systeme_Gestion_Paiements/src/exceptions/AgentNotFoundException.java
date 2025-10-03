package exceptions;

public class AgentNotFoundException extends RuntimeException {
    public AgentNotFoundException(String message) {
        super(message);
    }

    public AgentNotFoundException(int agentId) {
        super("Agent avec ID " + agentId + " non trouvé");
    }
}