package ui;

import services.PaiementService;
import models.Agent;
import java.util.Scanner;

public class AgentMenu {
    private Scanner scanner;
    private PaiementService paiementService;

    public AgentMenu(PaiementService paiementService) {
        this.scanner = new Scanner(System.in);
        this.paiementService = paiementService;
    }

    public void afficherMenuAgent(Agent agent) {
        int choix;
        do {
            System.out.println("\n=== MENU AGENT ===");
            System.out.println("1. Voir mes informations");
            System.out.println("2. Voir mes paiements");
            System.out.println("3. Filtrer et trier mes paiements");
            System.out.println("0. Déconnexion");
            System.out.print("Votre choix: ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    afficherMesInformations(agent);break;
                case 2:
                    paiementService.voirMesPaiements(agent);break;
                case 3:
                    paiementService.filtrerEtTrierMesPaiements(agent);break;
                case 0:
                    System.out.println("Déconnexion...");break;
                default: System.out.println(" Choix invalide !");
            }
        } while (choix != 0);
    }

    private void afficherMesInformations(Agent agent) {
        System.out.println("\n= MES INFORMATIONS =");
        System.out.println("Nom: " + agent.getNom());
        System.out.println("Prénom: " + agent.getPrenom());
        System.out.println("Email: " + agent.getEmail());
        System.out.println("Type: " + agent.getTypeAgent());
        if (agent.getDepartement() != null) {
            System.out.println("Département: " + agent.getDepartement().getNom());
        }
    }
}