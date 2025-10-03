package services;

import dao.DepartementDAO;
import dao.impl.DepartementDAOImpl;
import models.Departement;
import utils.Validator;

import java.util.List;
import java.util.Scanner;

public class DepartementService {
    private DepartementDAO departementDAO;
    private Scanner scanner;

    public DepartementService() {
        this.departementDAO = new DepartementDAOImpl();
        this.scanner = new Scanner(System.in);
    }

    public void createDepartement() {
        try {
            System.out.print("Nom du département: ");
            String nom = scanner.nextLine();

            Validator.validateNotEmpty(nom, "Nom de Departement");
            Departement existDepat = departementDAO.findByNom(nom);
            if (existDepat != null) {
                throw new IllegalArgumentException("ce departement avec ce nom exist déja");
            }

            Departement departement = new Departement();
            departement.setNom(nom);
            departementDAO.create(departement);
            System.out.println("le departement créer avec succées : " + departement);

        } catch (Exception e) {
            System.out.println("error lors de la creation de dept: " + e.getMessage());
        }
    }

    public void afficherTousDepts() {
        try {
            List<Departement> departements = departementDAO.findAll();
            if (departements.isEmpty()) {
                System.out.println("aucun department trouvé ");
                return;
            }

            System.out.println("la liste de departements est :");
            for (Departement dept : departements) {
                System.out.println("id de dept est:" + dept.getId() + " || Nom de dept est " + dept.getNom());
            }

        } catch (Exception e) {
            System.out.println("error lors d'affichage des departements " + e.getMessage());
        }
    }

    public void modifierDept() {
        try {
            afficherTousDepts();
            System.out.println("id de dept à modifier ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Departement departement = departementDAO.findById(id);
            if (departement == null) {
                System.out.println("aucun dept trouvé avec cet id :" + id);
                return;
            }

            System.out.println("dept actuel: " + departement.getNom());

            System.out.println("Vueiller enter nouveau nom de dept :");
            String nouveauNomDEpt = scanner.nextLine();
            Validator.validateNotEmpty(nouveauNomDEpt, "nouveau nom");

            Departement existeDeja = departementDAO.findByNom(nouveauNomDEpt);
            if (existeDeja != null && existeDeja.getId() != id) {
                System.out.println("ce nom de dept existe deja");
                return;
            }
            departement.setNom(nouveauNomDEpt);
            departementDAO.update(departement);
            System.out.println("le dept a été modifié avec sucuée ");

        } catch (Exception e) {
            System.out.println("error lors de la modification de dept " + e.getMessage());
        }
    }

    public void supprimerDept() {
        try {
            afficherTousDepts();

            System.out.println("id de dept à supprimer ");
            int id = scanner.nextInt();
            scanner.nextLine();
            Departement departement = departementDAO.findById(id);

            if (departement == null) {
                System.out.println("aucun dept trouvé avec ct id  " + id);
                return;
            }

            departementDAO.delete(id);
            System.out.println("dept supprimé avec succée ");
        } catch (Exception e) {
            System.out.println("eror lors de la suppression de dept " + e.getMessage());
        }
    }
}