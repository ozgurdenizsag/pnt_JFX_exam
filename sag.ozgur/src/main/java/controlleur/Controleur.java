package controlleur;


import facade.erreurs.CleAuthentificationInvalideException;
import facade.erreurs.CommandeNotFoundException;
import facade.erreurs.MauvaisLoginPasswordException;
import facade.erreurs.UtilisateurDejaConnecteException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import facade.ServiceImpl;
import modele.commande.Commande;
import modele.commande.Statut;
import modele.personnes.Personnel;
import modele.pizza.Pizza;
import observer.*;
import views.*;

import java.util.ArrayList;
import java.util.Collection;

public class Controleur {
    private Stage fenetrePrincipale;
    private ServiceImpl facade;

    //Pages
    private ValiderLogin validerLogin;
    private ValiderMdp validerMdp;
    private Menu menu;
    private ValiderCommande validerCommande;
    private MenuClient menuClient;
    private SaisieCommande saisieCommande;

    //Variables
    private Personnel personnel;
    private String identifiant;


    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    private Collection<Observateur> observateurs = new ArrayList<>();

    public void ajouterObservateur(Observateur o){
        observateurs.add(o);
    }

    public void enleverObservateur(Observateur o){
        observateurs.remove(o);
    }

    public void notifierToutLeMonde(Evenement e){
        for(Observateur o : observateurs){
            o.notifier(e);
        }
    }

    public void validerLogin(String login){
        personnel =  facade.getPersonnelParLogin(login);
        if (personnel != null){
            this.validerMdp = ValiderMdp.creerInstance(this,new Stage());
            validerMdp.show();
            validerLogin.close();
        }
    }




    public Controleur(Stage stage, ServiceImpl facadeParis) {
        facade = facadeParis;
        fenetrePrincipale = stage;
        this.validerLogin = ValiderLogin.creerInstance(this,fenetrePrincipale);
        validerLogin.show();
    }

    public void messageErreur(String msg){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Erreur");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(msg);
        Button closeButton = new Button("Fermer !");
        closeButton.setOnAction(ex -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout,layout.getPrefWidth(),layout.getPrefHeight());
        window.setScene(scene);
        window.showAndWait();
    }

    public void pagePrecedenteLogin() {
        validerLogin.getStage().show();
        validerMdp.close();
    }

    public void afficherPageCommande() {
        this.menuClient = MenuClient.creerInstance(this,new Stage());
        menuClient.show();
        validerLogin.close();
    }

    public void afficherMenu() {
        try {
            identifiant = facade.connexion(personnel.getNom(),personnel.getMdp());
            this.menu = Menu.creerInstance(this,new Stage());
            menu.show();
            validerMdp.close();
        } catch (MauvaisLoginPasswordException e ){
            messageErreur("Erreur");
        } catch (UtilisateurDejaConnecteException e){
            messageErreur("erreur");
        }
    }

    public void afficherPageValiderCommandes() {
        this.validerCommande = ValiderCommande.creerInstance(this,new Stage());
        ajouterObservateur(validerCommande);
        validerCommande.show();
        menu.close();
    }

    public ArrayList<Commande> getCommandes() {
        return (ArrayList<Commande>) facade.getCommandesParStatut(identifiant,Statut.PRECOMMANDE);
    }

    public void validerCommandes(long id,Object source){
        try {
            facade.validerPrecommande(identifiant,id);
            notifierToutLeMonde(new Evenement(source));
            messageErreur("Commande Validé");
        }catch (CleAuthentificationInvalideException e){
            messageErreur("Erreur");
        }catch (CommandeNotFoundException e){
            messageErreur("erreur");
        }
    }

    public void retourAuMenu(){
        menu.getStage().show();
        validerCommande.close();
    }

    public void retourAuMenuFromClient() {
        menu.getStage().show();
        menuClient.close();
    }

    public void afficherPageSaisieCommande() {
        this.saisieCommande = SaisieCommande.creerInstance(this,new Stage());
        saisieCommande.show();
        menuClient.close();
    }
}
