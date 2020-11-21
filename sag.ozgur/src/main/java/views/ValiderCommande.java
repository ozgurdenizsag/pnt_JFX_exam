package views;

import controlleur.Controleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modele.commande.Commande;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ValiderCommande implements Observateur {
    public BorderPane commandeRoot;
    public Label validerCommande;
    public ListView listeCommandePrecommande;
    private ArrayList<Commande> commandes = new ArrayList<>();
    private long idCommandeSelection;

    @Override
    public void notifier(Evenement event) {
        setCommandes();
        afficherCommandes();
    }


    private Controleur controleur;
    private Stage stage;

    public Stage getStage(){
        return this.stage;
    }

    private void setCommandes() {
        commandes = controleur.getCommandes();
    }

    public static ValiderCommande creerInstance(Controleur controleur, Stage stage) {
        URL location = ValiderCommande.class.getResource("/views/validercommande.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ValiderCommande view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        view.setCommandes();
        view.afficherCommandes();
        return view;
    }

    private void afficherCommandes() {
        listeCommandePrecommande.getItems().clear();
        for (Commande p: commandes) {
            listeCommandePrecommande.getItems().add("Commande pour " + p.getNom() + " à " + p.getAdresse() +" Statut : " + p.getStatut());
        }
        listeCommandePrecommande.getSelectionModel().selectedIndexProperty().addListener((v, oldValue, newValue) -> {
            if ((int) newValue >= 0){
                idCommandeSelection = commandes.get((int) newValue).getId();
            }
        });
    }

    public void show() {
        stage.setTitle("Menu");
        Scene scene = new Scene(commandeRoot, commandeRoot.getPrefWidth(), commandeRoot.getPrefHeight());
        stage.setScene(scene);
        stage.show();
    }


    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    public Controleur getControleur() {
        return controleur;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        stage.close();
    }

    public void validerCommande(ActionEvent actionEvent) {
        controleur.validerCommandes(idCommandeSelection,this);
    }

    public void retourAuMenu(){
        controleur.retourAuMenu();
    }
}
