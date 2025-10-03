package dao.impl;


import dao.DepartementDAO;
import models.Departement;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class DepartementDAOImpl implements DepartementDAO {

    @Override
    public void create(Departement departement) {
        String sql = "insert into departement (nom) values (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, departement.getNom());
            pstmt.executeUpdate();

            // Récupérer l'ID généré
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                departement.setId(generatedKeys.getInt(1));
            }

            System.out.println(" Département créé avec succès: " + departement.getNom());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Departement findById(int id) {
        String sql = "select * from departement where id = ?";
        Departement departement = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                departement = new Departement();
                departement.setId(rs.getInt("id"));
                departement.setNom(rs.getString("nom"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departement;
    }

    @Override
    public Departement findByNom(String nom) {
        String sql = "select * from departement where nom = ?";
        Departement departement = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nom);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                departement = new Departement();
                departement.setId(rs.getInt("id"));
                departement.setNom(rs.getString("nom"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departement;
    }

    @Override
    public List<Departement> findAll() {
        String sql = "select * from departement";
        List<Departement> departements = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Departement departement = new Departement();
                departement.setId(rs.getInt("id"));
                departement.setNom(rs.getString("nom"));
                departements.add(departement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departements;
    }

    @Override
    public void update(Departement departement) {
        String sql = "update departement set nom = ? where id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, departement.getNom());
            pstmt.setInt(2, departement.getId());
            pstmt.executeUpdate();
            System.out.println(" Département mis à jour avec succès");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "delete from departement where id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println(" Département supprimé avec succès");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
