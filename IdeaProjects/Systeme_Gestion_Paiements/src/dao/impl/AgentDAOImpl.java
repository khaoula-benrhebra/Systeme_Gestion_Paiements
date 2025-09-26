package dao.impl;

import dao.AgentDAO;
import models.Agent;
import models.enums.TypeAgent;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgentDAOImpl implements AgentDAO {

    @Override
    public void create(Agent agent) {
        // Modifié: on n'insère pas l'id car il est auto-incrémenté
        String sql = "insert into agent (nom, prenom, email, mot_de_passe, type_agent) values (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, agent.getNom());
            pstmt.setString(2, agent.getPrenom());
            pstmt.setString(3, agent.getEmail());
            pstmt.setString(4, agent.getMotDePasse());
            pstmt.setString(5, agent.getTypeAgent().name());

            pstmt.executeUpdate();
            System.out.println("Agent créé avec succès");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Agent findByEmail(String email) {

        String sql = "select * from agent where email = ?";
        Agent agent = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                agent = new Agent(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        TypeAgent.valueOf(rs.getString("type_agent"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agent;
    }

    @Override
    public Agent findById(int idAgent) {
        return null;
    }

    @Override
    public List<Agent> findAll() {
        return new ArrayList<>();
    }

    @Override
    public void update(Agent agent) {

    }

    @Override
    public void delete(int idAgent) {

    }
}