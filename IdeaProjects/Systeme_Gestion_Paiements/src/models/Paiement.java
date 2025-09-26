package models;

import java.time.LocalDate;
import models.enums.TypePaiement;

public class Paiement {
    private int id;
    private Agent agent;
    private double montant;
    private LocalDate datePaiement;
    private TypePaiement typePaiement;
    private boolean conditionValidee;
    private String motif;



    public Paiement(int id, Agent agent, double montant, LocalDate datePaiement,
                    TypePaiement typePaiement, boolean conditionValidee, String motif) {
        this.id = id;
        this.agent = agent;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.conditionValidee = conditionValidee;
        this.motif = motif;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) { this.agent = agent; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public LocalDate getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }

    public TypePaiement getTypePaiement() { return typePaiement; }
    public void setTypePaiement(TypePaiement typePaiement) { this.typePaiement = typePaiement; }

    public boolean isConditionValidee() { return conditionValidee; }
    public void setConditionValidee(boolean conditionValidee) { this.conditionValidee = conditionValidee; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    @Override
    public String toString() {
        return "Paiement{" +
                "id=" + id +
                ", agent=" + (agent != null ? agent.getNom() : "Aucun") +
                ", montant=" + montant +
                ", date=" + datePaiement +
                ", type=" + typePaiement +
                ", conditionValidee=" + conditionValidee +
                ", motif='" + motif + '\'' +
                '}';
    }
}
