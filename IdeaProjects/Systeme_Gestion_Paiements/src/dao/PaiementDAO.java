package dao;

import models.Paiement;
import models.enums.TypePaiement;

import java.util.List;

public interface PaiementDAO {
    void create(Paiement paiement);
    List<Paiement> findByAgent(int agentId);
    List<Paiement> findAll();
    void update(Paiement paiement);
    List<Paiement> findByAgentWithFilter(int agentId, TypePaiement type, Double montantMin, Double montantMax);
    List<Paiement> findByAgentSorted(int agentId, String sortBy, String sortOrder);
}