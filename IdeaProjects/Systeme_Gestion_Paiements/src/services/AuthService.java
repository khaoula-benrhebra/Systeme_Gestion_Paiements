package services;

import dao.AgentDAO;
import dao.impl.AgentDAOImpl;
import models.Agent;
import models.enums.TypeAgent;
import utils.Validator;

public class AuthService {
    private AgentDAO agentDAO;

    public AuthService() {
        this.agentDAO = new AgentDAOImpl();
    }

    public void createDefaultDirector() {
        try {
            // Vérifier si un directeur existe déjà
            Agent existingDirector = agentDAO.findByEmail("khaoula@entreprise.com");
            if (existingDirector == null) {
                // Créer le directeur par défaut
                Agent directeur = new Agent(
                        0,
                        "Khaoula",
                        "Benrhebra",
                        "khaoula@entreprise.com",
                        "123",
                        TypeAgent.DIRECTEUR
                );

                agentDAO.create(directeur);
                System.out.println("Directeur créé avec succès !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du directeur: " + e.getMessage());
        }
    }

    public Agent login(String email, String password) {
        try {
            // Validation des entrées
            Validator.validateEmail(email);
            Validator.validatePassword(password);

            // Rechercher l'agent par email
            Agent agent = agentDAO.findByEmail(email);

            if (agent == null) {
                throw new IllegalArgumentException("Aucun agent trouvé avec cet email");
            }

            // Vérifier le mot de passe
            if (!agent.getMotDePasse().equals(password)) {
                throw new IllegalArgumentException("Mot de passe incorrect");
            }

            System.out.println("Connexion réussie ! Bienvenue " + agent.getPrenom() + " " + agent.getNom());
            return agent;

        } catch (Exception e) {
            System.out.println("Erreur de connexion: " + e.getMessage());
            return null;
        }
    }
}