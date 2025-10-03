package services;

import dao.AgentDAO;
import dao.impl.AgentDAOImpl;
import dao.DepartementDAO;
import dao.impl.DepartementDAOImpl;
import models.Agent;
import models.Departement;
import models.enums.TypeAgent;
import utils.Validator;

public class AuthService {
    private AgentDAO agentDAO;
    private DepartementDAO departementDAO;

    public AuthService() {
        this.agentDAO = new AgentDAOImpl();
        this.departementDAO = new DepartementDAOImpl();
    }

    public void createDefaultDirector() {
        try {

            Agent existingDirector = agentDAO.findByEmail("khaoula@entreprise.com");

            if (existingDirector == null) {

                Departement deptDirecteur = departementDAO.findByNom("Direction Générale");
                if (deptDirecteur == null) {
                    deptDirecteur = new Departement();
                    deptDirecteur.setNom("Direction Générale");
                    departementDAO.create(deptDirecteur);
                }

                Departement deptCree = departementDAO.findByNom("Direction Générale");

                Agent directeur = new Agent(
                        0,
                        "Khaoula",
                        "Benrhebra",
                        "khaoula@entreprise.com",
                        "test123",
                        TypeAgent.DIRECTEUR
                );
                directeur.setDepartement(deptCree);

                agentDAO.create(directeur);
                System.out.println("Directeur et département créés avec succès !");
            }

        } catch (Exception e) {
            System.out.println("Erreur création directeur: " + e.getMessage());
        }
    }

    public Agent login(String email, String password) {
        try {
            Validator.validateEmail(email);
            Validator.validatePassword(password);

            Agent agent = agentDAO.findByEmail(email);

            if (agent == null) {
                throw new IllegalArgumentException("Aucun agent trouvé avec cet email");
            }

            if (!agent.getMotDePasse().equals(password)) {
                throw new IllegalArgumentException("Mot de passe incorrect");
            }

            System.out.println(" Connexion réussie ! Bienvenue " + agent.getPrenom() + " " + agent.getNom());
            return agent;

        } catch (Exception e) {
            System.out.println("Erreur connexion: " + e.getMessage());
            return null;
        }
    }
}