package ui;

import exceptions.AgentNotFoundException;
import exceptions.EmailAlreadyExistsException;
import exceptions.InvalidAgentDataException;
import exceptions.UnauthorizedActionException;
import services.AgentService;
import models.Agent;
import services.PaiementService;

import java.util.Scanner;

public class ResponsableMenu {
    private Scanner scanner;
    private AgentService agentService;
    private PaiementService paiementService;

    public ResponsableMenu(AgentService agentService , PaiementService paiementService) {
        this.scanner = new Scanner(System.in);
        this.agentService = agentService;
        this.paiementService=paiementService;
    }

    public void afficherMenuResponsable(Agent responsable) {
        int choix;
        do {
            System.out.println("\n MENU RESPONSABLE ");
            System.out.println("1. Créer un agent");
            System.out.println("2. Modifier un agent");
            System.out.println("3. Supprimer un agent");
            System.out.println("4. Ajouter un paiement à un agent");
            System.out.println("5. Voir mes paiements");
            System.out.println("6. Filtrer et trier mes paiements");
            System.out.println("0. Déconnexion");
            System.out.print("Votre choix: ");

            choix = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choix) {
                    case 1:
                        agentService.creerAgent(responsable);
                        break;
                    case 2:
                        agentService.modifierAgent(responsable);
                        break;
                    case 3:
                        agentService.supprimerAgent(responsable);
                        break;
                    case 4:
                        paiementService.ajouterPaiementResponsable(responsable);
                        break;
                    case 5:
                        paiementService.voirMesPaiements(responsable);
                        break;
                    case 6:
                        paiementService.filtrerEtTrierMesPaiements(responsable);
                        break;
                    case 0:
                        System.out.println("Déconnexion");
                        break;
                    default:
                        System.out.println(" Choix invalide !");
                }
            } catch (AgentNotFoundException | EmailAlreadyExistsException |
                     InvalidAgentDataException | UnauthorizedActionException e) {
                System.out.println("Erreur: " + e.getMessage());
            }

        } while (choix != 0);
    }
}