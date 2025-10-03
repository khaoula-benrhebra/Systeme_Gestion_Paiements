package dao;

import models.Departement;
import java.util.List;

public interface DepartementDAO {
    void create(Departement departement);
    Departement findById(int id);
    Departement findByNom(String nom);
    List<Departement> findAll();
    void update(Departement departement);
    void delete(int id);
}