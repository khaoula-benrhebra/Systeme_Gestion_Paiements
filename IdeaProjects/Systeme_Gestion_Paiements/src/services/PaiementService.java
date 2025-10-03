package services;

import dao.AgentDAO;
import dao.PaiementDAO;
import dao.impl.AgentDAOImpl;
import dao.impl.PaiementDAOImpl;
import models.Agent;
import models.Paiement;
import models.enums.TypeAgent;
import models.enums.TypePaiement;
import utils.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PaiementService {

    private PaiementDAO paiementDAO;
    private AgentDAO agentDAO;
    private Scanner scanner;

    public PaiementService() {
        this.paiementDAO = new PaiementDAOImpl();
        this.agentDAO = new AgentDAOImpl();
        this.scanner = new Scanner(System.in);
    }



        public void ajouterPaiementDirecteur(Agent agentConnecte) {
            try {

                if (agentConnecte.getTypeAgent() != TypeAgent.DIRECTEUR) {
                    System.out.println(" Accès refusé : Seul le directeur peut utiliser cette  fct");
                    return;
                }


                System.out.print("ID de l'agent (vous-même ou un responsable): ");
                int agentId = scanner.nextInt();
                scanner.nextLine();

                Agent agent = agentDAO.findById(agentId);
                if (agent == null) {
                    System.out.println("Agent introuvable");
                    return;
                }


                if (agent.getTypeAgent() != TypeAgent.DIRECTEUR &&
                        agent.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT) {
                    System.out.println(" Vous ne pouvez payer que les directeurs ou responsables");
                    return;
                }

                TypePaiement type = choisirTypePaiement();
                if (type == null) return;


                System.out.print("Montant: ");
                double montant = scanner.nextDouble();
                scanner.nextLine();
                Validator.validateMontant(montant);

                System.out.print("Motif: ");
                String motif = scanner.nextLine();
                Validator.validateNotEmpty(motif, "Motif");


                boolean conditionValidee;
                if (type == TypePaiement.SALAIRE || type == TypePaiement.PRIME) {
                    conditionValidee = true;
                } else {
                    conditionValidee = false;
                }

                // Créer paiement
                Paiement paiement = new Paiement();
                paiement.setType(type);
                paiement.setMontant(montant);
                paiement.setDate(LocalDate.now());
                paiement.setMotif(motif);
                paiement.setAgent(agent);
                paiement.setConditionValidee(conditionValidee);

                paiementDAO.create(paiement);
                System.out.println(" Paiement ajouté");

            } catch (Exception e) {
                System.out.println(" Erreur lors de la creation de paiment: " + e.getMessage());
            }
        }



    public void ajouterPaiementResponsable(Agent agentConnecte) {
        try {
            if (agentConnecte.getTypeAgent() != TypeAgent.RESPONSABLE_DEPARTEMENT) {
                System.out.println(" Accès refusé  Seul le responsable peut faire ce type de paiment ");
                return;
            }


            System.out.print("ID de l'agent: ");
            int agentId = scanner.nextInt();
            scanner.nextLine();

            Agent agent = agentDAO.findById(agentId);
            if (agent == null) {
                System.out.println(" Agent introuvable");
                return;
            }

            if (agent.getDepartement() == null ||
                    agent.getDepartement().getId() != agentConnecte.getDepartement().getId()) {
                System.out.println(" Cet agent n'est pas dans votre département");
                return;
            }

            if (agent.getTypeAgent() != TypeAgent.OUVRIER &&
                    agent.getTypeAgent() != TypeAgent.STAGIAIRE) {
                System.out.println(" Vous ne pouvez payer que les ouvriers et stagiaires");
                return;
            }


            System.out.println("choisi le type de paiement:");
            System.out.println("1. SALAIRE");
            System.out.println("2. PRIME");
            System.out.print("Choix: ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            TypePaiement type;
            if (choix == 1) type = TypePaiement.SALAIRE;
            else if (choix == 2) type = TypePaiement.PRIME;
            else {
                System.out.println(" Type invalide");
                return;
            }

            System.out.print("Montant: ");
            double montant = scanner.nextDouble();
            scanner.nextLine();
            Validator.validateMontant(montant);

            System.out.print("Motif: ");
            String motif = scanner.nextLine();
            Validator.validateNotEmpty(motif, "Motif");


            boolean conditionValidee = true;

            Paiement paiement = new Paiement();
            paiement.setType(type);
            paiement.setMontant(montant);
            paiement.setDate(LocalDate.now());
            paiement.setMotif(motif);
            paiement.setAgent(agent);
            paiement.setConditionValidee(conditionValidee);

            paiementDAO.create(paiement);
            System.out.println(" Paiement ajouté");

        } catch (Exception e) {
            System.out.println(" Erreur de l'ajout de paiment de vous agents (ouvrier ou stagiare): " + e.getMessage());
        }
    }

    public void confirmerBonusIndemnites(Agent agentConnecte) {
        try {

            if (agentConnecte.getTypeAgent() != TypeAgent.DIRECTEUR) {
                System.out.println(" Seul le directeur peut confirmer les bonus/indemnités");
                return;
            }


            List<Paiement> tousPaiements = paiementDAO.findAll();

            List<Paiement> aConfirmer = tousPaiements.stream()
                    .filter(p -> (p.getType() == TypePaiement.BONUS || p.getType() == TypePaiement.INDEMNITE)
                            && !p.isConditionValidee())
                    .toList();

            if (aConfirmer.isEmpty()) {
                System.out.println(" Aucun bonus/indemnité à confirmer");
                return;
            }

            System.out.println("Paiements à confirmer:");
            for (int i = 0; i < aConfirmer.size(); i++) {
                Paiement p = aConfirmer.get(i);
                System.out.println((i+1) + ". ID:" + p.getIdPaiement() + " | " + p.toString());
            }

            System.out.print("Numéro du paiement à confirmer: ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            if (choix < 1 || choix > aConfirmer.size()) {
                System.out.println(" Choix invalide");
                return;
            }

            Paiement paiement = aConfirmer.get(choix - 1);
            paiement.setConditionValidee(true);

            paiementDAO.update(paiement);
            System.out.println(" Paiement " + paiement.getType() + " confirmé et sauvegardé !");

        } catch (Exception e) {
            System.out.println("Erreur de la confirmation de paiment: " + e.getMessage());
        }
    }

    public void voirMesPaiements(Agent agent) {
        try {
            List<Paiement> paiements = paiementDAO.findByAgent(agent.getIdAgent());

            if (paiements.isEmpty()) {
                System.out.println("Aucun paiement trouvé.");
                return;
            }

            System.out.println("\n= MES PAIEMENTS =");
            for (Paiement p : paiements) {
                System.out.println(p.toString());
            }

        } catch (Exception e) {
            System.out.println("Erreur d'affichage d'historique de paiment : " + e.getMessage());
        }
    }


    private TypePaiement choisirTypePaiement() {
        System.out.println("Type de paiement:");
        System.out.println("1. SALAIRE");
        System.out.println("2. PRIME");
        System.out.println("3. BONUS");
        System.out.println("4. INDEMNITE");
        System.out.print("Choix: ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1: return TypePaiement.SALAIRE;
            case 2: return TypePaiement.PRIME;
            case 3: return TypePaiement.BONUS;
            case 4: return TypePaiement.INDEMNITE;
            default:
                System.out.println("Type invalide");
                return null;
        }
    }

    /// Filtre
    public void filtrerEtTrierMesPaiements(Agent agent) {
        try {

            List<Paiement> tousPaiements = paiementDAO.findByAgent(agent.getIdAgent());

            if (tousPaiements.isEmpty()) {
                System.out.println("Aucun paiement trouvé.");
                return;
            }

            List<Paiement> paiementsFiltres = filtrerParType(agent, tousPaiements);

            Collections.sort(paiementsFiltres, (p1, p2) -> p2.getDate().compareTo(p1.getDate()));

            afficherResultats(paiementsFiltres);

        } catch (Exception e) {
            System.out.println("Erreur lors du filtrage: " + e.getMessage());
        }
    }

    private List<Paiement> filtrerParType(Agent agent, List<Paiement> paiements) {

        if (agent.getTypeAgent() == TypeAgent.OUVRIER || agent.getTypeAgent() == TypeAgent.STAGIAIRE) {
            System.out.println("1. Voir tous les paiements");
            System.out.println("2. SALAIRE ");
            System.out.println("3. PRIME");
            System.out.print("Votre choix: ");
        } else {
            System.out.println("1. Voir tous les paiements");
            System.out.println("2. SALAIRE");
            System.out.println("3. PRIME");
            System.out.println("4. BONUS");
            System.out.println("5. INDEMNITE");
            System.out.print("Votre choix: ");
        }

        int choix = scanner.nextInt();
        scanner.nextLine();

        List<Paiement> resultats = new ArrayList<>();

        if (choix == 1) {
            resultats = paiements;
        } else if (choix == 2) {
            for (Paiement p : paiements) {
                if (p.getType() == TypePaiement.SALAIRE) {
                    resultats.add(p);
                }
            }
        } else if (choix == 3) {
            for (Paiement p : paiements) {
                if (p.getType() == TypePaiement.PRIME) {
                    resultats.add(p);
                }
            }
        } else if (choix == 4) {
            if (agent.getTypeAgent() == TypeAgent.DIRECTEUR ||
                    agent.getTypeAgent() == TypeAgent.RESPONSABLE_DEPARTEMENT) {
                for (Paiement p : paiements) {
                    if (p.getType() == TypePaiement.BONUS) {
                        resultats.add(p);
                    }
                }
            } else {
                System.out.println("Option non disponible pour votre rôle.");
                resultats = paiements;
            }
        } else if (choix == 5) {
            if (agent.getTypeAgent() == TypeAgent.DIRECTEUR ||
                    agent.getTypeAgent() == TypeAgent.RESPONSABLE_DEPARTEMENT) {
                for (Paiement p : paiements) {
                    if (p.getType() == TypePaiement.INDEMNITE) {
                        resultats.add(p);
                    }
                }
            } else {
                System.out.println("Option non disponible pour votre rôle.");
                resultats = paiements;
            }
        } else {
            System.out.println("Choix invalide, affichage de tous les paiements.");
            resultats = paiements;
        }

        return resultats;
    }

    private void afficherResultats(List<Paiement> paiements) {
        if (paiements.isEmpty()) {
            System.out.println("\nAucun paiement trouvé avec ces critères.");
            return;
        }

        System.out.println("\n RÉSULTATS (" + paiements.size() + " paiements) ");

        double total = 0;
        for (Paiement p : paiements) {
            System.out.println(p.toString());
            total += p.getMontant();
        }

        System.out.println("TOTAL: " + total + " dh");
    }

}
