package controllers;

import services.AuthService;
import services.DepartementService;
import services.AgentService;
import models.Agent;
import services.PaiementService;
import ui.AgentMenu;
import ui.MainMenu;
import ui.DirecteurMenu;
import ui.ResponsableMenu;
import models.enums.TypeAgent;

public class MainController {
    private AuthService authService;
    private DepartementService departementService;
    private AgentService agentService;
    private MainMenu mainMenu;
    private DirecteurMenu directeurMenu;
    private ResponsableMenu responsableMenu;
    private PaiementService paiementService;
    private AgentMenu agentMenu;

    public MainController() {
        this.authService = new AuthService();
        this.departementService = new DepartementService();
        this.agentService = new AgentService();
        this.paiementService =new PaiementService();
        this.mainMenu = new MainMenu();
        this.directeurMenu = new DirecteurMenu(departementService, agentService , paiementService);
        this.responsableMenu = new ResponsableMenu(agentService , paiementService);
        this.agentMenu = new AgentMenu(paiementService);
    }

    public void startApplication() {

        authService.createDefaultDirector();
        mainMenu.afficherMenuPrincipal();


        Agent connectedAgent = mainMenu.demanderConnexion(authService);

        if (connectedAgent != null) {
            // Redirection selon le type
            if (connectedAgent.getTypeAgent() == TypeAgent.DIRECTEUR) {
                directeurMenu.afficherMenuDirecteur(connectedAgent);
            } else if (connectedAgent.getTypeAgent() == TypeAgent.RESPONSABLE_DEPARTEMENT) {
                responsableMenu.afficherMenuResponsable(connectedAgent);
            }else{
                agentMenu.afficherMenuAgent(connectedAgent);
            }
        }
    }
}