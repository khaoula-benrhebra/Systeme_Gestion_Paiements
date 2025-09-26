import services.AuthService;
import models.Agent;
import utils.DBConnection;
import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Test de connexion à la base
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("Connexion OK!");
        } else {
            System.out.println("Échec de la connexion à la base de données !");
            return;
        }

        // Créer le service d'authentification
        AuthService authService = new AuthService();

        // Créer le directeur par défaut
        authService.createDefaultDirector();

        // Interface de login
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== SYSTÈME DE CONNEXION ===");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        // Tentative de connexion
        Agent connectedAgent = authService.login(email, password);

        if (connectedAgent != null) {
            System.out.println("\n=== CONNEXION RÉUSSIE ===");
            System.out.println("Bienvenue " + connectedAgent.getPrenom() + " " + connectedAgent.getNom());
            System.out.println("Type: " + connectedAgent.getTypeAgent());

            // Ici le menu principal aprés
        } else {
            System.out.println("Échec de la connexion");
        }

        scanner.close();
    }
}