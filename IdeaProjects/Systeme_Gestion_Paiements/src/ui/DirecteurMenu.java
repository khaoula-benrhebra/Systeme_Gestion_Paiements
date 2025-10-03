package ui;
import services.StatistiqueService;
import exceptions.AgentNotFoundException;
import exceptions.EmailAlreadyExistsException;
import exceptions.InvalidAgentDataException;
import exceptions.UnauthorizedActionException;
import services.DepartementService;
import services.AgentService;
import models.Agent;
import services.PaiementService;

import java.util.Scanner;

public class DirecteurMenu {
    private Scanner scanner;
    private DepartementService departementService;
    private AgentService agentService;
    private PaiementService paiementService;
    private StatistiqueService statistiqueService;

    public DirecteurMenu(DepartementService departementService, AgentService agentService, PaiementService paiementService) {
        this.scanner = new Scanner(System.in);
        this.departementService = departementService;
        this.agentService = agentService;
        this.paiementService=paiementService;
        this.statistiqueService = new StatistiqueService();
    }

    public void afficherMenuDirecteur(Agent directeur) {
        int choix;
        do {
            System.out.println("\n MENU DIRECTEUR ");
            System.out.println("1. Créer un département");
            System.out.println("2. Voir les départements");
            System.out.println("3. Modifier un département");
            System.out.println("4. Supprimer un département");
            System.out.println("5. Créer un responsable");
            System.out.println("6. Modifier un responsable");
            System.out.println("7. Supprimer un responsable");
            System.out.println("8. Ajouter un paiement");
            System.out.println("9. Confirmer bonus/indemnités");
            System.out.println("10. Voir mes paiements");
            System.out.println("11. Filtrer et trier mes paiements");
            System.out.println("12. Statistiques de mon département");
            System.out.println("13. Statistiques d'un département");
            System.out.println("0. Déconnexion");
            System.out.print("Votre choix: ");

            choix = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choix) {
                    case 1:
                        departementService.createDepartement();
                        break;
                    case 2:
                        departementService.afficherTousDepts();
                        break;
                    case 3:
                        departementService.modifierDept();
                        break;
                    case 4:
                        departementService.supprimerDept();
                        break;
                    case 5:
                        agentService.creerAgent(directeur);
                        break;
                    case 6:
                        agentService.modifierAgent(directeur);
                        break;
                    case 7:
                        agentService.supprimerAgent(directeur);
                        break;
                    case 8:
                        paiementService.ajouterPaiementDirecteur(directeur);
                        break;
                    case 9:
                        paiementService.confirmerBonusIndemnites(directeur);
                        break;
                    case 10:
                        paiementService.voirMesPaiements(directeur);
                        break;
                    case 11:
                        paiementService.filtrerEtTrierMesPaiements(directeur);
                        break;

                    case 0:
                        System.out.println("Déconnexion");
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (AgentNotFoundException | EmailAlreadyExistsException |
                     InvalidAgentDataException | UnauthorizedActionException e) {
                System.out.println(" Erreur: " + e.getMessage());
            }

        } while (choix != 0);

    }

}