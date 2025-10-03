package models;

import java.time.LocalDate;
import models.enums.TypePaiement;

public class Paiement {
    private int idPaiement;
    private TypePaiement type;
    private double montant;
    private LocalDate date;
    private String motif;
    private Agent agent;
    private boolean conditionValidee;

    public Paiement(){};


    public Paiement(int idPaiement, TypePaiement type, double montant, LocalDate date, String motif, Agent agent, boolean conditionValidee) {
        this.idPaiement = idPaiement;
        this.type = type;
        this.montant = montant;
        this.date = date;
        this.motif = motif;
        this.agent = agent;
        this.conditionValidee = conditionValidee;
    }


    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public TypePaiement getType() {
        return type;
    }

    public void setType(TypePaiement type) {
        this.type = type;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public boolean isConditionValidee() {
        return conditionValidee;
    }

    public void setConditionValidee(boolean conditionValidee) {
        this.conditionValidee = conditionValidee;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "idPaiement=" + idPaiement +
                ", type=" + type +
                ", montant=" + montant +
                ", date=" + date +
                ", motif='" + motif + '\'' +
                ", agent=" + agent +
                ", conditionValidee=" + conditionValidee +
                '}';
    }
}
