package ui;

import services.AuthService;
import models.Agent;
import java.util.Scanner;

public class MainMenu {
    private Scanner scanner;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void afficherMenuPrincipal() {
        System.out.println("  SYSTÈME DE GESTION DES PAIEMENTS  ");
    }

    public Agent demanderConnexion(AuthService authService) {
        System.out.println("\n CONNEXION ");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        return authService.login(email, password);
    }
}