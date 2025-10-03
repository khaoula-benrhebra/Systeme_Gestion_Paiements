package services;

import dao.AgentDAO;
import dao.PaiementDAO;
import dao.impl.AgentDAOImpl;
import dao.impl.PaiementDAOImpl;
import models.Agent;
import models.Departement;
import models.Paiement;
import models.enums.TypePaiement;

import java.util.*;
import java.util.stream.Collectors;

public class StatistiqueService {
    private PaiementDAO paiementDAO;
    private AgentDAO agentDAO;

    public StatistiqueService() {
        this.paiementDAO = new PaiementDAOImpl();
        this.agentDAO = new AgentDAOImpl();
    }


    /// Compter le nombre de paiements par type
    public Map<TypePaiement, Long> compterPaiementsParType(List<Paiement> paiements) {
        return paiements.stream()
                .collect(Collectors.groupingBy(
                        Paiement::getType,
                        Collectors.counting()
                ));
    }


     /// Calcul du salaire moyen du département
    public Double calculerSalaireMoyenDepartement(List<Agent> agents) {
        OptionalDouble moyenne = agents.stream()
                .mapToDouble(agent -> {
                    List<Paiement> paiements = paiementDAO.findByAgent(agent.getIdAgent());
                    return paiements.stream()
                            .mapToDouble(Paiement::getMontant)
                            .sum();
                })
                .average();

        return moyenne.orElse(0.0);
    }




    }