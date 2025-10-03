package services;

import dao.AgentDAO;
import dao.impl.AgentDAOImpl;
import dao.DepartementDAO;
import dao.impl.DepartementDAOImpl;
import models.Agent;
import models.Departement;
import models.enums.TypeAgent;
import utils.Validator;
import exceptions.AgentNotFoundException;
import exceptions.EmailAlreadyExistsException;
import exceptions.InvalidAgentDataException;
import exceptions.UnauthorizedActionException;

import java.util.List;
import java.util.Scanner;

public class AgentService {
    private AgentDAO agentDAO;
    private DepartementDAO departementDAO;
    private Scanner scanner;

    public AgentService() {
        this.agentDAO = new AgentDAOImpl();
        this.departementDAO = new DepartementDAOImpl();
        this.scanner = new Scanner(System.in);
    }

    public void creerAgent(Agent agentConnecte) {
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();

        // Validation
        Validator.validateNotEmpty(nom, "Nom");
        Validator.validateNotEmpty(prenom, "Prénom");
        Validator.validateEmail(email);
        Validator.validatePassword(motDePasse);

        // Vérifier si l'email existe déjà
        if (agentDAO.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException(email);
        }

        // Déterminer type et département
        TypeAgent typeAgent;
        Departement departement;

        if (agentConnecte.getTypeAgent() == TypeAgent.DIRECTEUR) {
            // Directeur crée un RESPONSABLE
            typeAgent = TypeAgent.RESPONSABLE_DEPARTEMENT;
            departement = choisirDepartement();
            if (departement == null) {
                throw new InvalidAgentDataException("Département invalide");
            }

        } else {
            // Responsable crée OUVRIER ou STAGIAIRE
            typeAgent = choisirTypeAgent();
            departement = agentConnecte.getDepartement();

            if (departement == null) {
                throw new UnauthorizedActionException("Vous devez être affecté à un département pour créer un agent");
            }
        }

        Agent nouvelAgent = new Agent(0, nom, prenom, email, motDePasse, typeAgent);
        nouvelAgent.setDepartement(departement);
        agentDAO.create(nouvelAgent);

        System.out.println("Agent créé avec succès");
    }


    public void modifierAgent(Agent agentConnecte) {
        System.out.print("ID de l'agent à modifier: ");
        int idAgent = scanner.nextInt();
        scanner.nextLine();

        Agent agent = agentDAO.findById(idAgent);
        if (agent == null) {
            throw new AgentNotFoundException(idAgent);
        }

        if (agentConnecte.getTypeAgent() == TypeAgent.DIRECTEUR) {
            if (agent.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT) {
                throw new UnauthorizedActionException("Vous ne pouvez modifier que les responsables de département");
            }
        }
        else if (agentConnecte.getTypeAgent() == TypeAgent.RESPONSABLE_DEPARTEMENT) {
            if (agent.getTypeAgent() != TypeAgent.OUVRIER && agent.getTypeAgent() != TypeAgent.STAGIAIRE) {
                throw new UnauthorizedActionException("Vous ne pouvez modifier que les ouvriers et stagiaires");
            }
            if (agent.getDepartement() == null ||
                    agentConnecte.getDepartement() == null ||
                    agent.getDepartement().getId() != agentConnecte.getDepartement().getId()) {
                throw new UnauthorizedActionException("Vous ne pouvez modifier que les agents de votre département");
            }
        }

        System.out.print("Nouveau nom (" + agent.getNom() + "): ");
        String nouveauNom = scanner.nextLine();
        System.out.print("Nouveau prénom (" + agent.getPrenom() + "): ");
        String nouveauPrenom = scanner.nextLine();
        System.out.print("Nouvel email (" + agent.getEmail() + "): ");
        String nouvelEmail = scanner.nextLine();
        System.out.print("Nouveau mot de passe: ");
        String nouveauMdp = scanner.nextLine();

        if (!nouveauNom.isEmpty()) {
            Validator.validateNotEmpty(nouveauNom, "Nom");
            agent.setNom(nouveauNom);
        }
        if (!nouveauPrenom.isEmpty()) {
            Validator.validateNotEmpty(nouveauPrenom, "Prénom");
            agent.setPrenom(nouveauPrenom);
        }
        if (!nouvelEmail.isEmpty()) {
            Validator.validateEmail(nouvelEmail);
            Agent existingAgent = agentDAO.findByEmail(nouvelEmail);
            if (existingAgent != null && existingAgent.getIdAgent() != idAgent) {
                throw new EmailAlreadyExistsException(nouvelEmail);
            }
            agent.setEmail(nouvelEmail);
        }
        if (!nouveauMdp.isEmpty()) {
            Validator.validatePassword(nouveauMdp);
            agent.setMotDePasse(nouveauMdp);
        }

        if (agentConnecte.getTypeAgent() == TypeAgent.DIRECTEUR) {
            System.out.println("Nouveau type:");
            System.out.println("1. OUVRIER");
            System.out.println("2. STAGIAIRE");
            System.out.println("3. RESPONSABLE_DEPARTEMENT");
            System.out.print("Choix: ");
            int choixType = scanner.nextInt();
            scanner.nextLine();

            if (choixType == 1) agent.setTypeAgent(TypeAgent.OUVRIER);
            else if (choixType == 2) agent.setTypeAgent(TypeAgent.STAGIAIRE);
            else if (choixType == 3) agent.setTypeAgent(TypeAgent.RESPONSABLE_DEPARTEMENT);
            else {
                throw new InvalidAgentDataException("Type d'agent invalide");
            }

            Departement nouveauDept = choisirDepartement();
            if (nouveauDept == null) {
                throw new InvalidAgentDataException("Département invalide");
            }
            agent.setDepartement(nouveauDept);
        }

        else if (agentConnecte.getTypeAgent() == TypeAgent.RESPONSABLE_DEPARTEMENT) {
            System.out.println("Nouveau type:");
            System.out.println("1. OUVRIER");
            System.out.println("2. STAGIAIRE");
            System.out.print("Choix: ");
            int choixType = scanner.nextInt();
            scanner.nextLine();

            if (choixType == 1) agent.setTypeAgent(TypeAgent.OUVRIER);
            else if (choixType == 2) agent.setTypeAgent(TypeAgent.STAGIAIRE);
            else {
                throw new InvalidAgentDataException("Type d'agent invalide");
            }
        }

        agentDAO.update(agent);
        System.out.println("Agent modifié avec succès");
    }

    public void supprimerAgent(Agent agentConnecte) {
        System.out.print("ID de l'agent à supprimer: ");
        int idAgent = scanner.nextInt();
        scanner.nextLine();

        Agent agent = agentDAO.findById(idAgent);
        if (agent == null) {
            throw new AgentNotFoundException(idAgent);
        }

        if (agentConnecte.getTypeAgent() == TypeAgent.DIRECTEUR) {
            if (agent.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT) {
                throw new UnauthorizedActionException("Vous ne pouvez supprimer que les responsables de département");
            }
        }
        else if (agentConnecte.getTypeAgent() == TypeAgent.RESPONSABLE_DEPARTEMENT) {
            if (agent.getTypeAgent() != TypeAgent.OUVRIER && agent.getTypeAgent() != TypeAgent.STAGIAIRE) {
                throw new UnauthorizedActionException("Vous ne pouvez supprimer que les ouvriers et stagiaires");
            }

            if (agent.getDepartement() == null ||
                    agentConnecte.getDepartement() == null ||
                    agent.getDepartement().getId() != agentConnecte.getDepartement().getId()) {
                throw new UnauthorizedActionException("Vous ne pouvez supprimer que les agents de votre département");
            }

            if (agent.getIdAgent() == agentConnecte.getIdAgent()) {
                throw new UnauthorizedActionException("Vous ne pouvez pas vous supprimer vous-même");
            }
        }
        agentDAO.delete(idAgent);
        System.out.println("Agent supprimé avec succès");
    }


    private TypeAgent choisirTypeAgent() {
        System.out.print("Type (1=Ouvrier, 2=Stagiaire): ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        if (choix != 1 && choix != 2) {
            throw new InvalidAgentDataException("Type d'agent invalide. Choisissez 1 ou 2.");
        }

        return (choix == 1) ? TypeAgent.OUVRIER : TypeAgent.STAGIAIRE;
    }

    private Departement choisirDepartement() {
        System.out.println("\n Départements disponibles:");
        List<Departement> departements = departementDAO.findAll();

        if (departements.isEmpty()) {
            throw new InvalidAgentDataException("Aucun département disponible");
        }

        for (Departement dept : departements) {
            System.out.println("ID: " + dept.getId() + " | " + dept.getNom());
        }

        System.out.print("ID du dépt: ");
        int idDept = scanner.nextInt();
        scanner.nextLine();

        Departement departement = departementDAO.findById(idDept);
        if (departement == null) {
            throw new InvalidAgentDataException("Département avec ID " + idDept + " non trouvé");
        }

        return departement;
    }
}