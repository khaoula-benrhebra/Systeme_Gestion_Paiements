package dao.impl;

import dao.PaiementDAO;
import models.Paiement;
import models.Agent;
import models.enums.TypePaiement;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAOImpl implements PaiementDAO {

    @Override
    public void create(Paiement paiement) {
        String sql = "insert into paiement (montant, date_paiement, type_paiement, agent_id, condition_validee, motif) values (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, paiement.getMontant());
            pstmt.setDate(2, Date.valueOf(paiement.getDate()));
            pstmt.setString(3, paiement.getType().name());
            pstmt.setInt(4, paiement.getAgent().getIdAgent());
            pstmt.setBoolean(5, paiement.isConditionValidee());
            pstmt.setString(6, paiement.getMotif());

            pstmt.executeUpdate();
            System.out.println(" Paiement créé avec succès");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Paiement> findByAgent(int agentId) {
        String sql = "select * from paiement where agent_id = ?";
        List<Paiement> paiements = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, agentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Paiement paiement = new Paiement();
                paiement.setIdPaiement(rs.getInt("id"));
                paiement.setMontant(rs.getDouble("montant"));
                paiement.setDate(rs.getDate("date_paiement").toLocalDate());
                paiement.setType(TypePaiement.valueOf(rs.getString("type_paiement")));
                paiement.setConditionValidee(rs.getBoolean("condition_validee"));
                paiement.setMotif(rs.getString("motif"));


                Agent agent = new Agent();
                agent.setIdAgent(agentId);
                paiement.setAgent(agent);

                paiements.add(paiement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    @Override
    public List<Paiement> findAll() {
        String sql = "select * from paiement";
        List<Paiement> paiements = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Paiement paiement = new Paiement();
                paiement.setIdPaiement(rs.getInt("id"));
                paiement.setMontant(rs.getDouble("montant"));
                paiement.setDate(rs.getDate("date_paiement").toLocalDate());
                paiement.setType(TypePaiement.valueOf(rs.getString("type_paiement")));
                paiement.setConditionValidee(rs.getBoolean("condition_validee"));
                paiement.setMotif(rs.getString("motif"));

                Agent agent = new Agent();
                agent.setIdAgent(rs.getInt("agent_id"));
                paiement.setAgent(agent);

                paiements.add(paiement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }


        @Override
        public void update(Paiement paiement) {
            String sql = "update paiement set condition_validee = ? where id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setBoolean(1, paiement.isConditionValidee());
                pstmt.setInt(2, paiement.getIdPaiement());

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println(" Paiement mis à jour dans la base de données");
                } else {
                    System.out.println(" Aucun paiement trouvé avec cet ID");
                }

            } catch (SQLException e) {
                System.out.println(" Erreur lors de la mise à jour du paiement: " + e.getMessage());
                e.printStackTrace();
            }

    }

    @Override
    public List<Paiement> findByAgentWithFilter(int agentId, TypePaiement type, Double montantMin, Double montantMax) {
        return List.of();
    }

    @Override
    public List<Paiement> findByAgentSorted(int agentId, String sortBy, String sortOrder) {
        return List.of();
    }
}