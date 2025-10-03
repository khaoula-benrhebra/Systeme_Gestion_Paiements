package dao.impl;

import dao.AgentDAO;
import models.Agent;
import models.Departement;
import models.enums.TypeAgent;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgentDAOImpl implements AgentDAO {

    @Override
    public void create(Agent agent) {
        String sql = "insert  into agent (nom, prenom, email, mot_de_passe, type_agent, departement_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, agent.getNom());
            pstmt.setString(2, agent.getPrenom());
            pstmt.setString(3, agent.getEmail());
            pstmt.setString(4, agent.getMotDePasse());
            pstmt.setString(5, agent.getTypeAgent().name());

            if (agent.getDepartement() != null) {
                pstmt.setInt(6, agent.getDepartement().getId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            pstmt.executeUpdate();
            System.out.println("Agent créé avec succès");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Agent findByEmail(String email) {
        String sql = "select  a.*, d.nom as departement_nom from agent a left join departement d ON a.departement_id = d.id WHERE a.email = ?";
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

                if (rs.getInt("departement_id") > 0) {
                    Departement dept = new Departement();
                    dept.setId(rs.getInt("departement_id"));
                    dept.setNom(rs.getString("departement_nom"));
                    agent.setDepartement(dept);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agent;
    }

    @Override
    public Agent findById(int idAgent) {
        String sql = "select a.*, d.nom as departement_nom FROM agent a left join departement d ON a.departement_id = d.id WHERE a.id = ?";
        Agent agent = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAgent);
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

                if (rs.getInt("departement_id") > 0) {
                    Departement dept = new Departement();
                    dept.setId(rs.getInt("departement_id"));
                    dept.setNom(rs.getString("departement_nom"));
                    agent.setDepartement(dept);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agent;
    }

    @Override
    public List<Agent> findAll() {
        String sql = "select a.*, d.nom as departement_nom FROM agent a left join departement d ON a.departement_id = d.id";
        List<Agent> agents = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Agent agent = new Agent(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        TypeAgent.valueOf(rs.getString("type_agent"))
                );

                if (rs.getInt("departement_id") > 0) {
                    Departement dept = new Departement();
                    dept.setId(rs.getInt("departement_id"));
                    dept.setNom(rs.getString("departement_nom"));
                    agent.setDepartement(dept);
                }

                agents.add(agent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agents;
    }

    @Override
    public void update(Agent agent) {
        String sql = "UPDATE agent SET nom=?, prenom=?, email=?, mot_de_passe=?, type_agent=?, departement_id=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, agent.getNom());
            pstmt.setString(2, agent.getPrenom());
            pstmt.setString(3, agent.getEmail());
            pstmt.setString(4, agent.getMotDePasse());
            pstmt.setString(5, agent.getTypeAgent().name());

            if (agent.getDepartement() != null) {
                pstmt.setInt(6, agent.getDepartement().getId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            pstmt.setInt(7, agent.getIdAgent());
            pstmt.executeUpdate();
            System.out.println(" Agent mis à jour avec succès");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int idAgent) {
        String sql = "DELETE FROM agent WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAgent);
            pstmt.executeUpdate();
            System.out.println(" Agent supprimé avec succès");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void affecterDepartement(int idAgent, int idDepartement) {
        String sql = "UPDATE agent SET departement_id = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idDepartement);
            pstmt.setInt(2, idAgent);
            pstmt.executeUpdate();
            System.out.println(" Agent " + idAgent + " affecté au département " + idDepartement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}